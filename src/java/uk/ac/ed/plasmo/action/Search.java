package uk.ac.ed.plasmo.action;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ed.plasmo.assembler.SearchModelsAssembler;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.Constants;
import uk.ac.ed.plasmo.utility.table.HeaderItem;
import uk.ac.ed.plasmo.utility.table.Table;
import uk.ac.ed.plasmo.utility.table.TableAssembler;

import com.opensymphony.xwork2.ActionContext;
/**
 * <p>Action class that interacts with the data access layer in 
 * order to retrieve summary data about models in the database whose 
 * attributes match a user-defined search term</p>
 * <p>If the user is logged in they will also be able to see a 
 * summary of relevant  models flagged as private provided they 
 * have sufficient privileges.</p> 
 * @author ctindal
 *
 */
public class Search extends TableView {
	
	private static final long serialVersionUID = -3599643644384784848L;

	private static final String RPP_SESSION_KEY = "SEARCH_MODELS_RPP";
	
	
	private String query;
	private String type;
	//private static final String [] TYPE_OPTIONS = {"formats", "models", "users"};
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/*public static String[] getTypeOptions() {
		return TYPE_OPTIONS;
	}*/
	
	public String getModels() {
		
                //System.out.println("GM called");
		try {
			Map<String, Object> session = ActionContext.getContext().getSession();
			String rppStr = (String) session.get(RPP_SESSION_KEY);
			setRpp(Integer.parseInt(rppStr));
		}
		catch(NumberFormatException e) {
			setRpp(DEFAULT_ROWS_PER_PAGE);
		}
		
		retrieveData();

		return SUCCESS;
	}
	
	public String setResPerPageValue() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		//check the supplied value is valid
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
		
		//if the supplied value is valid
		if(validOption) {
			session.put(RPP_SESSION_KEY, String.valueOf(getRpp()));
		}
		//otherwise
		else {
			String rppString = (String) session.get(RPP_SESSION_KEY);
			
			//check there is a value already in session and use that
			if(rppString != null) {
				setRpp(Integer.parseInt(rppString));
			}
			//use the default value
			else {
				setRpp(DEFAULT_ROWS_PER_PAGE);
				session.put(RPP_SESSION_KEY, String.valueOf(DEFAULT_ROWS_PER_PAGE));
			}
		}
		
		retrieveData();
		
		return SUCCESS;
	}
	
	public void retrieveData() {
		
            //System.out.println("RetrieveData");
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("user");
		
		Map<String, Object> qParams = new HashMap<String, Object>();
		qParams.put("query", query);
		qParams.put("type", type);
		qParams.put("user", user);
		
		TableAssembler assembler = new SearchModelsAssembler(qParams);
		
		Table table = new Table(assembler, null);
		
		setTable(table);
		
		setValues();
	}
	
	public static HeaderItem [] createHeadersForBrowseModels() {
		String [] headerTitles = Constants.getBrowseModelsHeaderTitles();
		
		boolean[] headerSortable = null;

	     headerSortable = new boolean[]{false, false, false, false, false};
	     
	     int colNum = headerTitles.length;
	     
	     HeaderItem[] tableHeader = new HeaderItem[colNum];
		 for(int i=0; i<colNum; i++)
			 tableHeader[i] = new HeaderItem(headerTitles[i], headerSortable[i]);
		 
 		 return tableHeader;
		
	}

}
