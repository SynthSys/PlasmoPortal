package uk.ac.ed.plasmo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sql.DataSource;

//import org.apache.struts2.ServletActionContext;

import uk.ac.ed.plasmo.control.StartupListener;

public class RelationalDBDAO {
	
	private static DataSource dataSource;
	
	static {
		//dataSource = (DataSource) ServletActionContext.getServletContext().getAttribute("DBPOOL");
		dataSource = (DataSource) StartupListener.getServletContext().getAttribute("DBPOOL");
	}
	
	
	/**
	 * gets a connection to the database
	 * @return connection
	 */
	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * close the connection to the database
	 * @param conn the connection to be closed
	 */
	public static void closeConnection(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
                                try {
                                    if (!conn.getAutoCommit()) {
                                        conn.rollback();
                                        System.out.println("Rollbacked the transaction when auto-closing connection");
                                    }
                                    else {
                                        Statement st = conn.createStatement();
                                        st.execute("UNLOCK TABLES");
                                        st.close();
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error when closing coneciton: "+e.getMessage());
                                    e.printStackTrace(System.out);
                                }
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * release resources being held by PreparedStatement object
	 * @param ps the prepared statement to be closed
	 */
	public static void closePreparedStatement(PreparedStatement ps) {
		try {
			if(ps != null) {
				ps.close();
				ps = null;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * release resources being held by Statement object
	 * @param smt the statement to be closed
	 */
	public static void closeStatement(Statement stmt) {
		try {
			if(stmt!=null) {
				stmt.close();
				stmt = null;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * release resources being held by a ResultSet object
	 * @param rs the result set to be closed
	 */
	public static void closeResultSet(ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * converts a result set to an arrayList of string arrays.
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String []> formatResultSetToArrayList(ResultSet resSet) throws SQLException {
		ResultSetMetaData resSetData = resSet.getMetaData();
		int columnCount = resSetData.getColumnCount();

		//create ArrayList to store each row of results in
		ArrayList<String[]> results = new ArrayList<String[]>();

		while (resSet.next()) {
			String[] columns = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				columns[i] = resSet.getString(i + 1);
			}
			results.add(columns);
		}
		if(results.size() > 0) {
			return results;
		}
		return null;

	}

}
