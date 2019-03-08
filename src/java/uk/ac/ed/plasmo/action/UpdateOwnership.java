package uk.ac.ed.plasmo.action;

import java.util.Map;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.assembler.SecurityAssembler;
import uk.ac.ed.plasmo.entity.User;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action class responsible for processing data supplied via the application
 * front end to change the ownership of a specific model in the database
 * @author ctindal
 *
 */
public class UpdateOwnership extends ActionSupport {
	
	private static final long serialVersionUID = -8105758925897148351L;
	
	private String ownerUserName;
	private String accession;
	
	public String getOwnerUserName() {
		return ownerUserName;
	}
	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
	}

	public String execute() {
		
		if(hasPermission() != null && hasPermission()) {

			ModelAssembler assembler = new ModelAssembler();
			Map<String, Object> session = ActionContext.getContext().getSession();
			User supervisor = (User) session.get("user");

			User newOwner = new User();
			newOwner.setUserName(ownerUserName);

			String result = assembler.updateModelOwnership(supervisor, newOwner, accession);

			if(result == null) {
				return SUCCESS;
			}
			//else {
				//addActionError(result);
				//return INPUT;
			//}
		}
		if(hasPermission() == null) {
			return "no_model";
		}
		
		addActionError("unauthorised");
		return "unauthorised";
	}
	
	protected Boolean hasPermission() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("user");
		
		if(accession == null || accession.trim().equals("")){
			return null;
		}
		
		if(user == null){
			return false;
		}
		else {
			SecurityAssembler secAssembler = new SecurityAssembler();
			Boolean authorised = secAssembler.isAuthorisedToChangeModelOwnership(user, accession);
			return authorised;
		}
	}

}
