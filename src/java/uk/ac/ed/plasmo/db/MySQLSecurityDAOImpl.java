package uk.ac.ed.plasmo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.liferay.portal.security.pwd.PwdEncryptor;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.Constants;

/**
 * DB2 specific implementation of {@link SecurityDAO}
 * @author ctindal
 *
 */
public class MySQLSecurityDAOImpl extends RelationalDBDAO implements SecurityDAO {
	
	@Override
	public User activateUserAccount(String activationId) {
		
		if(activationId == null || activationId.length() != DataConstants.USER_UUID_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		ParamQuery parQ = MySQLQuery.getParamQuery("USER_BY_ACTIVATION_ID");
		
		try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			prepStat =  parQ.getPrepStat(conn);
			
			prepStat.setString(1, activationId);
			
			resSet = prepStat.executeQuery();
			
			if(resSet.first()) {
				User user = new User();
				user.setUserName(resSet.getString(1));
                                user.setEmail(resSet.getString(2));
                                user.setFamily(resSet.getString(3));
                                user.setGiven(resSet.getString(4));
                                user.setOrganisation(resSet.getString(5));
                                user.setOid(resSet.getInt(6));

                                //get the groups this user belongs to
                                parQ = MySQLQuery.getParamQuery("USERS_GROUPS");
                                prepStat =  parQ.getPrepStat(conn);
                                prepStat.setInt(1, user.getOid());
                                resSet = prepStat.executeQuery();
                                List<String> gps = new ArrayList<>();
                                while(resSet.next()) {
                                        gps.add(resSet.getString(1));
                                }
                                user.setGroups(gps);

                                /*
                                        if(resSet.first()) {
                                                resSet.beforeFirst();
                                                List<String> gps = new ArrayList<>();
                                                while(resSet.next()) {
                                                        gps.add(resSet.getString(1));
                                                }
                                                user.setGroups(gps);
                                        }*/
            	
                            parQ = MySQLUpdate.getParamQuery("ACTIVATE_USER");

                            prepStat =  parQ.getPrepStat(conn);
                            prepStat.setString(1, user.getUserName());

                            prepStat.executeUpdate();

                            conn.commit();
                            conn.setAutoCommit(true);
                            return user;
			}
			
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public User getUserWithUuid(String uuid) {
		
		if(uuid == null || uuid.length() != DataConstants.USER_UUID_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("USER_BY_ACTIVATION_ID");
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		try {
			prepStat =  parQ.getPrepStat(conn);
                        prepStat.setString(1, uuid);
                        resSet = prepStat.executeQuery();
            
                        if(resSet.first()){
                            return this.getUser(conn, prepStat, resSet);
                        }
                        return null;
            
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public User getUserWithUsername(String username) {
		if(username == null || username.length() > DataConstants.MAX_USER_USERNAME_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("USER_BY_USERNAME");
		
		ResultSet resSet = null;
        PreparedStatement prepStat = null;
        
        try {
                prepStat =  parQ.getPrepStat(conn);
                prepStat.setString(1, username);

                resSet = prepStat.executeQuery();
                if(resSet.first()){
                    return this.getUser(conn, prepStat, resSet);
            }
            return null;
            
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public User getUserWithUsernameAndPassword(String username, String password) {
		
		if(username == null || password == null || 
				username.length() > DataConstants.MAX_USER_USERNAME_LENGTH 
				|| password.length() > DataConstants.MAX_USER_PASSWORD_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("USER_LOGIN");
		
		ResultSet resSet = null;
        PreparedStatement prepStat = null;
        
        if(Constants.PWD_ENCRYPTED) {
        	password = PwdEncryptor.encrypt(password);
        }
        
        try {
            prepStat =  parQ.getPrepStat(conn);
            prepStat.setString(1, username);
            prepStat.setString(2, password);
            resSet = prepStat.executeQuery();
            if(resSet.first()){
                if(resSet.getString(1).equals(username) && resSet.getString(2).equals(password)){
                	return this.getUser(conn, prepStat, resSet);
                }
            }
            return null;
            
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}

	@Override
	public Boolean isUserAuthorisedToEditModel(User user, String accession) {
		
		if(user == null || user.getUserName() == null || 
				user.getUserName().trim().equals("")) {
			return false;
		}
		
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_OWNER");
		ResultSet resSet = null;
        PreparedStatement prepStat = null;
        
        try {
			prepStat =  parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			
			resSet = prepStat.executeQuery();
			if(resSet.first()) {
				if(user.getUserName().equals(resSet.getString(3))) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public Boolean isUserAuthorisedToUpdateModelOwnership(User user,
			String accession) {
		
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		if(user == null || user.getOid() == null) {
			return false;
		}
		Connection conn = getConnection();
		//query to find out if user is owner and also a supervisor or is the supervisor of the model owner
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_OWNER_AND_SUPERVISOR_OR_OWNER_SUPERVISOR");
		ResultSet resSet = null;
        PreparedStatement prepStat = null;
        
        try {
			prepStat =  parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			prepStat.setInt(2, user.getOid());
			prepStat.setString(3, accession);
			prepStat.setInt(4, user.getOid());
			
			resSet = prepStat.executeQuery();
			if(resSet.first()) {
				return true;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public Boolean isUserAuthorisedToAccessModel(User user, String accession) {
		
		if(user == null || user.getUserName() == null) {
			user = new User();
			//user.setUserName("");
			user.setOid(-1);
		}
		
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_IS_PUBLIC_AND_OWNER_ID");
		ResultSet resSet = null;
        PreparedStatement prepStat = null;
		
        try {
			prepStat =  parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			
			resSet = prepStat.executeQuery();
			if(resSet.first()) {
				//if the model is private
				if(resSet.getInt(1) == 0) {
					//only allow access if the user is the submitter
					if(user.getOid() == resSet.getInt(2)) {
						return true;
					}
					//or if the user is a member of a group with model read access
					else {
						
						parQ = MySQLQuery.getParamQuery("GROUPS_WITH_MODEL_READ_ACCESS");
						prepStat =  parQ.getPrepStat(conn);
						prepStat.setString(1, accession);
						resSet = prepStat.executeQuery();
                                                
                                                Set<String> allowedToRead = new HashSet<>();
                                                while(resSet.next()) {
                                                        allowedToRead.add(resSet.getString(1));
                                                }
                                                List<String> groups = user.getGroups();
                                                allowedToRead.retainAll(groups);
                                                if (!allowedToRead.isEmpty()) return true;

                                                
                                                /*
						//if the model has associated groups with read access
						if(resSet.first()) {
							List<String> allowedToRead = new ArrayList<>();
							resSet.beforeFirst();
							while(resSet.next()) {
								allowedToRead.add(resSet.getString(1));
							}
							//get the groups this user is a member of
							List<String> groups = user.getGroups();
							if(groups != null && groups.size() > 0) {
								//find a match between 'allowed to read' groups and groups this
								//user is a member of
								for(String groupN : allowedToRead) {
									if(groups.contains(groupN)) {
										return true;
										}
									}	
							}
						}*/
						
						parQ = MySQLQuery.getParamQuery("SUPERVISOR_OF_MODEL_OWNER");
						prepStat =  parQ.getPrepStat(conn);
						prepStat.setString(1, accession);
						resSet = prepStat.executeQuery();
                                                
                                                Set<Integer> supervisorIds = new HashSet<>();
                                                while(resSet.next()) {
                                                        supervisorIds.add(resSet.getInt(1));
                                                }
                                                if(supervisorIds.contains(user.getOid())) {
                                                        return true;
                                                }
                                                
                                                /*
						//if the model owner has a supervisor
						if(resSet.first()) {
							List<Integer> supervisorIds = new ArrayList<>();
							resSet.beforeFirst();
							while(resSet.next()) {
								supervisorIds.add(resSet.getInt(1));
							}
							//if the user is a supervisor of the model owner then they have access rights
							if(supervisorIds.contains(user.getOid())) {
								return true;
							}
						}*/
						
						return false;
					}
				}
				else {
					return true;
				}
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}

	@Override
	public User getPasswordResetData(String email) {
		if(email == null || email.length() > DataConstants.MAX_USER_EMAIL_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLUpdate.getParamQuery("UPDATE_USER_UUID_BY_EMAIL");
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		try {
			 
			prepStat =  parQ.getPrepStat(conn);
			
			String uuid = UUID.randomUUID().toString();
			prepStat.setString(1, uuid);
			prepStat.setString(2, email);
			
			int update = prepStat.executeUpdate();
			
			//only continue if the update occurred
			if(update > 0) {
				
				parQ = MySQLQuery.getParamQuery("USER_BY_EMAIL");
				
				prepStat =  parQ.getPrepStat(conn);
				prepStat.setString(1, email);
				resSet = prepStat.executeQuery();
				
				if(resSet.first()) {
					User user = new User();
					user.setUserName(resSet.getString(2));
					user.setEmail(resSet.getString(3));
					user.setGiven(resSet.getString(4));
					user.setFamily(resSet.getString(5));
					user.setOrganisation(resSet.getString(6));
					user.setUuid(resSet.getString(7));
					return user;
				}
				
				return null;
				
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}

	@Override
	public String saveUserPassword(User user) {
		if(user == null) {
			return "An unexpected error occurred. Could not find new password details.";
		}
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
        
        ParamQuery parQ = null;
        
        try {
        	
        	conn.setAutoCommit(false);
			parQ = MySQLUpdate.getParamQuery("SAVE_NEW_USER_PASSWORD");
			
			 
			prepStat =  parQ.getPrepStat(conn);
			prepStat.setString(1, PwdEncryptor.encrypt(user.getPassword()));
			prepStat.setString(2, user.getUserName());
			
			int update = prepStat.executeUpdate();
			
			if(update == 1) {
				conn.commit();
				return null;
			}
			else {
				conn.rollback();
				return "An unexpected error occurred. Please try again.";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		
	}

	private User getUser(Connection conn, PreparedStatement prepStat, ResultSet resSet) throws SQLException {
		User user = new User();
    	user.setUserName(resSet.getString(1));
    	user.setEmail(resSet.getString(3));
    	user.setFamily(resSet.getString(4));
    	user.setGiven(resSet.getString(5));
    	user.setOrganisation(resSet.getString(6));
    	user.setOid(resSet.getInt(7));
    	
    	//get the groups this user belongs to
    	ParamQuery parQ = MySQLQuery.getParamQuery("USERS_GROUPS");
        prepStat =  parQ.getPrepStat(conn);
        prepStat.setInt(1, user.getOid());
        resSet = prepStat.executeQuery();

        List<String> gps = new ArrayList<>();
        while(resSet.next()) {
                gps.add(resSet.getString(1));
        }
        user.setGroups(gps);

        /*
        if(resSet.first()) {
                resSet.beforeFirst();
                List<String> gps = new ArrayList<>();
                while(resSet.next()) {
                        gps.add(resSet.getString(1));
                }
                user.setGroups(gps);
        }*/
    	
        //get the list of users this user supervises
        parQ = MySQLQuery.getParamQuery("SUPERVISED_USERS_AND_SELF");
        prepStat =  parQ.getPrepStat(conn);
        prepStat.setInt(1, user.getOid());
        prepStat.setInt(2, user.getOid());
        resSet = prepStat.executeQuery();
        List<User> supervisedUsers = new ArrayList<>();

        while(resSet.next()) {
            User supervised = new User();
            supervised.setOid(resSet.getInt(1));
            supervised.setUserName(resSet.getString(2));
            supervised.setGiven(resSet.getString(3));
            supervised.setFamily(resSet.getString(4));
            supervisedUsers.add(supervised);
        }
        supervisedUsers.remove(user);
        
        /*
        if(resSet.first()) {
                resSet.beforeFirst();
                if(resSet.last()) {
                        //only add to supervises if the size of the list is greater 
                        //than 1 as query will always return current user
                        if(resSet.getRow() > 1) {
                                resSet.beforeFirst();
                                while(resSet.next()) {
                                User supervised = new User();
                                supervised.setOid(resSet.getInt(1));
                                supervised.setUserName(resSet.getString(2));
                                supervised.setGiven(resSet.getString(3));
                                supervised.setFamily(resSet.getString(4));
                                supervisedUsers.add(supervised);
                                }
                        }
                }
        }*/
        
        user.setSupervisesUsers(supervisedUsers);

	user.setIsSupervisor(user.getSupervisesUsers().size() > 0);
		
    	return user;
	}
}
