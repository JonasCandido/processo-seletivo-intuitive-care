package com.jonascandido.ansdespesas.processor;

import com.jonascandido.ansdespesas.client.OperadoraApiClient;
import com.jonascandido.ansdespesas.model.Trimestre;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Locale;

@Component
public class Consolidator {

    private final OperadoraApiClient operadoraApiClient;
    private final Map<String, CacheEntry> cacheMap = new HashMap<>();
    private final Path cacheFile = Paths.get("temp", "operadoras_cache.json");
    private final long EXPIRATION_DAYS = 30;

    private final NumberFormat realFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static class CacheEntry {
        public String cnpj;
        public String razao;
        public long lastUpdated;

        public CacheEntry() {}
        public CacheEntry(String cnpj, String razao) {
            this.cnpj = cnpj;
            this.razao = razao;
            this.lastUpdated = Instant.now().toEpochMilli();
        }
    }

    public Consolidator(OperadoraApiClient operadoraApiClient) {
        this.operadoraApiClient = operadoraApiClient;
        loadCache();
    }

    public Path consolidate(List<Path> files) throws IOException {
        Path csvPath = Paths.get("temp", "consolidado_despesas.csv");
        Files.createDirectories(csvPath.getParent());

        // chave = CNPJ-Ano-Trimestre
        Map<String, Double> despesasPorChave = new TreeMap<>(
                Comparator.comparing((String key) -> Integer.parseInt(key.split("-")[1])) // ano
                        .thenComparingInt(key -> Integer.parseInt(key.split("-")[2]))   // trimestre
                        .thenComparing(key -> key.split("-")[0])                        // CNPJ
        );

        Map<String, String> razaoPorChave = new HashMap<>();
        Map<String, Set<String>> razoesPorCnpj = new HashMap<>(); // para detectar CNPJs duplicados com diferentes razões

        for (Path file : files) {
            List<String[]> rows = extractRows(file);
            for (String[] cols : rows) {
                try {
                    if (cols.length < 6) {
                        System.err.println("Linha ignorada (menos de 6 colunas): " + Arrays.toString(cols));
                        continue;
                    }

                    if (!isNumeric(cols[4]) || !isNumeric(cols[5])) {
                        System.err.println("Linha ignorada (colunas de valor não numéricas): " + Arrays.toString(cols));
                        continue;
                    }

                    double saldoInicial = parseValor(cols[4]);
                    double saldoFinal = parseValor(cols[5]);
                    double valorDespesa = saldoFinal - saldoInicial;

                    String regAns = cols[1].replace("\"", "").trim();

                    CacheEntry entry = cacheMap.get(regAns);
                    if (entry == null || isExpired(entry)) {
                        String cnpj = operadoraApiClient.getCnpj(regAns);
                        String razao = operadoraApiClient.getRazaoSocial(regAns);
                        entry = new CacheEntry(cnpj, razao);
                        cacheMap.put(regAns, entry);
                    }

                    // valida CNPJs com diferentes razões sociais
                    razoesPorCnpj.putIfAbsent(entry.cnpj, new HashSet<>());
                    Set<String> razoes = razoesPorCnpj.get(entry.cnpj);
                    if (!razoes.contains(entry.razao)) {
                        if (!razoes.isEmpty()) {
                            System.err.println("CNPJ duplicado com razões sociais diferentes: " + entry.cnpj +
                                    " -> existentes: " + razoes + ", nova: " + entry.razao);
                        }
                        razoes.add(entry.razao);
                    }

                    // Detecta o trimestre e ano, validando mês
                    int ano = 0;
                    int tri = 0;
                    try {
                        String dataStr = cols[0].replace("\"", "").trim();
                        String[] parts = dataStr.split("-");
                        if (parts.length < 2) {
                            System.err.println("Data inválida (menos de 2 partes): " + Arrays.toString(cols));
                            continue;
                        }

                        ano = Integer.parseInt(parts[0]);
                        int mes = Integer.parseInt(parts[1]);
                        if (mes < 1 || mes > 12) {
                            System.err.println("Mês inválido na linha: " + Arrays.toString(cols));
                            continue; // pula linha inválida
                        }
                        tri = (mes - 1) / 3 + 1;

                    } catch (Exception e) {
                        System.err.println("Não foi possível determinar trimestre/ano da linha: " + Arrays.toString(cols));
                        continue;
                    }

                    String key = entry.cnpj + "-" + ano + "-" + tri;
                    despesasPorChave.merge(key, valorDespesa, Double::sum);
                    razaoPorChave.putIfAbsent(key, entry.razao);

                } catch (Exception e) {
                    System.err.println("Erro ao processar linha: " + Arrays.toString(cols) + " -> " + e.getMessage());
                }
            }
        }

        // escreve CSV consolidado (uma linha por CNPJ/Ano/Trimestre)
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write("CNPJ;RazaoSocial;Trimestre;Ano;ValorDespesas\n");
            for (String key : despesasPorChave.keySet()) {
                String[] parts = key.split("-");
                String cnpj = parts[0];
                int ano = Integer.parseInt(parts[1]);
                int tri = Integer.parseInt(parts[2]);
                double valor = despesasPorChave.get(key);

                if (valor <= 0) {
                    System.err.println("Valor de despesa zero ou negativo para " + cnpj +
                            " no trimestre " + tri + "/" + ano);
                }

                writer.write(String.join(";",
                        cnpj,
                        razaoPorChave.get(key),
                        String.valueOf(tri),
                        String.valueOf(ano),
                        formatReal(valor)
                ));
                writer.write("\n");
            }
        }

