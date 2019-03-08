package uk.ac.ed.plasmo.action;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;

import uk.ac.ed.plasmo.assembler.UserModelsAssembler;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.table.Table;
import uk.ac.ed.plasmo.utility.table.TableAssembler;
/**
 * Action class involved in displaying info about a specific user. 
 * NB: not currently in use as needs further development
 * @author ctindal
 *
 */
public class DisplayUserInfo extends TableView {
	
	private static final long serialVersionUID = 1L;
	
	private static final String RPP_SESSION_KEY = "USER_MODELS_RPP";
	
	public String execute() {
		
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
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("user");
		
		TableAssembler assembler = new UserModelsAssembler(user);
		
		Table table = new Table(assembler,null);
		
		setTable(table);
		
		setValues();
	}

}
