package uk.ac.ed.plasmo.assembler;

import java.io.File;
import java.util.List;

import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.NewItemDAO;
import uk.ac.ed.plasmo.db.QueryItemDAO;
import uk.ac.ed.plasmo.entity.User;

/**
 * The {@code UserAssembler} class is used to initialise data access object 
 * and call methods used to retrieve and submit model format data stored in the database.
 * @author ctindal
 *
 */
public class ModelFormatAssembler {
	
	/**
	 * initialise the DAO used to add a new model format to the database
	 * @param format the name of the format to be submitted
	 * @param description the description of the format
	 * @param schema the XML Schema file used to validate models of this format
	 * @param schemaFileName the file name of the schema file
	 * @param user the user submitting the format
	 * @return
	 */
	public String addNewModelFormat(String format, String description, File schema, String schemaFileName, User user) {
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		
		String message = newItemDAO.addNewModelFormat(format, description, schema, schemaFileName, user);
		
		return message;
	}
	
	/**
	 * initialise the DAO used to get all model formats from the database
	 * @return
	 */
	public List<String> getModelFormats() {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getAllModelFormats();
	}
	
	/**
	 * initialise the DAO used to get the file system location of the schema file for
	 * a particular model format
	 * @param formatName the name of the format
	 * @return
	 */
	public String getSchemaLocation(String formatName) {
		
		if(formatName == null || formatName.equals("")) {
			return null;
		}
		
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		String schemaLocation = queryItemDAO.getSchemaLocation(formatName);
		return schemaLocation;
	}

}
