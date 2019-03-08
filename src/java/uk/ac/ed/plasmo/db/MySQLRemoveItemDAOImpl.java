package uk.ac.ed.plasmo.db;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closeConnection;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closePreparedStatement;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.getConnection;

import uk.ac.ed.plasmo.utility.Constants;
import uk.ac.ed.plasmo.utility.FileUtility;

/**
 * DB2 specific implementation of {@link RemoveItemDAO}
 * @author ctindal
 *
 */
public class MySQLRemoveItemDAOImpl extends RelationalDBDAO implements
		RemoveItemDAO {

	@Override
	public void deleteInterimModel(String oid) {
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		try {
			ParamQuery parQ = MySQLUpdate.getParamQuery("REMOVE_INTERIM_MODEL");
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, oid);
			prepStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		
		
		
	}

	@Override
	public String deleteModel(String accession) {
		
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		
		try {
			
			ParamQuery parQ = MySQLUpdate.getParamQuery("DELETE_MODEL");
			
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			prepStat.executeUpdate();
			
			String dataDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX + accession.substring((Constants.DB_ID_PREFIX+"_").length());
			File dir = new File(dataDir);
			
			FileUtility.deleteDirectory(dir);
			
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		} finally {
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		
		return null;
		
	}

    @Override
    public int hideModel(String accession) {
        
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return 0;
		}
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		
		try {
			ParamQuery parQ = MySQLUpdate.getParamQuery(MySQLUpdate.HIDE_MODEL);
			//System.out.println("HQ: "+parQ.getQuerySQL());
                        
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			int nr = prepStat.executeUpdate();
			//System.out.println("NR: "+nr);
			/*String dataDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX + accession.substring((Constants.DB_ID_PREFIX+"_").length());
			File dir = new File(dataDir);
			
			FileUtility.deleteDirectory(dir);
			*/
			conn.setAutoCommit(true);
                        if (nr != 1) {
                            System.out.println("ERROR IN HIDING, wrong nr of updated rows: "+nr);
                            //return false;
                        }
                        return nr;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		} finally {
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		
		
    }
}
