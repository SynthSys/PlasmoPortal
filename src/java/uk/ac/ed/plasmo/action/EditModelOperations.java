package uk.ac.ed.plasmo.action;

import java.util.Map;

import uk.ac.ed.plasmo.assembler.SecurityAssembler;
import uk.ac.ed.plasmo.entity.User;

import com.opensymphony.xwork2.ActionContext;

public class EditModelOperations extends ModelOperations {
	
	private static final long serialVersionUID = 1L;
	
	protected static final String NO_MODEL = "no_model";
	protected static final String UNAUTHORISED = "unauthorised";
	
        protected User user;
	private Boolean authorised;
	private String accession;
	
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
	}
	
	public String checkAuthorisation() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		user = (User) session.get("user");
		
		if(accession == null || accession.trim().equals("")){
			return NO_MODEL;
		} 
		
		if(user == null){
			addActionError(UNAUTHORISED);
			return UNAUTHORISED;
		}
		else {
			//check the user is authorised to modify the model
			SecurityAssembler secAssembler = new SecurityAssembler();
			authorised = secAssembler.isAuthorisedToEditModel(user, getAccession());
			
			//if authorised is null, no model was found so redirect to appropriate page
			if(authorised == null) {
				return NO_MODEL;
			}
			else {
				//if authorised, get the model data to display
				if(authorised) {
					//this.setVersionDisplayData();
					return SUCCESS;
					
				} 
				else {
					addActionError(UNAUTHORISED);
					return UNAUTHORISED;
				}
			}
		}

	}

}
