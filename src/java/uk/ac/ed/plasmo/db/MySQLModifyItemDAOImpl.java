package uk.ac.ed.plasmo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ed.plasmo.entity.GroupAttributes;
import uk.ac.ed.plasmo.entity.User;

/**
 * DB2 specific implementation of {@link ModifyItemDAO}
 * @author ctindal
 *
 */
public class MySQLModifyItemDAOImpl extends RelationalDBDAO implements
		ModifyItemDAO {

	@Override
	public void setModelIsDeleted(String accession, boolean delete) {
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		
		try {
			conn.setAutoCommit(true);
			int del;
			if(delete) {
				del = 0;
			}
			else {
				del = 1;
			}
			
			ParamQuery parQ = MySQLUpdate.getParamQuery("SET_MODEL_DELETED");
			
			prepStat = parQ.getPrepStat(conn);
			
			prepStat.setInt(1, del);
			prepStat.setString(2, accession);
			
			prepStat.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		} finally {
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}

	}
	
	@Override
	public String updateModelOwnership(User supervisor, User newOwner,
			String accession) {
		
		if(newOwner == null || newOwner.getUserName() == null) {
			return "An error occurred. You have not specified the new owner of the model.";
		}
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		Statement stmt = null;
        ResultSet resSet = null;
        
        try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			//get the list of users supervised by this user
			ParamQuery parQ = MySQLQuery.getParamQuery("SUPERVISED_USERS_AND_SELF");
			//prepStat = parQ.getScrollablePrepStat(conn);
			prepStat = parQ.getPrepStat(conn);
			prepStat.setInt(1, supervisor.getOid());
			prepStat.setInt(2, supervisor.getOid());
			
			resSet = prepStat.executeQuery();
                        List<String> supervisedUserNames = new ArrayList<>();
                        while(resSet.next()) {
                            supervisedUserNames.add(resSet.getString(2));
                        }  
                        
                        if(supervisedUserNames.contains(newOwner.getUserName())) {
                                parQ = MySQLUpdate.getParamQuery("UPDATE_MODEL_OWNER");
                                prepStat = parQ.getPrepStat(conn);

                                prepStat.setString(1, newOwner.getUserName());
                                prepStat.setString(2, accession);

                                prepStat.executeUpdate();
				conn.commit();	
                        }
                        else {
                                conn.rollback();
                                return "An error occurred. You have tried to set the ownership of this model to a user you do not supervise";
                        }                        
                        
                        /*
			if(resSet.first()) {
				resSet.beforeFirst();
				//set the list of user id's that ownership of the model can be granted to
				//(i.e. all the users supervised by the current supervisor and the supervisor)
				List<String> supervisedUserNames = new ArrayList<>();
				while(resSet.next()) {
					supervisedUserNames.add(resSet.getString(2));
				}
				
				//does the new owner id match any of the ids in the list
				if(supervisedUserNames.contains(newOwner.getUserName())) {
					parQ = MySQLUpdate.getParamQuery("UPDATE_MODEL_OWNER");
					prepStat = parQ.getPrepStat(conn);
					
					prepStat.setString(1, newOwner.getUserName());
					prepStat.setString(2, accession);
					
					prepStat.executeUpdate();
					
				}
				else {
					conn.rollback();
					return "An error occurred. You have tried to set the ownership of this model to a user you do not supervise";
				}
				
			}
                        
			conn.commit();
			*/
                        
			//return null if all went well
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			return "An unexpected error occurred. Please try again";
		} finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}

	@Override
	public void updateGroupPermissions(String accession,
			GroupAttributes grpAttribute) {
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		Statement stmt = null;
                ResultSet resSet = null;
        
        try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			//first delete any records for this group and model already in the permission table
			ParamQuery parQ = MySQLUpdate.getParamQuery("DELETE_MODEL_GROUP_PERMISSION");
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			prepStat.setString(2, grpAttribute.getName());
			prepStat.executeUpdate();
			
			//If the attribute is edit or access then update the table
			if(grpAttribute.getAccess() == true){
				parQ = MySQLUpdate.getParamQuery("ADD_MODEL_GROUP_READ_ACCESS");
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				prepStat.setString(2, grpAttribute.getName());
				prepStat.executeUpdate();
			}
			else if(grpAttribute.getEdit() == true){
				parQ = MySQLUpdate.getParamQuery("ADD_MODEL_GROUP_WRITE_ACCESS");
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				prepStat.setString(2, grpAttribute.getName());
				prepStat.executeUpdate();
			}
		
			conn.commit();
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
                        throw new RuntimeException("Could not update permissions");
		} finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		
	}
}
