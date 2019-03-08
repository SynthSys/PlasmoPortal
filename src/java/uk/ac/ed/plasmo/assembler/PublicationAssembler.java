/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.plasmo.assembler;

import uk.ac.ed.plasmo.db.ModifyItemDAO;
import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.NewItemDAO;
import uk.ac.ed.plasmo.db.QueryItemDAO;
import uk.ac.ed.plasmo.entity.Publication;

/**
 *
 * @author tzielins
 */
public class PublicationAssembler {

    public Publication getPublication(String accession, long pubOID) {
        QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
        return queryItemDAO.getPublication(accession,pubOID);
    }

    public void savePublication(Publication publication, String accession) {
        NewItemDAO queryItemDAO = MySQLDAOFactory.getNewItemDAO();
        if (publication.getPubOID() > 0) queryItemDAO.updatePublication(publication,accession);
        else queryItemDAO.insertPublication(publication,accession);
    }

    public void removePublication(Publication publication, String accession) {
        NewItemDAO queryItemDAO = MySQLDAOFactory.getNewItemDAO();
        queryItemDAO.removePublication(publication,accession);
    }
    
}
