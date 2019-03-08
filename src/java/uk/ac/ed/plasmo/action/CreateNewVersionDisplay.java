package uk.ac.ed.plasmo.action;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.entity.ModelData;

/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve data about a specific model (using it's accession id) so it can be displayed to the
 * user when they want to upload a new version of the model to the database</p>
 * <p>The user must be logged in and be the owner of the model to proceed.</p> 
 * @author ctindal
 *
 */
public class CreateNewVersionDisplay extends EditModelOperations {

	private static final long serialVersionUID = 1L;
	
	private ModelData modelData;
	
	public ModelData getModelData() {
		ModelAssembler assembler = new ModelAssembler();
		modelData = assembler.getModelDataForNewModelVersionDisplay(getAccession());
		return modelData;
	}
	public void setModelData(ModelData modelData) {
		this.modelData = modelData;
	}
	
	public String execute() {
		//make sure the user has the right to edit this model
		return checkAuthorisation();
	}

}
