package uk.ac.ed.plasmo.assembler;

import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.SecurityDAO;
import uk.ac.ed.plasmo.entity.User;

/**
 * The {@code UserAssembler} class is used to initialise data access object 
 * and call methods used to retrieve data stored in the database that is relevant
 * to authorisation and authentication.
 * @author ctindal
 *
 */
public class SecurityAssembler {
	
	/**
	 * initialise the database access object used to 
	 * get a user based on specific user name and password  
	 * @param username the user's user name
	 * @param password the user's password
	 * @return
	 */
	public User getUser(String username, String password) {
		
		if(username == null || password == null){
            return null;
        }
		
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		return securityDAO.getUserWithUsernameAndPassword(username, password);
		
	}
	
	/**
	 * initialise the database access object used to 
	 * get a user based on specific user name  
	 * @param username the user's user name
	 * @return
	 */
	public User getUser(String username) {
		if(username == null){
            return null;
        }
		
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		return securityDAO.getUserWithUsername(username);
	}
	
	/**
	 * initialise the database access object used to check if a user is allowed
	 * to edit a specific model
	 * @param user the user trying to edit the model
	 * @param accession the accession id of the model
	 * @return
	 */
	public Boolean isAuthorisedToEditModel(User user, String accession) {
		
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		
		return securityDAO.isUserAuthorisedToEditModel(user, accession);
	}
	
	/**
	 * initialise the database access object used to check if a user is allowed
	 * to edit a model
	 * @param user the user trying to access the model
	 * @param accession the accession id of the model
	 * @return
	 */
	public Boolean isAuthorisedToAccessModel(User user, String accession) {
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		return securityDAO.isUserAuthorisedToAccessModel(user, accession);
	}
	
	/**
	 * initialise the database access object used to check if a user is allowed 
	 * to change the ownership of a model
	 * @param user
	 * @param accession
	 * @return
	 */
	public Boolean isAuthorisedToChangeModelOwnership(User user, String accession) {
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		return securityDAO.isUserAuthorisedToUpdateModelOwnership(user, accession);
	}

}
