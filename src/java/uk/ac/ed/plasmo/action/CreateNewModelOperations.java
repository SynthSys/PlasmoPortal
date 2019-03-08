package uk.ac.ed.plasmo.action;

import java.util.Map;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.entity.ModelSubmission;

import com.opensymphony.xwork2.ActionContext;

public class CreateNewModelOperations extends ModelOperations {
	
	private static final long serialVersionUID = 1L;
	
	public void removeInterimModel() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		//if the user was midway through submitting a model (which is stored in session) 
		//and decided to start again, and the submission has to be removed from session 
		// and deleted from the interim table in the db
		ModelSubmission submission = (ModelSubmission) session.get("modelSubmission");
		if(submission != null) {
			ModelAssembler assembler = new ModelAssembler();
			String tempId = submission.getTempId();
			if(tempId != null && !tempId.trim().equals("")) {
				assembler.deleteInterimModel(tempId);
			}
			session.remove("modelSubmission");
		}
	}

}
