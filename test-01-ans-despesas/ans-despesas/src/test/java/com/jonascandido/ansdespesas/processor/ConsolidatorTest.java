package com.jonascandido.ansdespesas.processor;

import com.jonascandido.ansdespesas.client.OperadoraApiClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsolidatorTest {

    @TempDir
    Path tempDir;

    private OperadoraApiClient apiClient;
    private Consolidator consolidator;

    @BeforeEach
    void setup() throws IOException {
        apiClient = Mockito.mock(OperadoraApiClient.class);

        when(apiClient.getCnpj("123"))
                .thenReturn("11111111000100");
        when(apiClient.getRazaoSocial("123"))
                .thenReturn("OPERADORA TESTE");

        // força pasta temp local (como no projeto real)
        Files.createDirectories(Paths.get("temp"));

        consolidator = new Consolidator(apiClient);
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.walk(Paths.get("temp"))
                .filter(Files::isRegularFile)
                .forEach(p -> p.toFile().delete());
    }

    @Test
    void deveConsolidarCsvGerandoZipOrdenado() throws Exception {
        Path csv = criarCsvValido();

        Path zip = consolidator.consolidate(List.of(csv));

        assertTrue(Files.exists(zip), "ZIP consolidado deve existir");

        Path csvConsolidado = Paths.get("temp", "consolidado_despesas.csv");
        assertTrue(Files.exists(csvConsolidado));

        List<String> linhas = Files.readAllLines(csvConsolidado);

        assertEquals(2, linhas.size());
        assertTrue(linhas.get(1).contains("11111111000100"));
        assertTrue(linhas.get(1).contains("OPERADORA TESTE"));
        assertTrue(linhas.get(1).contains("1"));
        assertTrue(linhas.get(1).contains("2025"));
    }

    @Test
    void deveIgnorarLinhaComDataInvalida() throws Exception {
        Path csv = criarCsvComMesInvalido();

        Path zip = consolidator.consolidate(List.of(csv));

        Path csvConsolidado = Paths.get("temp", "consolidado_despesas.csv");
        List<String> linhas = Files.readAllLines(csvConsolidado);

        assertEquals(1, linhas.size());
    }

    @Test
    void deveLogarValorNegativoMasManterNoCsv() throws Exception {
        Path csv = criarCsvComValorNegativo();

        Path zip = consolidator.consolidate(List.of(csv));

        Path csvConsolidado = Paths.get("temp", "consolidado_despesas.csv");
        List<String> linhas = Files.readAllLines(csvConsolidado);

        assertEquals(2, linhas.size());
        assertTrue(linhas.get(1).contains("-"));
    }

    @Test
    void deveUsarCacheEvitarMultiplasChamadasApi() throws Exception {
        Path csv = criarCsvValido();

        consolidator.consolidate(List.of(csv));
        consolidator.consolidate(List.of(csv));

        verify(apiClient, times(1)).getCnpj("123");
        verify(apiClient, times(1)).getRazaoSocial("123");
    }

    // Helpers
    private Path criarCsvValido() throws IOException {
        Path file = tempDir.resolve("despesas.csv");

        Files.write(file, List.of(
                "DATA;REG_ANS;X;Y;SALDO_INI;SALDO_FIM",
                "2025-01;123;;;100,00;300,00"
        ));

        return file;
    }

    private Path criarCsvComMesInvalido() throws IOException {
        Path file = tempDir.resolve("despesas_mes_invalido.csv");

        Files.write(file, List.of(
                "DATA;REG_ANS;X;Y;SALDO_INI;SALDO_FIM",
                "2025-15;123;;;100,00;300,00"
        ));

        return file;
    }

    private Path criarCsvComValorNegativo() throws IOException {
        Path file = tempDir.resolve("despesas_negativo.csv");

        Files.write(file, List.of(
                "DATA;REG_ANS;X;Y;SALDO_INI;SALDO_FIM",
                "2025-02;123;;;500,00;100,00"
        ));

        return file;
    }
}
