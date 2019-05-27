package de.woody64k.tools.pdf.pdfextractor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws URISyntaxException, IOException {
		final String address = "D:\\temp\\pdf\\PDF-Formular-Beispiel.pdf";
		LOG.info("Print Load Document");
		try {
			// URL scalaByExampleUrl = new URL(address);
			// PDDocument pdDoc = PDDocument.load(scalaByExampleUrl.openStream());
			File pdfFile = new File(address);
			PDDocument pdDoc = PDDocument.load(pdfFile);
			PDDocumentCatalog pdCatalog = pdDoc.getDocumentCatalog();
			PDAcroForm pdAcroForm = pdCatalog.getAcroForm();

			for(PDField pdField : pdAcroForm.getFields()){
				pdField.getFullyQualifiedName();
				pdField.getValueAsString();
			}
		} catch (IOException ioEx) {
			LOG.error("Main-Error: ", ioEx);
		}
	}
}
