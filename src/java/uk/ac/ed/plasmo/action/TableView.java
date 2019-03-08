package uk.ac.ed.plasmo.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import uk.ac.ed.plasmo.utility.table.Table;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * <p>Utility action class use to provide formatting and views of tabular data</p>
 * @author ctindal
 *
 */
public class TableView extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_ROWS_PER_PAGE = 20;
	
	public static final int FIRST_PAGE = 1;
	public static final int [] DEFAULT_PER_PAGE_OPTIONS = {5,10,20,50,100};
	
	private int pgNo; //the current page to display
	private int rpp; //rows per page
	private int noPgs; //number of pages
	private Table table;
	
        String msg;
	
	private int [] perPageOptions;
	private String noDataMessage;
	
	public void setValues() {
		
		//if there are models in the database
		if(table.getNumRows() > 0) {
		
		perPageOptions = DEFAULT_PER_PAGE_OPTIONS;
		adjustPerPageOptions();
		
		if(perPageOptions.length > 0 && perPageOptions[perPageOptions.length-1] < rpp) {
			rpp = perPageOptions[perPageOptions.length-1];
		}
		
		noPgs = Math.max((int)Math.ceil((double)table.getNumRows()/rpp), 1);
		
		if(pgNo < 1) {
			pgNo = FIRST_PAGE;
		}
		pgNo = Math.min(noPgs, pgNo);
		
		
		
		table.setTableData(getRowOffset(), rpp*pgNo);
		
		}
		else {
			noDataMessage = "There is no data to display";
		}
		
	}
	
	private void adjustPerPageOptions() {
		
		Arrays.sort(perPageOptions);
		int n = table.getNumRows();
		ArrayList<Integer> tmpList = new ArrayList<Integer>();
		 
		for(int i=0;i<perPageOptions.length;i++) {
			if(i == 0) {
				if(n > perPageOptions[i]) {
					tmpList.add(perPageOptions[i]);
				}
			}
			else if(n > perPageOptions[i-1]) {
				tmpList.add(perPageOptions[i]);
			}
		}
		
		perPageOptions = new int [tmpList.size()];
		for(int i=0;i<tmpList.size();i++) {
			perPageOptions[i] = tmpList.get(i);
		}		
		
		/*Arrays.sort(perPageOptions);
		int n = table.getNumRows();
		ArrayList<Integer> tmpList = new ArrayList<Integer>();
		for(int i=0;i<perPageOptions.length;i++) {
			
			if(i == 0) {
				tmpList.add(perPageOptions[i]);
			}
			else if(perPageOptions[i] <= n) {
				tmpList.add(perPageOptions[i]);
			}
		}
		
		perPageOptions = new int [tmpList.size()];
		for(int i=0;i<tmpList.size();i++) {
			perPageOptions[i] = tmpList.get(i);
		}*/
		
		
		/*ArrayList<Integer> l = new ArrayList<Integer>();
		boolean setValueAdded = false;
		for (int i=0; i<perPageOptions.length; i++) {
			if (perPageOptions[i] <= rpp) {
				l.add(perPageOptions[i]);
				if (perPageOptions[i] == rpp)  
					setValueAdded = true;
			}
			else {
				if (!setValueAdded) {
					l.add(rpp);
					setValueAdded = true;
				}
				l.add(perPageOptions[i]);
				if (perPageOptions[i] > n)
					break;
			}
		}
		if (!setValueAdded)
			l.add(rpp);
		perPageOptions = new int[l.size()];
		for (int i=0; i<l.size(); i++)
			perPageOptions[i] = (Integer)l.get(i);*/
	}
	
	public void setPerPageSessionValue(String key) {
		Map<String, Object> session = ActionContext.getContext().getSession();
		boolean validOption = false;
		int index = 0;
		while(index < DEFAULT_PER_PAGE_OPTIONS.length && !validOption) {
			if(getRpp() == DEFAULT_PER_PAGE_OPTIONS[index]) {
				validOption = true;
			}
			else {
				index++;
			}
		}
		
		if(validOption) {
			session.put(key, String.valueOf(getRpp()));
		}
		else {
			session.put(key, String.valueOf(DEFAULT_ROWS_PER_PAGE));
		}
	}
	
	public int getPgNo() {
		return pgNo;
	}
	public void setPgNo(int pgNo) {
		this.pgNo = pgNo;
	}
	public int getRpp() {
		return rpp;
	}
	public void setRpp(int rpp) {
		this.rpp = rpp;
	}
	public int getNoPgs() {
		noPgs = Math.max((int)Math.ceil((double)table.getNumRows()/rpp), 1);
		return noPgs;
	}
	public void setNoPgs(int noPgs) {
		this.noPgs = noPgs;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	
	public int getRowOffset(){
		return rpp*(pgNo-1) + 1;
	}
	public int[] getPerPageOptions() {
		return perPageOptions;
	}

	public void setPerPageOptions(int[] perPageOptions) {
		this.perPageOptions = perPageOptions;
	}
	public String getNoDataMessage() {
		return noDataMessage;
	}
	public void setNoDataMessage(String noDataMessage) {
		this.noDataMessage = noDataMessage;
	}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
