package uk.ac.ed.plasmo.action;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.entity.VersionSubmission;

/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve data about a specific model (using it's accession id) so it can be displayed to the
 * user when they want to edit the meta data associated with the specified version of the model.</p>
 * <p>The user must be logged in and be the owner of the model to proceed.</p> 
 * @author ctindal
 *
 */
public class EditModelVersionDisplay extends EditModelOperations {

	private static final long serialVersionUID = 1L;
	
	private String version;
	private VersionSubmission submission;
	
	public VersionSubmission getSubmission() {
		return submission;
	}
	public void setSubmission(VersionSubmission submission) {
		this.submission = submission;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String execute() {
		String result = this.checkAuthorisation();
		if(!result.equals(SUCCESS)) {
			return result;
		}
		else {
			this.setVersionDisplayData();
			return result;
		}	
	}
	
	public void setVersionDisplayData() {
		ModelAssembler assembler = new ModelAssembler();
		submission = assembler.getModelDataForEditModelVersionDisplay(getAccession(), version);
	}
	
	
}
