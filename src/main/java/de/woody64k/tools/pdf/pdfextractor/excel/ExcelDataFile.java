package de.woody64k.tools.pdf.pdfextractor.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelDataFile {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDataFile.class);

	private int nextRowNum = 1;
	private final List<String> header = new ArrayList<>();
	private final XSSFWorkbook workbook = new XSSFWorkbook();
	private XSSFSheet activeSheet = workbook.createSheet("Datenimport aus PDF");

	public ExcelDataFile() {
		super();
	}

	public void addEntity(Map<String, String> propertyMap) {
		Row row = activeSheet.createRow(nextRowNum++);
		List<String> actualHeaders = appendHeaders(propertyMap.keySet());
		int colNum = 0;
		for (String header : actualHeaders) {
			Cell cell = row.createCell(colNum++);
			cell.setCellValue(propertyMap.get(header));
		}
	}

	private List<String> appendHeaders(Set<String> keySet) {
		Set<String> newHeaderList = new LinkedHashSet<>(keySet);
		newHeaderList.removeAll(header);
		header.addAll(newHeaderList);
		return header;
	}

	public void saveExcel(File outFile) {
		try {
			writeHeader();
			FileOutputStream outputStream = new FileOutputStream(outFile);
			workbook.write(outputStream);
			workbook.close();
			LOGGER.info("Excel wurde gespeichert: " + outFile.getPath());
		} catch (IOException ioEx) {
			LOGGER.error("Fehler beim Schreben der Datei: " + outFile.getPath(), ioEx);
			throw new RuntimeException(ioEx);
		}
	}

	private void writeHeader() {
		Row row = activeSheet.createRow(0);
		int colNum = 0;
		for (String newHeader : header) {
			Cell cell = row.createCell(colNum++);
			cell.setCellValue(newHeader);
		}
	}
}