        saveCache();

        // compacta em ZIP
        Path zipPath = Paths.get("temp", "consolidado_despesas.zip");
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            zos.putNextEntry(new ZipEntry(csvPath.getFileName().toString()));
            Files.copy(csvPath, zos);
            zos.closeEntry();
        }

        return zipPath;
    }

    private String formatReal(double valor) {
        // remove símbolo R$ e garante que não fique espaço
        return realFormat.format(valor).replace("R$", "").replace("\u00A0", "").trim();
    }

    // Converte string para double, retorna 0 e registra caso não seja numérico
    private double parseValor(String s) {
        if (s == null || s.isEmpty()) return 0.0;
        s = s.replace("\"", "").replace(".", "").replace(",", ".").trim();
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            System.err.println("Coluna não numérica ignorada: \"" + s + "\"");
            return 0.0;
        }
    }

    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        s = s.replace("\"", "").replace(".", "").replace(",", ".").trim();
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isExpired(CacheEntry entry) {
        return Instant.ofEpochMilli(entry.lastUpdated)
                .plus(EXPIRATION_DAYS, ChronoUnit.DAYS)
                .isBefore(Instant.now());
    }

    private void loadCache() {
        if (!Files.exists(cacheFile)) return;
        try {
            List<Map<String, Object>> list = objectMapper.readValue(cacheFile.toFile(),
                    new TypeReference<>() {});
            for (Map<String, Object> map : list) {
                CacheEntry entry = new CacheEntry();
                entry.cnpj = (String) map.get("cnpj");
                entry.razao = (String) map.get("razao");
                entry.lastUpdated = ((Number) map.get("lastUpdated")).longValue();
                String regAns = (String) map.get("regAns");
                cacheMap.put(regAns, entry);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar cache: " + e.getMessage());
        }
    }

    private void saveCache() {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Map.Entry<String, CacheEntry> e : cacheMap.entrySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("regAns", e.getKey());
                map.put("cnpj", e.getValue().cnpj);
                map.put("razao", e.getValue().razao);
                map.put("lastUpdated", e.getValue().lastUpdated);
                list.add(map);
            }
            Files.createDirectories(cacheFile.getParent());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(cacheFile.toFile(), list);
        } catch (Exception e) {
            System.err.println("Erro ao salvar cache: " + e.getMessage());
        }
    }

    /** Extrai linhas de CSV, TXT ou XLSX */
    private List<String[]> extractRows(Path file) throws IOException {
        String name = file.getFileName().toString().toLowerCase();
        List<String[]> rows = new ArrayList<>();

        if (name.endsWith(".csv") || name.endsWith(".txt")) {
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line;
                reader.readLine(); // ignora header
                while ((line = reader.readLine()) != null) {
                    rows.add(line.split("[;\t]"));
                }
            }
        } else if (name.endsWith(".xlsx")) {
            try (Workbook wb = new XSSFWorkbook(Files.newInputStream(file))) {
                Sheet sheet = wb.getSheetAt(0);
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;
                    String[] cols = new String[row.getLastCellNum()];
                    for (int c = 0; c < row.getLastCellNum(); c++) {
                        Cell cell = row.getCell(c);
                        cols[c] = (cell != null ? cell.toString() : "");
                    }
                    rows.add(cols);
                }
            }
        }
        return rows;
    }
}