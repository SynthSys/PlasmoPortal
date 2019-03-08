package uk.ac.ed.plasmo.action;

import java.util.Map;

import uk.ac.ed.plasmo.assembler.SecurityAssembler;
import uk.ac.ed.plasmo.entity.User;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class DisplayModelOperations extends ActionSupport {

	private static final long serialVersionUID = 1L;
	protected static final String NO_MODEL = "no_model";
	
	private String accession;
	private Boolean authorised;
	
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
	}
	
	public String checkAuthorisation() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("user");
		
		if(accession == null || accession.trim().equals("")){
			return NO_MODEL;
		}
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		authorised = secAssembler.isAuthorisedToAccessModel(user, accession);
		
		//if authorised is null or false, the model is private
		if(authorised == null || !authorised) {
			return NO_MODEL;
		}
		else {
			return SUCCESS;
		}
	}
	
	public User getUser() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("user");
		
		return user;
	}

}
