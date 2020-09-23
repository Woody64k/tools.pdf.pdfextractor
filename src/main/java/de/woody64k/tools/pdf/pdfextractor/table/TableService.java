package de.woody64k.tools.pdf.pdfextractor.table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.pdfbox.pdmodel.PDDocument;

import de.woody64k.tools.pdf.pdfextractor.data.table.DataTable;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Rectangle;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.detectors.DetectionAlgorithm;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

public class TableService {

    public List<DataTable<String>> extractTables(File pdfFile, String password) {
	try (PDDocument pdfDocument = password == null ? PDDocument.load(pdfFile)
		: PDDocument.load(pdfFile, password)) {
	    List<Table> tables = findTables(pdfDocument);
	    return extractTables(tables);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    private List<Table> findTables(PDDocument pdfDocument) throws IOException, ParseException {
	TableExtractor tableExtractor = createExtractor(new CommandLine.Builder().build());
	PageIterator pageIterator = getPageIterator(pdfDocument);
	List<Table> tables = new ArrayList<>();
	while (pageIterator.hasNext()) {
	    Page page = pageIterator.next();
	    tables.addAll(tableExtractor.extractTables(page));
	}
	return tables;
    }

    private PageIterator getPageIterator(PDDocument pdfDocument) throws IOException {
	ObjectExtractor extractor = new ObjectExtractor(pdfDocument);
	return extractor.extract();
    }

    private List<DataTable<String>> extractTables(List<Table> tables) throws IOException {
	List<DataTable<String>> outTables = new ArrayList<>();
	for (Table table : tables) {
	    outTables.add(extractTable(table));
	}
	return outTables;
    }

    private DataTable<String> extractTable(Table table) throws IOException {
	DataTable<String> outTable = new DataTable<>();
	outTable.newHeadingRow();
	for (List<RectangularTextContainer> row : table.getRows()) {
	    List<String> cells = new ArrayList<>(row.size());
	    for (RectangularTextContainer<?> tc : row) {
		outTable.addCellToActualRow(tc.getText());
	    }
	    outTable.newRow();
	}
	return outTable;
    }

    private static TableExtractor createExtractor(CommandLine line) throws ParseException {
	TableExtractor extractor = new TableExtractor();
	extractor.setGuess(line.hasOption('g'));
	extractor.setUseLineReturns(line.hasOption('u'));
	return extractor;
    }

    private static class TableExtractor {
	private boolean guess = false;
	private boolean useLineReturns = false;
	private BasicExtractionAlgorithm basicExtractor = new BasicExtractionAlgorithm();
	private SpreadsheetExtractionAlgorithm spreadsheetExtractor = new SpreadsheetExtractionAlgorithm();

	public TableExtractor() {
	}

	public void setGuess(boolean guess) {
	    this.guess = guess;
	}

	public void setUseLineReturns(boolean useLineReturns) {
	    this.useLineReturns = useLineReturns;
	}

	public List<Table> extractTables(Page page) {
	    return extractTablesBasic(page);
	}

	public List<Table> extractTablesBasic(Page page) {
	    if (guess) {
		// guess the page areas to extract using a detection algorithm
		// currently we only have a detector that uses spreadsheets to find table areas
		DetectionAlgorithm detector = new NurminenDetectionAlgorithm();
		List<Rectangle> guesses = detector.detect(page);
		List<Table> tables = new ArrayList<>();

		for (Rectangle guessRect : guesses) {
		    Page guess = page.getArea(guessRect);
		    tables.addAll(basicExtractor.extract(guess));
		}
		return tables;
	    }

	    return basicExtractor.extract(page);
	}

	public List<Table> extractTablesSpreadsheet(Page page) {
	    // TODO add useLineReturns
	    return spreadsheetExtractor.extract(page);
	}
    }
}
