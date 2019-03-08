package uk.ac.ed.plasmo.action;

import uk.ac.ed.plasmo.assembler.DOWNLOAD_TYPE;
import java.io.InputStream;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve a specific model (identified by it's accession) for the user to download.</p>
 * <p>the user must have sufficient privileges to be able to access and download the model.</p> 
 * @author ctindal
 *
 */
public class DownloadModel extends DisplayModelOperations {

	private static final long serialVersionUID = 1L;
	
	
	private String version;
	private InputStream model;
        private DOWNLOAD_TYPE type;

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

        public void setType(DOWNLOAD_TYPE type) {
            this.type = type;
        }
        
	
	public String execute() {
		
		String result = this.checkAuthorisation();
		if(!result.equals(SUCCESS)) {
			return result;
		}
		
                
		ModelAssembler assembler = new ModelAssembler();
		model = assembler.getModelAsInputStream(getAccession(), version,type);
		
		if(model == null) {
			return INPUT;
		}
		else {
			return SUCCESS;
		}
	}
	
	public InputStream getInputStream() {
		//setAccession(getAccession().replaceAll(":", ""));
		//accession = accession.replaceAll(":", "");
	    return model;
	}

}
