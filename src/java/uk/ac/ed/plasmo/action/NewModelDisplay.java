package uk.ac.ed.plasmo.action;

public class NewModelDisplay extends CreateNewModelOperations {

	private static final long serialVersionUID = 1L;
		
	public String execute() {	
		removeInterimModel();
		return SUCCESS;
	}
	
	/*public void removeInterimModel() {
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
	}*/

}
