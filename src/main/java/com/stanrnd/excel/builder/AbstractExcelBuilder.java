package com.stanrnd.excel.builder;

import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import com.stanrnd.excel.meta.ExcelColumn;
import com.stanrnd.excel.meta.ExcelSheet;
import com.stanrnd.excel.meta.ExcelTitle;

abstract public class AbstractExcelBuilder {
	
	protected void build(Workbook workbook, ExcelSheet excelSheet, List<?> rowDatas) {
		Sheet sheet = workbook.createSheet(excelSheet.getName());
		
		int rowNumber = -1;
		int columnNumber = -1;
		
		List<ExcelColumn> excelColumns = excelSheet.getColumns();
		
		ExcelTitle excelTitle = excelSheet.getTitle();
		Row titleRow = sheet.createRow(++rowNumber);
		titleRow.setHeight((short) excelTitle.getHeight());
		Cell titleCell = titleRow.createCell(++columnNumber);
		titleCell.setCellValue(excelTitle.getText());
		titleCell.setCellStyle(getTitleCellStyle(workbook));
		if(excelColumns.size() > 1) {
			CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, excelColumns.size()-1);
			sheet.addMergedRegion(cellRangeAddress);
		}
		
		columnNumber = -1;
		Row headerRow = sheet.createRow(++rowNumber);
		int maxHeaderHeight = 300;
		for (ExcelColumn excelColumn : excelColumns) {
			Cell cell = headerRow.createCell(++columnNumber);
			cell.setCellValue(excelColumn.getHeader().getText());
			cell.setCellStyle(getHeaderCellStyle(workbook));
			if(excelColumn.getWidth() == 0) {
				sheet.autoSizeColumn(columnNumber);
			} else {
				sheet.setColumnWidth(columnNumber, excelColumn.getWidth());
			}
			if(maxHeaderHeight < excelColumn.getHeader().getHeight()) {
				maxHeaderHeight = excelColumn.getHeader().getHeight();
			}
		}
		headerRow.setHeight((short) maxHeaderHeight);
		
		for(Object rowData:rowDatas) {
			int maxDataHeight = 300;
			Row row = sheet.createRow(++rowNumber);
			columnNumber = -1;
			for (ExcelColumn excelColumn : excelColumns) {
				Cell cell = row.createCell(++columnNumber);
				Object value = excelColumn.getValueParser().parse(rowData);
				if(value instanceof String) {
					cell.setCellValue((String) value);
				} else {
					cell.setCellValue(value.toString());
				}
				if(excelColumn.getWidth() == 0) {
					sheet.autoSizeColumn(columnNumber);
				} else {
					sheet.setColumnWidth(columnNumber, excelColumn.getWidth());
				}
				if(maxDataHeight < excelColumn.getHeader().getHeight()) {
					maxDataHeight = excelColumn.getHeader().getHeight();
				}
			}
			row.setHeight((short) maxDataHeight);
		}
		
		setBorder(sheet, rowDatas.size(), excelColumns.size());
	}
	
	private CellStyle getTitleCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		Font font= workbook.createFont();
		font.setFontHeightInPoints((short)11);
		font.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
	    font.setBold(true);
		style.setFont(font);
		return style;
	}
	
	private CellStyle getHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		Font font= workbook.createFont();
	    font.setBold(true);
	    font.setItalic(true);
		style.setFont(font);
		style.setFillPattern(FillPatternType.LEAST_DOTS);
		style.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		return style;
	}
	
	private void setBorder(Sheet sheet, int rows, int cols) {
		CellRangeAddress bottonBorderRange = new CellRangeAddress(1, 1, 0, cols-1);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, bottonBorderRange, sheet);
		
		CellRangeAddress borderRange = new CellRangeAddress(1, rows+1, 0, cols-1);
		RegionUtil.setBorderTop(BorderStyle.MEDIUM, borderRange, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM, borderRange, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM, borderRange, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM, borderRange, sheet);
	}

}