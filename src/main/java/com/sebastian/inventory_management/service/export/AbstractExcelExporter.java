package com.sebastian.inventory_management.service.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Template Method abstrato para exportação de dados para Excel.
 * Centraliza a lógica comum de criação de workbook, sheet, header e escrita.
 * 
 * @param <T> Tipo do DTO a ser exportado
 * 
 *            Padrão GoF: Template Method
 */
public abstract class AbstractExcelExporter<T> {

    /**
     * Template Method - define o algoritmo de exportação.
     */
    public final ByteArrayInputStream export(List<T> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(getSheetName());
            createHeaderRow(sheet);
            fillDataRows(sheet, data);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * Cria a linha de cabeçalho com os nomes das colunas.
     */
    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] columns = getColumnHeaders();
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
        }
    }

    /**
     * Preenche as linhas de dados.
     */
    private void fillDataRows(Sheet sheet, List<T> data) {
        int rowIdx = 1;
        for (T item : data) {
            Row row = sheet.createRow(rowIdx++);
            fillRow(row, item);
        }
    }

    // ==================== Abstract Methods (hooks) ====================

    /**
     * @return Nome da aba da planilha
     */
    protected abstract String getSheetName();

    /**
     * @return Array com os nomes das colunas do cabeçalho
     */
    protected abstract String[] getColumnHeaders();

    /**
     * Preenche uma linha com os dados do item.
     * 
     * @param row  Linha a ser preenchida
     * @param item Dados do item
     */
    protected abstract void fillRow(Row row, T item);
}
