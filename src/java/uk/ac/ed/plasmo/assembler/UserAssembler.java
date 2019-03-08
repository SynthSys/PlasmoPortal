package uk.ac.ed.plasmo.assembler;

import java.util.HashMap;
import java.util.List;

import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.NewItemDAO;
import uk.ac.ed.plasmo.db.QueryItemDAO;
import uk.ac.ed.plasmo.db.SecurityDAO;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.persistence.RefGroup;

/**
 * The {@code UserAssembler} class is used to initialise data access object 
 * and call methods used to retrieve user data stored in the database.
 * @author ctindal
 *
 */
public class UserAssembler {
	
	/**
	 * initialise the DAO used to create a new user in the database
	 * @param user the details of the user to be created
	 * @return
	 */
	public HashMap<Boolean, Object> createNewUser(User user) {	
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		return newItemDAO.createNewUser(user);
	}
	
	/**
	 * initialise the DAO used to activate a user account
	 * @param activationId the activation id of the user account to be
	 * activated
	 * @return
	 */
	public User activateUser(String activationId) {
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		return securityDAO.activateUserAccount(activationId);
	}
	
	/**
	 * initialise the DAO used to create and retrieve a unique user id
	 * to form part of a link to reset the user's account. The user's email
	 * is used to identify the user in question 
	 * @param email the email of the user
	 * @return
	 */
	public User getUserPasswordResetId(String email) {
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		return securityDAO.getPasswordResetData(email);
	}
	
	/**
	 * initialise the DAO used to retrieve a user using their unique user id
	 * @param uuid
	 * @return
	 */
	public User getUser(String uuid) {
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		return securityDAO.getUserWithUuid(uuid);
	}
	
	/**
	 * initialise the DAO used to update a user's password
	 * @param user
	 * @return
	 */
	public String savePassword(User user) {
		SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO();
		return securityDAO.saveUserPassword(user);
	}
	
	/**
	 * initialise the DAO used to retrieve all the user groups stored in the database 
	 * @return
	 */
	public List<RefGroup> getUserGroups() {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getUserGroups();
	}
	
	

}
