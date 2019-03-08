/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.plasmo.action;

import uk.ac.ed.plasmo.assembler.PublicationAssembler;
import uk.ac.ed.plasmo.db.MySQLQueryItemDAOImpl;
import uk.ac.ed.plasmo.entity.Publication;

/**
 *
 * @author tzielins
 */
public class RemovePublication extends EditPublication {
 
    @Override
    public Publication getPublication() {
        if (publication == null) publication = new Publication();
        return publication;
    }

    
    @Override
    public String execute() {
        
        String result = this.checkAuthorisation();
        if(!result.equals(SUCCESS)) {
                return result;
        };
        
        try {
        model = getModelData(getAccession());

        if (publication.getPubOID() <= 0) return INPUT;
        
        PublicationAssembler assembler = new PublicationAssembler();
        assembler.removePublication(publication,getAccession());
            
        } catch (MySQLQueryItemDAOImpl.MissingModel | MySQLQueryItemDAOImpl.MissingPublication e) {
            return NO_MODEL;
        }
        
        return SUCCESS;
        
        
    }
    
}
