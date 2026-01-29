package com.jonascandido.ansdespesas.processor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DespesaFileProcessorTest {

    private final DespesaFileProcessor processor = new DespesaFileProcessor();

    @Test
    void deveDetectarCsvComDespesas() throws IOException {
        Path tempFile = Files.createTempFile("teste", ".csv");
        Files.writeString(tempFile,
                "CNPJ;RazaoSocial;Descricao;Valor\n" +
                "123;Empresa X;Despesas com Eventos/Sinistros;1000\n" +
                "456;Empresa Y;Outra Despesa;2000\n"
        );

        List<Path> filtered = processor.filterDespesaFiles(List.of(tempFile));
        assertThat(filtered).containsExactly(tempFile);

        List<String[]> rows = processor.normalizeFile(tempFile);
        assertThat(rows).hasSize(1);
        assertThat(rows.get(0)).contains("Despesas com Eventos/Sinistros");
    }

    @Test
    void deveIgnorarCsvSemDespesas() throws IOException {
        Path tempFile = Files.createTempFile("teste", ".csv");
        Files.writeString(tempFile,
                "CNPJ;RazaoSocial;Descricao;Valor\n" +
                "123;Empresa X;Outra Despesa;1000\n"
        );

        List<Path> filtered = processor.filterDespesaFiles(List.of(tempFile));
        assertThat(filtered).isEmpty();

        List<String[]> rows = processor.normalizeFile(tempFile);
        assertThat(rows).isEmpty();
    }

    @Test
    void deveDetectarXlsxComDespesas() throws IOException {
        Path tempFile = Files.createTempFile("teste", ".xlsx");
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Dados");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("CNPJ");
            header.createCell(1).setCellValue("RazaoSocial");
            header.createCell(2).setCellValue("Descrição");
            header.createCell(3).setCellValue("Valor");

            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("123");
            row1.createCell(1).setCellValue("Empresa X");
            row1.createCell(2).setCellValue("Despesas com Eventos/Sinistros");
            row1.createCell(3).setCellValue(1000);

            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("456");
            row2.createCell(1).setCellValue("Empresa Y");
            row2.createCell(2).setCellValue("Outra Despesa");
            row2.createCell(3).setCellValue(2000);

            try (var out = Files.newOutputStream(tempFile)) {
                wb.write(out);
            }
        }

        List<Path> filtered = processor.filterDespesaFiles(List.of(tempFile));
        assertThat(filtered).containsExactly(tempFile);

        List<String[]> rows = processor.normalizeFile(tempFile);
        assertThat(rows).hasSize(1);
        assertThat(rows.get(0)).contains("Despesas com Eventos/Sinistros");
    }

    @Test
    void deveIgnorarXlsxSemDespesas() throws IOException {
        Path tempFile = Files.createTempFile("teste", ".xlsx");
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Dados");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("CNPJ");
            header.createCell(1).setCellValue("RazaoSocial");
            header.createCell(2).setCellValue("Descrição");
            header.createCell(3).setCellValue("Valor");

            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("123");
            row.createCell(1).setCellValue("Empresa X");
            row.createCell(2).setCellValue("Outra Despesa");
            row.createCell(3).setCellValue(1000);

            try (var out = Files.newOutputStream(tempFile)) {
                wb.write(out);
            }
        }

        List<Path> filtered = processor.filterDespesaFiles(List.of(tempFile));
        assertThat(filtered).isEmpty();

        List<String[]> rows = processor.normalizeFile(tempFile);
        assertThat(rows).isEmpty();
    }

    @Test
    void deveDetectarTxtComDespesas() throws IOException {
        Path tempFile = Files.createTempFile("teste", ".txt");
        Files.writeString(tempFile,
                "CNPJ\tRazaoSocial\tDescricao\tValor\n" +
                "123\tEmpresa X\tDespesas com Eventos/Sinistros\t1000\n" +
                "456\tEmpresa Y\tOutra Despesa\t2000\n"
        );

        List<Path> filtered = processor.filterDespesaFiles(List.of(tempFile));
        assertThat(filtered).containsExactly(tempFile);

        List<String[]> rows = processor.normalizeFile(tempFile);
        assertThat(rows).hasSize(1);
        assertThat(rows.get(0)).contains("Despesas com Eventos/Sinistros");
    }

    @Test
    void deveIgnorarTxtSemDespesas() throws IOException {
        Path tempFile = Files.createTempFile("teste", ".txt");
        Files.writeString(tempFile,
                "CNPJ\tRazaoSocial\tDescricao\tValor\n" +
                "123\tEmpresa X\tOutra Despesa\t1000\n"
        );

        List<Path> filtered = processor.filterDespesaFiles(List.of(tempFile));
        assertThat(filtered).isEmpty();

        List<String[]> rows = processor.normalizeFile(tempFile);
        assertThat(rows).isEmpty();
    }
}
