package uk.ac.ed.plasmo.utility.table;

import java.util.ArrayList;

/**
 * The {@code Table} class represents tablular data. Data is retrieved 
 * from a storage resource by referring to abstract methods {@code retrieveData} 
 * and {@code getTotalRows} of the {@code TableAssembler} class.
 * @author ctindal
 *
 */
public class Table {
	
	private HeaderItem[] headers;
	private ArrayList<String []> data;
	private int numCols;
	private int numRows;
	private TableAssembler assembler;
	
	public Table(TableAssembler assembler, HeaderItem [] headers) {
		this.assembler = assembler;
		this.headers = headers;
		refreshTable();
	}
	
	public void refreshTable() {
		if(assembler == null) {
			numRows = 0;
		}
		else {
			numRows = assembler.retrieveNumberOfRows();
		}
		setTotals(retrieveTotals());
	}
	
	public void setTotals(int[] totals) {
		if(totals != null && headers != null) {
			for(int i=0;i<headers.length;i++) {
				headers[i].setTotal(totals[i]);
			}
		}
	}
	
	// This should be overriden if Totals are required
	public int[] retrieveTotals() {
		int[] totals = null;
		if (headers != null){
			totals = (assembler).retrieveTotals(); 
			if (totals != null) 
				setTotals(totals);
			else {
				totals = new int[headers.length];
				for (int i=0; i<headers.length; i++)
					totals[i] = 0;
			}
		}
		return totals;
	}
	
	public HeaderItem[] getHeaders() {
		return headers;
	}

	public void setHeaders(HeaderItem[] headers) {
		this.headers = headers;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}
	
	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
	
	public void setTableData(int offset, int rowNum) {
		data = assembler.retrieveData(-1, true, offset, rowNum);
	}
	
	public ArrayList<String []> getData() {
		return data;
	}
}
