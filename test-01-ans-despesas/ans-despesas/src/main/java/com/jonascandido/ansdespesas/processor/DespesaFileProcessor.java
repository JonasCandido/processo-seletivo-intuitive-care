package com.jonascandido.ansdespesas.processor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DespesaFileProcessor {

    private static final String KEYWORD = "Despesas com Eventos/Sinistros";

    public List<Path> filterDespesaFiles(List<Path> files) {
        return files.stream()
                .filter(this::containsKeyword)
                .collect(Collectors.toList());
    }

    private boolean containsKeyword(Path file) {
        String name = file.getFileName().toString().toLowerCase();

        try {
            if (name.endsWith(".csv") || name.endsWith(".txt")) {
                return containsKeywordInText(file);
            } else if (name.endsWith(".xlsx")) {
                return containsKeywordInXlsx(file);
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }

    // TXT/CSV: lê linha por linha
    private boolean containsKeywordInText(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return reader.lines().anyMatch(line -> line.contains(KEYWORD));
        }
    }

    // XLSX: detecta coluna “Descrição” e checa cada linha
    private boolean containsKeywordInXlsx(Path file) throws IOException {
        try (Workbook wb = new XSSFWorkbook(Files.newInputStream(file))) {
            Sheet sheet = wb.getSheetAt(0);

            // Detecta coluna de descrição
            int descricaoCol = -1;
            Row header = sheet.getRow(0);
            if (header != null) {
                for (Cell cell : header) {
                    if (matchesDescricao(cell.getStringCellValue())) {
                        descricaoCol = cell.getColumnIndex();
                        break;
                    }
                }
            }
            if (descricaoCol == -1) return false;

            // Checa cada linha da coluna de descrição
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                Cell cell = row.getCell(descricaoCol);
                if (cell != null && cell.getCellType() == CellType.STRING &&
                    cell.getStringCellValue().contains(KEYWORD)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Normaliza qualquer arquivo (TXT, CSV, XLSX) para String[] de colunas
     */
    public List<String[]> normalizeFile(Path file) throws IOException {
        List<String[]> rows = new ArrayList<>();
        String name = file.getFileName().toString().toLowerCase();

        if (name.endsWith(".csv") || name.endsWith(".txt")) {
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(KEYWORD)) {
                        String[] cols = line.split("[;\t]");
                        rows.add(cols);
                    }
                }
            }
        } else if (name.endsWith(".xlsx")) {
            try (Workbook wb = new XSSFWorkbook(Files.newInputStream(file))) {
                Sheet sheet = wb.getSheetAt(0);

                int descricaoCol = -1;
                Row header = sheet.getRow(0);
                if (header != null) {
                    for (Cell cell : header) {
                        if (matchesDescricao(cell.getStringCellValue())) {
                            descricaoCol = cell.getColumnIndex();
                            break;
                        }
                    }
                }
                if (descricaoCol == -1) return rows;

                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;
                    Cell cell = row.getCell(descricaoCol);
                    if (cell != null && cell.getCellType() == CellType.STRING &&
                        cell.getStringCellValue().contains(KEYWORD)) {

                        String[] cols = new String[row.getLastCellNum()];
                        for (int c = 0; c < row.getLastCellNum(); c++) {
                            Cell valueCell = row.getCell(c);
                            cols[c] = (valueCell != null ? valueCell.toString() : "");
                        }
                        rows.add(cols);
                    }
                }
            }
        }

        return rows;
    }

    private boolean matchesDescricao(String s) {
        if (s == null) return false;
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // remove acentos
        return normalized.toLowerCase().contains("descricao");
    }
}
