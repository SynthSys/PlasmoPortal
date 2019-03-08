package uk.ac.ed.plasmo.action;

import java.util.List;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.db.DataConstants;
import uk.ac.ed.plasmo.entity.Attribute;
import uk.ac.ed.plasmo.entity.ModelSubmission;
/**
 * Struts2 action class responsible for submitting specific model meta data edits
 * to the backend model database 
 * @author ctindal
 *
 */
public class EditModel extends EditModelDisplay {

	private static final long serialVersionUID = 1L;
	
        @Override
	public String execute() {
		String result = this.checkAuthorisation();
		if(!result.equals(SUCCESS)) {
			return result;
		}
		
		ModelSubmission submission = this.getSubmission();
		
		if(submission == null) {
			addActionError("An unexpected error occurred. Please try again.");
		}
		else {
			if(submission.getAttributes() != null && submission.getAttributes().size() > 0) {

				List<Attribute> attributes = submission.getAttributes();
				for(int i=0;i<attributes.size();i++){
					Attribute att = attributes.get(i);
					if(att != null) {
						if((att.getName() == null || att.getName().trim().equals("")) && (att.getValue() == null || att.getValue().trim().equals(""))) {
							attributes.remove(i);
							i--;
						}
						else {
							if(att.getName() == null || att.getName().trim().equals("")) {
								addFieldError("submission.attributes["+i+"].name","Please enter an appropriate name to identify the attribute value you have entered or remove this attribute entry from the submission.");
							}
							else {
								if(att.getName().length() > DataConstants.ATTRIBUTE_NAME) {
									addFieldError("submission.attributes["+i+"].name","Attribute name exceeds the maximum character length of "+DataConstants.ATTRIBUTE_NAME);
								}
							}
							if(att.getValue() == null || att.getValue().trim().equals("")){
								addFieldError("submission.attributes["+i+"].value","Please enter an appropriate value to accompany the attribute name you have entered or remove this attribute entry from the submission.");
							}
							/*else {
								if(att.getValue().length() > DataConstants.ATTRIBUTE_VALUE) {
									addFieldError("submission.attributes["+i+"].value","Attribute value exceeds the maximum character length of "+DataConstants.ATTRIBUTE_VALUE);
								}
							}*/
						}
					}
					else {
						attributes.remove(i);
						i--;
					}
				}
				submission.setAttributes(attributes);
			}
			
			if(!submission.isPrivate()) {
				submission.setAccessGroupIds(null);
			}
		}
		
		//count the number of errors from validation
		if(getFieldErrors().size() > 0 || getActionErrors().size() > 0) {
			setModelDisplayData();
			//submission.setName(getSubmission().getName());
			this.setSubmission(submission);
			return INPUT;
		}
		else {
			submission.setAccession(getAccession());
			ModelAssembler assembler = new ModelAssembler();
			result = assembler.editModel(submission);
			
			if(result == null) {
                            setMsg("model successfully modified");
				return SUCCESS;
			}
			else {
				addActionError(result);
				return INPUT;
			}	
		}
	}

}
