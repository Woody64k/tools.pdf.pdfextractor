package de.woody64k.tools.pdf.pdfextractor.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.woody64k.tools.pdf.pdfextractor.data.table.DataTable;

public class ExtractedData {
    public final Map<String, String> propertyMap;
    public final List<DataTable<String>> tableData;

    public ExtractedData() {
	super();
	this.propertyMap = new HashMap<>();
	this.tableData = new ArrayList<>();
    }
}
