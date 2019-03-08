package uk.ac.ed.plasmo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * the {@code ParamQuery} class builds and configures parameterised queries
 * to be executed on the database 
 * @author ctindal
 *
 */
public class ParamQuery {
	
	//private PreparedStatement prepStat;
	private final String queryName; // descriptive name for query retrieval
	private final String querySQL;  // SQL for query
	
	public ParamQuery(String name, String querySQL) {
		this.queryName = name;
		this.querySQL = querySQL;
	}
	
	/*--key methods for getting SQL query--*/
	public PreparedStatement getPrepStat(Connection conn) throws SQLException {
		return conn.prepareStatement(querySQL);
	}
        
        //in reality no need for them
	private PreparedStatement getScrollablePrepStat(Connection conn) throws SQLException {
		return conn.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
        
	/*--'get' methods for instance variables--*/
	public String getQueryName() {
		return this.queryName;
	}
	
	public String getQuerySQL() {
		return this.querySQL;
	}
	

}
