package uk.ac.ed.plasmo.action;

import java.util.Date;
import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.entity.ModelSubmission;

/**
 * <p>Action class that interacts with the data access layer in order to
 * delete a model from the database.</p>
 * <p>The user must be logged in and be the owner of the model to proceed.</p>
 * <p>NB: This functionality is currently disabled i.e. models cannot be
 * deleted from the database</p>  
 * @author ctindal
 *
 */
public class DeleteModel extends EditModelOperations {
	
	private static final long serialVersionUID = 1L;
	boolean confirmed;
        String modelName;
        String modelDesc;
        
        @Override
	public String execute() {
            
            
		String result = this.checkAuthorisation();
		
		if(!result.equals(SUCCESS)) {
			return result;
		}
		
		ModelAssembler assembler = new ModelAssembler();
                if (!confirmed) {
                    
                    ModelSubmission model = assembler.getModelDataForEditModelDisplay(getAccession());
                    modelName = model.getName();
                    modelDesc = model.getDescription();
                    //System.out.println("D: "+model.getDescription());
                    return INPUT;
                }
                
                /*if (user!= null)
                    System.out.println("User: "+user.getUserName()+" hides model: "+getAccession());
                else 
                    System.out.println("Null user hides model: "+getAccession());
                    */ 
                
                
		//assembler.deleteModel(getAccession());
                int nr = assembler.hideModel(getAccession());
		if (nr != 1)
                    System.out.println("Something went wrong with hiding of the model "+getAccession()+ "wrong number of rows: "+nr);
                
                if (user!= null)
                    System.out.println("User: "+user.getUserName()+" hid model: "+getAccession()+", "+(new Date()));
                else 
                    System.out.println("Null user hid model: "+getAccession()+", "+(new Date()));
                
                setMsg("Model has been deleted");
		return SUCCESS;
	}

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelDesc() {
        return modelDesc;
    }
        
        
}
