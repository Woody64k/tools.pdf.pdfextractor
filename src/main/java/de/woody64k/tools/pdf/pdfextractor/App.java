package de.woody64k.tools.pdf.pdfextractor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws URISyntaxException, IOException {
		final File workingDir = new File("C:\\temp\\pdf\\");
		List<File> pdfFileList = Arrays.asList(workingDir.listFiles(a -> a.getName().endsWith(".pdf")));

		pdfFileList.forEach(pdfFile -> {
			LOG.info("Print Load Document");
			try {
				Map<String, String> propertyMap = getFieldsFromPdfForm(pdfFile);
				System.out.println(propertyMap);
			} catch (IOException ioEx) {
				LOG.error("Main-Error: ", ioEx);
			}
		});
	}

	private static Map<String, String> getFieldsFromPdfForm(File pdfFile) throws InvalidPasswordException, IOException {
		Map<String, String> propertyMap = new HashMap<>();
		PDDocument pdDoc = PDDocument.load(pdfFile);
		PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
		PDAcroForm pdAcroForm = pdCatalog.getAcroForm();

		for (PDField pdField : pdAcroForm.getFields()) {
			propertyMap.put(pdField.getFullyQualifiedName(), pdField.getValueAsString());
		}
		return propertyMap;
	}
}
