package uk.ac.ed.plasmo.action;

import java.util.Map;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.entity.User;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ModifyModelGroupPermission extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
public String execute() {
		
//		if(hasPermission() != null && hasPermission()) {
//
//			ModelAssembler assembler = new ModelAssembler();
//			Map<String, Object> session = ActionContext.getContext().getSession();
//			User supervisor = (User) session.get("user");
//
//			User newOwner = new User();
//			newOwner.setUserName(ownerUserName);
//
//			String result = assembler.updateModelOwnership(supervisor, newOwner, accession);
//
//			if(result == null) {
//				return SUCCESS;
//			}
//			//else {
//				//addActionError(result);
//				//return INPUT;
//			//}
//		}
//		if(hasPermission() == null) {
//			return "no_model";
//		}
//		
//		addActionError("unauthorised");
//		return "unauthorised";
	
		return SUCCESS;
	}

}
