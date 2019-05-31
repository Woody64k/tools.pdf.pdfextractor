package de.woody64k.tools.pdf.pdfextractor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.woody64k.tools.pdf.pdfextractor.excel.ExcelDataFile;

public class App {
	private static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws URISyntaxException, IOException {
		try {
			String workingDirPath = (args.length == 0) ? "C:\\temp\\pdf\\" : args[0] + '\\';
			final File workingDir = new File(workingDirPath);
			List<File> pdfFileList = Arrays.asList(workingDir.listFiles(a -> a.getName().endsWith(".pdf")));
			LOG.info("Print Load Document");

			ExcelDataFile excelFile = new ExcelDataFile();
			pdfFileList.forEach(pdfFile -> {
				Map<String, String> propertyMap = getFieldsFromPdfForm(pdfFile);
				excelFile.addEntity(propertyMap);
			});
			File outfile = new File(workingDirPath + "output.xlsx");
			outfile.createNewFile();
			excelFile.saveExcel(outfile);
		} catch (Throwable e) {
			LOG.error("Ein Fehler ist Aufgetreten", e);
			System.exit(1);// Fehler
		}
		System.exit(0);// kein Fehler
	}

	private static Map<String, String> getFieldsFromPdfForm(File pdfFile) {
		try (PDDocument pdDoc = PDDocument.load(pdfFile);) {
			Map<String, String> propertyMap = new LinkedHashMap<>();
			PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
			PDAcroForm pdAcroForm = pdCatalog.getAcroForm();
			LOG.info("Lese PDF: " + pdfFile.getName());
			for (PDField pdField : pdAcroForm.getFields()) {
				propertyMap.put(pdField.getFullyQualifiedName(), pdField.getValueAsString());
			}
			return propertyMap;
		} catch (IOException ioEx) {
			LOG.error("Main-Error: ", ioEx);
			throw new RuntimeException(ioEx);
		}
	}
}
