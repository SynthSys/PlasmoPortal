package uk.ac.ed.plasmo.utility.table;

import java.util.ArrayList;
import java.util.Map;
/**
 * abstract class used to initialise data access object and call methods used
 * to build tables of generic data set. Used, for example, when browsing through
 * lists/tables of similar data objects
 * @author ctindal
 *
 */
public abstract class TableAssembler {
	
	private Map<String, Object> params;
	
	public TableAssembler() {
		params = null;
	}
	
	public TableAssembler(Map<String, Object> params) {
		setDataRetrievalParams(params);
	}
	
	public void setDataRetrievalParams(Map<String, Object> params) {
		if (params!=null) { 
			this.params = params;
			setParams();
		}
		else {
			resetDataRetrivalParams();
		}
	}
	
	/**
	 * clears the Map of parameters used to build the query
	 */
	public void resetDataRetrivalParams() {
		params.clear();
	}
	
	/**
	 * sets the value of local parameters on a per assembler basis
	 */
	public void setParams(){}
	
	
	
	/**
	 * sets up the DAO to retrieve the total number of rows retrieved from {@link retrieveData}. 
	 * @return the total number of rows in the data set
	 */
	public abstract int retrieveNumberOfRows();
	
	/**
	 * sets up the DAO to retrieve the meta data for the set of models currently being browsed
	 * @param column what to sort the retrieved data on
	 * @param ascending sort ascending or descending
	 * @param offset where to start the retrieval from
	 * @param num the number of models to retrieve starting from the <b>offset</b> value
	 * @return
	 */
	public abstract ArrayList<String []> retrieveData(int column, boolean ascending, int offset, int num);
	
	/**
	 * sets up the DAO to retrieve the column totals for each of the meta data columns retrieved from the
	 * retrieveData method
	 * @return
	 */
	public int[] retrieveTotals() { // This should be overridden if functionality is required
		return null;
	}
	
	public Object getParam(Object key) {
		return params.get(key);
	}
	
	public Object[] getParams(Object key) {
		return (Object[]) params.get(key);
	}
	
}
