package uk.ac.ed.plasmo.action;

public class NewModelVersionDisplay extends CreateNewVersionDisplay {

	/*private static final long serialVersionUID = 1L;
	
	private Boolean authorised;
	
	public boolean isAuthorised() {
		return authorised;
	}
	public void setAuthorised(boolean authorised) {
		this.authorised = authorised;
	}
	
	public String execute() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("user");
		
		//no model or user in session means error in prior submission stage 
		if(user == null || getAccession() == null || getAccession().trim().equals("")){
			return INPUT;
		} 
		else {
			//check the user is authorised to modify the model
			SecurityAssembler secAssembler = new SecurityAssembler();
			authorised = secAssembler.isAuthorisedToEditModel(user, getAccession());
		}
		
		//if authorised is null, no model was found so redirect to appropriate page
		if(authorised == null) {
			return INPUT;
		}
		else {
			//if authorised, get the model data to display
			if(authorised) {
				this.setVersionDisplayData();
				//if no data was found, redirect to 'not found' page
				if(getModelData() == null) {
					return INPUT;
				}
			}
		}
		return SUCCESS;
	}*/
}
