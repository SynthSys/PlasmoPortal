package uk.ac.ed.plasmo.db;

import java.util.List;

import uk.ac.ed.plasmo.entity.User;

/**
 * DAO responsible for security-related database queries
 * @author ctindal
 *
 */
public interface SecurityDAO {
	
	/**
	 * gets user details from the database using their username and password
	 * @param username
	 * @param password
	 * @return
	 */
	public User getUserWithUsernameAndPassword(String username, String password);
	
	/**
	 * gets a user from the database using their username
	 * @param username
	 * @return
	 */
	public User getUserWithUsername(String username);
	
	/**
	 * queries database to see if a user is authorised to edit a specific model
	 * @param user the user to be authorised
	 * @param accession the accession no. of the model
	 * @return
	 */
	public Boolean isUserAuthorisedToEditModel(User user, String accession);
	
	/**
	 * checks to see if a user is authorised to access a model
	 * @param user the user to be authorised
	 * @param accession the accession no. of the model
	 * @return
	 */
	public Boolean isUserAuthorisedToAccessModel(User user, String accession);
	
	/**
	 * queries database to see if a user id authorised to change the person
	 * who owns/is the administrator of a specific model 
	 * @param user the user to be authorised
	 * @param accession the accession no. of the model
	 * @return
	 */
	public Boolean isUserAuthorisedToUpdateModelOwnership(User user, String accession);
	
	/**
	 * updates the database activating a user account (so they are able to login) using
	 * a unique activation id to identify the user account to be activated
	 * @param activationId unique id used to identify the user account to be activated
	 * @return
	 */
	public User activateUserAccount(String activationId);
	
	/**
	 * if a user has forgotten their password, this method uses the user's (unique) email address
	 * to update the user account with a unique id which 
	 * @param email
	 * @return
	 */
	public User getPasswordResetData(String email);
	
	/**
	 * gets a user's details from the database using their uuid 
	 * @param uuid the user's uuid
	 * @return
	 */
	public User getUserWithUuid(String uuid);
	
	/**
	 * updates a user's password in the database
	 * @param user the user who's password is to be updated
	 * @return
	 */
	public String saveUserPassword(User user);
}


