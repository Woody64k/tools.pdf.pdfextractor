package de.woody64k.tools.pdf.pdfextractor.data.table;

import java.util.ArrayList;
import java.util.List;

public class DataTable<T> {
    public DataRow<T> heading = new DataRow<>();
    public List<DataRow<T>> rows = new ArrayList<>();
    public DataRow<T> actualRow;

    public DataRow<T> newHeadingRow() {
	actualRow = new DataRow<>();
	heading = actualRow;
	return actualRow;
    }

    public DataRow<T> newRow() {
	actualRow = new DataRow<>();
	rows.add(actualRow);
	return actualRow;
    }

    public void addCellToActualRow(T value) {
	actualRow.newCell(value);
    }

    public static class DataRow<T> {
	public List<DataCell<T>> cells = new ArrayList<>();

	public DataCell<T> newCell(T value) {
	    DataCell<T> actualCell = new DataCell<>();
	    actualCell.value = value;
	    cells.add(actualCell);
	    return actualCell;
	}
    }

    public static class DataCell<T> {
	public T value;
    }
}
