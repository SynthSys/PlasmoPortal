package uk.ac.ed.plasmo.action;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.assembler.PublicationAssembler;
import uk.ac.ed.plasmo.db.MySQLQueryItemDAOImpl;
import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve data about a specific model (using it's accession id) so it can be displayed to the
 * user when they want to edit the model's meta data</p>
 * <p>The user must be logged in and be the owner of the model to proceed.</p> 
 * @author ctindal
 *
 */
public class EditPublication extends EditModelOperations {

	private static final long serialVersionUID = 1L;
	protected Publication publication;
	protected long pubOID;
        protected ModelSubmission model;
        protected String refType;

        public Publication getPublication() {
            return publication;
        }

        public void setPublication(Publication publication) {
            this.publication = publication;
        }

        public long getPubOID() {
            return pubOID;
        }

        public void setPubOID(long pubOID) {
            this.pubOID = pubOID;
        }

    public ModelSubmission getModel() {
        return model;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }


	
        
	
        @Override
	public String execute() {
		String result = this.checkAuthorisation();
		if(!result.equals(SUCCESS)) {
			return result;
		};
                
                try {
                model = getModelData(getAccession());
                
                if (pubOID > 0) {
                    publication = getPublication(getAccession(),pubOID);
                } else {
                    publication = new Publication();
                    if (refType == null || refType.isEmpty()) refType = "Journal Article";
                    publication.setReferenceType(refType);
                }
                } catch (MySQLQueryItemDAOImpl.MissingModel | MySQLQueryItemDAOImpl.MissingPublication e) {
                    return NO_MODEL;
                }
                return SUCCESS;
	}

    protected Publication getPublication(String accession, long pubOID) {
        PublicationAssembler assembler = new PublicationAssembler();
        return assembler.getPublication(accession,pubOID);
    }

    protected ModelSubmission getModelData(String accession) {
        ModelAssembler assembler = new ModelAssembler();
        return assembler.getModelDataForEditModelDisplay(accession);
    }
	

}
