package uk.ac.ed.plasmo.action;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import uk.ac.ed.plasmo.assembler.BrowseModelsAssembler;
import uk.ac.ed.plasmo.db.IdGenerator;
import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.MySQLNewItemDAOImpl;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.Constants;
import uk.ac.ed.plasmo.utility.table.HeaderItem;
import uk.ac.ed.plasmo.utility.table.Table;
import uk.ac.ed.plasmo.utility.table.TableAssembler;
/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve summary data about all the models in the database</p>
 * <p>If the user is logged in they will also be able to see a summary of models flagged as private
 * provided they have sufficient privileges.</p> 
 * @author ctindal
 *
 */
public class DisplayModels extends TableView {
	
	private static final long serialVersionUID = 1L;
	
	private static final String RPP_SESSION_KEY = "BROWSE_MODELS_RPP";
	
	public String execute() {
		
		try {
			Map<String, Object> session = ActionContext.getContext().getSession();
			String rppStr = (String) session.get(RPP_SESSION_KEY);
			setRpp(Integer.parseInt(rppStr));
		}
		catch(NumberFormatException e) {
			setRpp(DEFAULT_ROWS_PER_PAGE);
		}
		
                /*System.out.println("NEXT_ID: "+ IdGenerator.getInstance().getNextModelAccession());
                List<String> names = Arrays.asList("X","Y","XY","Description","Tested");
                MySQLNewItemDAOImpl dap = new MySQLNewItemDAOImpl();
                try {
                    dap.addAttributesNames(names);
                } catch (SQLException e) {
                    System.out.println("Something wrong: "+e.getMessage());
                }*/
                
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
		
            //System.out.println("Retrieve data all models");
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("user");
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		
		TableAssembler assembler = new BrowseModelsAssembler(params);
		
		Table table = new Table(assembler, createHeadersForBrowseModels());
		
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
