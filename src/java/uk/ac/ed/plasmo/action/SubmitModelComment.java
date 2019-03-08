package uk.ac.ed.plasmo.action;

import java.util.Map;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.entity.ModelData;
import uk.ac.ed.plasmo.entity.User;

import com.josephoconnell.html.HTMLInputFilter;
import com.opensymphony.xwork2.ActionContext;

/**
 * <p>Action class that interacts with the data access layer in order to
 * submit user comments about a specific model to the database</p>
 * <p>Comments can only be submitted if the user if logged in to the application</p> 
 * @author ctindal
 *
 */
public class SubmitModelComment extends DisplayModelOperations {

	private static final long serialVersionUID = 1L;
	
	private String comment;
	private String ajxInput;
	private ModelData modelData;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public ModelData getModelData() {
		return modelData;
	}
	public void setModelData(ModelData modelData) {
		this.modelData = modelData;
	}
	public String getAjxInput() {
		return ajxInput;
	}
	public void setAjxInput(String ajxInput) {
		this.ajxInput = ajxInput;
	}
	
	public String execute() {
		
		String result = this.checkAuthorisation();
		if(!result.equals(SUCCESS)) {
			return result;
		}
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		User user = (User) session.get("user");
		
		//no model or user in session means can't submit comment
		if(user == null){
			return INPUT;
		}
		
		if(comment != null) {
			comment = new HTMLInputFilter().filter(comment);
		}
		
		ModelAssembler assembler = new ModelAssembler();
		assembler.addModelComment(getAccession(), comment, user);
		
		if(ajxInput != null) {
			modelData = new ModelData();
			modelData.setComments(assembler.getModelComments(getAccession()));
			return "ajax";
		}
		
		return SUCCESS;
	}

}
