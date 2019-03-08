package uk.ac.ed.plasmo.action;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.entity.ModelSubmission;
/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve data about a specific model (using it's accession id) so it can be displayed to the
 * user when they want to edit the model's meta data</p>
 * <p>The user must be logged in and be the owner of the model to proceed.</p> 
 * @author ctindal
 *
 */
public class EditModelPublications extends EditModelOperations {

	private static final long serialVersionUID = 1L;
	private ModelSubmission submission;
	
	public ModelSubmission getSubmission() {
		return submission;
	}
	public void setSubmission(ModelSubmission submission) {
		this.submission = submission;
	}
	
	public String execute() {
		String result = this.checkAuthorisation();
		if(!result.equals(SUCCESS)) {
			return result;
		}
		else {
			this.setModelDisplayData();
			return result;
		}	
	}
	
	public void setModelDisplayData() {
            ModelAssembler assembler = new ModelAssembler();
            submission = assembler.getModelDataForEditPublicationsDisplay(getAccession());
	}

}
