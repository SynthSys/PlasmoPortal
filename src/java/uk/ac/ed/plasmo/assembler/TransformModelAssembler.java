package uk.ac.ed.plasmo.assembler;

import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.QueryItemDAO;

/**
 * The {@code UserAssembler} class is used to initialise data access object 
 * and call methods used to retrieve model tranformation through xslt data stored in the database.
 * @author ctindal
 *
 */
public class TransformModelAssembler {
	
	/**
	 * initialise the DAO used to get the file system location of a specific
	 * XSL file used to transform models of specific formats
	 * @param id the database identifier of the xsl file
	 * @param accession the accession no. of the model to be transformed
	 * @param version the version no. of the model to be transformed
	 * @return the file system location of the xsl file
	 */
	public String getXSLTFileLocForModelVsn(String id, String accession, String version) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getXSLTFileLocForModelVersion(id, accession, version);
	}
	
}
