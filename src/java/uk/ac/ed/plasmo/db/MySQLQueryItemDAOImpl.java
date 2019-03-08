package uk.ac.ed.plasmo.db;

import java.util.ArrayList;
import java.util.List;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closeConnection;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closePreparedStatement;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closeResultSet;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.getConnection;


import uk.ac.ed.plasmo.entity.Attribute;
//import uk.ac.ed.plasmo.entity.DataFile;
import uk.ac.ed.plasmo.entity.GroupAttributes;
import uk.ac.ed.plasmo.entity.ModelData;
import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.entity.VersionSubmission;
import uk.ac.ed.plasmo.persistence.RefGroup;
import uk.ac.ed.plasmo.utility.Constants;

/**
 * DB2 specific implementation of {@link QueryItemDAO}
 * @author ctindal
 *
 */
public class MySQLQueryItemDAOImpl extends RelationalDBDAO implements
		QueryItemDAO {

	@Override
	public List<String> getAllModelFormats() {
		
		Connection conn = getConnection();
		
		Statement stmt = null;
        ResultSet resSet = null;
        
        try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			resSet = stmt.executeQuery(MySQLQuery.ALL_MODEL_FORMATS);
			
			if(resSet.first()) {
				
				ArrayList <String> results = new ArrayList<>();
				
				resSet.beforeFirst();
				while(resSet.next()) {
					results.add(resSet.getString(1));
				}
				return results;
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closeConnection(conn);
		}
	}
	
	@Override
	public String getXSLTFileLocForModelVersion(String id, String accession, String version) {
		
		try {
			//make sure the id no supplied is valid. If not, null
			Integer.parseInt(id);
		}
		catch(Exception e) {
			return null;
		}
		//make sure the accession supplied is valid. If not, null
		if(accession == null || accession.length() > DataConstants.MAX_ACCESSION_LENGTH){
			return null;
		}
		boolean useLatestVersion = false;
		if(version == null) {
			useLatestVersion = true;
		}
		else {
			int versionNo;
			try {
				versionNo = Integer.parseInt(version);
				if(versionNo < DataConstants.MIN_VERSION_NO || versionNo > DataConstants.MAX_VERSION_NO){
					return null;
				}
			}
			catch(Exception e) {
				return null;
			}
		}
		
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		ParamQuery parQ =null;// MySQLQuery.getParamQuery("XSLT_FILE_FOR_MODEL");
		try {
			
			if(useLatestVersion){
				//query to find the current maximum model version number
	        	parQ = MySQLQuery.getParamQuery("MAX_MODEL_VERSION");
	        	
	        	prepStat = parQ.getPrepStat(conn);
	        	
	        	prepStat.setString(1, accession);
	        	resSet = prepStat.executeQuery();
	        	
	        	//if previous versions of the model already exist
	        	if(resSet.first()) {
	        		version = resSet.getString(1);
	        	}
	        	else {
	        		return null;
	        	}
			}
			
			parQ = MySQLQuery.getParamQuery("XSLT_FILE_FOR_MODEL");
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, id);
			prepStat.setString(2, accession);
			prepStat.setString(3, version);
			
			resSet = prepStat.executeQuery();
			
			if(resSet.first()) {
				return Constants.UPLOAD_XSLT_DIR + resSet.getString(1) + "/" + resSet.getString(2);
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally{
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	
	@Override
	public String getSchemaLocation(String formatName) {
		
		if(formatName == null || formatName.equals("")) {
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		try {
			ParamQuery parQ = MySQLQuery.getParamQuery("SCHEMA_LOCATION");
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, formatName);
			
			resSet = prepStat.executeQuery();
			if(resSet.first()) {
				if(resSet.getString(2) != null && !resSet.getString(2).equals("")) {
					return Constants.UPLOAD_SCHEMA_DIR + resSet.getString(1) + "/" + resSet.getString(2);
				}
				else {
					return "";
				}
			}
			else {
				return null;
			}
				 
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public int getTotalNumberOfUserModels(User user) {
		
		if(user == null || user.getUserName() == null) {
			return 0;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		ParamQuery parQ = MySQLQuery.getParamQuery("TOTAL_NUMBER_OF_USER_MODELS");
		
		try {
			
			 
			prepStat = parQ.getPrepStat(conn);
	        prepStat.setString(1, user.getUserName());
	        
			resSet = prepStat.executeQuery();
			
			int totalNumberOfModels = 0;
			
			while(resSet.next()) {
				totalNumberOfModels = resSet.getInt(1);
			}
			return totalNumberOfModels;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public int getTotalModelsFromSearch(User user, String queryParam) {
		
                
		if(queryParam == null || queryParam.trim().equals("")){
			return 0;
		}
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		//ParamQuery parQ = new ParamQuery("","");//MySQLQuery.getParamQuery("NUM_MODELS_FOUND_FROM_SEARCH");
		
		//because the query is dynamic, it will be built on the fly depending on certain parameters
		//received from other layers of the application
		StringBuilder sb = new StringBuilder("SELECT COUNT(DISTINCT MDL_ACCESSION_ID) FROM (");
		
		boolean loggedIn = (user != null && user.getOid() != null);
		
		//construct the rest of the query
		sb.append(buildSearchQuery(loggedIn));
		sb.append(") AS MODDET ");
		
		//parQ.setQuerySQL(sb.toString());
		
		//remove case-sensitivity
		queryParam = queryParam.toUpperCase();
		if(queryParam.equals("*")) {
			queryParam = "";
		}
		
		try {
			
			 
			prepStat = conn.prepareStatement(sb.toString());//parQ.getPrepStat(conn);
			
			int numParams = 4;
			for(int i=0;i<MySQLQuery.SEARCH_COLS.size();i++) {
				if(loggedIn) {
					prepStat.setInt((i*numParams)+1, user.getOid());
					prepStat.setInt((i*numParams)+2, user.getOid());
					prepStat.setInt((i*numParams)+3, user.getOid());
					prepStat.setString((i*numParams)+4, "%"+queryParam+"%");
				}
				else {
					prepStat.setString(i+1, "%"+queryParam+"%");
				}
			}
			
			resSet = prepStat.executeQuery();
			
			int numModels = 0;
			
			while(resSet.next()) {
				numModels = resSet.getInt(1);
			}
			
			return numModels;
			
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public ArrayList<String[]> searchModels(String queryParam, int columnIndex, boolean ascending,
			int offset, int num, User user) {
		
                num = num-offset+1;
                if (num < 0) num = 5;
            
            //System.out.println("searchModels");
            
		if(queryParam == null || queryParam.trim().equals("")){
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		PreparedStatement prepStatII = null;
		ResultSet resSetII = null;
        
        //ParamQuery parQ = new ParamQuery("","");
        
        //because the query is dynamic, it will be built on the fly depending on certain parameters
		//received from other layers of the application
		StringBuilder sb = new StringBuilder( 
				MySQLQuery.OUTER_BROWSE_MODELS_COLS + 
        		
        		" FROM ( ");
		
		boolean loggedIn = (user != null && user.getOid() != null);
		
		//construct the rest of the query
		sb.append(buildSearchQuery(loggedIn));
		
		sb.append(") AS MODDET " + MySQLQuery.MODEL_NAME_PAGINATION);
        
                //System.out.println("Q345: "+sb.toString());
        String queryString = this.assembleBrowseModelsQ(columnIndex, sb.toString(), MySQLQuery.ORDER_BY_MODEL_NAME, ascending);
                //System.out.println("Q347: "+queryString);
        //parQ.setQuerySQL(queryString);
        
        //remove case-sensitivity
        queryParam = queryParam.toUpperCase();
        if(queryParam.equals("*")) {
			queryParam = "";
		}
        
        try {
        	
        	 
			prepStat = conn.prepareStatement(queryString);//parQ.getPrepStat(conn);
                        /*{
			ParameterMetaData pams = prepStat.getParameterMetaData();
                        System.out.println("Pams size: "+pams.getParameterCount());
                        for (int i = 1;i<=pams.getParameterCount();i++) {
                            System.out.println("P"+i+": "+pams.getParameterTypeName(i));
                        }
                        }*/
			int numParams = 4;
                        //int pamsSet = 0;
			for(int i=0;i<MySQLQuery.SEARCH_COLS.size();i++) {
				if(loggedIn) {
					prepStat.setInt((i*numParams)+1, user.getOid());
					prepStat.setInt((i*numParams)+2, user.getOid());
					prepStat.setInt((i*numParams)+3, user.getOid());
					prepStat.setString((i*numParams)+4, "%"+queryParam+"%");
                                        //pamsSet+=4;
                                        //System.out.println("PS 4f:"+((i*numParams)+1)+"-"+((i*numParams)+4));
				}
				else {
					prepStat.setString(i+1, "%"+queryParam+"%");
                                        //pamsSet+=1;
                                        //System.out.println("PS 1f:"+(i+1));
				}
			}
			
			if(loggedIn) {
				prepStat.setInt(numParams*MySQLQuery.SEARCH_COLS.size()+1, offset-1);
				prepStat.setInt(numParams*MySQLQuery.SEARCH_COLS.size()+2, num);
                                //pamsSet+=2;
                                //System.out.println("PS 2f:"+(numParams*MySQLQuery.SEARCH_COLS.size()+1)+"-"+(numParams*MySQLQuery.SEARCH_COLS.size()+2));
			}
			else {
				prepStat.setInt(MySQLQuery.SEARCH_COLS.size()+1, offset-1);
				prepStat.setInt(MySQLQuery.SEARCH_COLS.size()+2, num);
                                //pamsSet+=2;
                                //System.out.println("PS 2f:"+(MySQLQuery.SEARCH_COLS.size()+1)+"-"+(MySQLQuery.SEARCH_COLS.size()+2));
			}
	        //System.out.println("PamsSet: "+pamsSet);
	        resSet = prepStat.executeQuery();
	        
	        ResultSetMetaData resSetData = resSet.getMetaData();
			int columnCount = resSetData.getColumnCount(); //substract 1 because you don't need the row no.
			
			//create ArrayList to store each row of results in
			ArrayList<String[]> results = new ArrayList<>();
                        ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_DESCRIPTION");
			 
			prepStatII = parQ.getPrepStat(conn);
			
			//set the main values in an array
			while (resSet.next()) {
				String[] columns = new String[columnCount+1];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSet.getString(i + 1);
				}
				
				//need to get the description for each model
				prepStatII.setString(1, resSet.getString(1));
				resSetII = prepStatII.executeQuery();
				while(resSetII.next()) {
					columns[columnCount] = resSetII.getString(1);
				}
				results.add(columns);
				prepStatII.clearParameters();
			}
			
			if(results.size() > 0) {
				return results;
			}
			return null;
	        
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closeResultSet(resSetII);
			closePreparedStatement(prepStat);
			closePreparedStatement(prepStatII);
			closeConnection(conn);
		}
	}
	
	/**
	 * builds part of the query used when searching for specific models.
	 * @param loggedIn
	 * @return
	 */
	private String buildSearchQuery(boolean loggedIn){
		
		StringBuilder sb = new StringBuilder("");
		
		List<String> searchCols = MySQLQuery.SEARCH_COLS;
		
		//for each of the above search params, build each UNION-based query
		for(int i=0;i<searchCols.size();i++) {
			sb.append(MySQLQuery.SEARCH_MODELS_BASE_II);
			
			//only add permissions-based tables and joins if the user is logged in
			if(loggedIn) {
				sb.append(MySQLQuery.USER_PERMISSIONS_TABLES);
			}
			
			//only add the attribute table join if this is the union query used to
			//search through the attribute values of each model
			if(searchCols.get(i).indexOf("ATT_VALUE") >= 0) {
				sb.append(MySQLQuery.JOIN_ATTRIBUTE_TABLE);
			}
			
			sb.append(" WHERE " +MySQLQuery.LATEST_VERSION_CRITERIA);
			
			if(loggedIn) {
				sb.append(MySQLQuery.USER_PERMISSIONS_JOIN_CRITERIA);
			}
			else {
				sb.append("AND MDL_IS_PUBLIC = 1");
			}
			sb.append(" AND MDL_IS_DELETED=0");
			sb.append(searchCols.get(i));
			if(i+1 != searchCols.size()) {
				sb.append(" UNION ");
			}
                        //System.out.println("BS iter: "+i+":\n"+sb.toString());
	
		}
		
		return sb.toString();
	}
	
	@Override
	public int getTotalNumberOfModels(User user) {
		
		Connection conn = getConnection();
		
		Statement stmt = null;
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		ParamQuery parQ = new ParamQuery("","");
		
		StringBuilder sb = new StringBuilder("SELECT COUNT(DISTINCT MDL_ACCESSION_ID) FROM ( " +
        		MySQLQuery.ALL_PUBLIC_MODELS_Q);
        
        if(user != null && user.getOid() != null) {
        	sb.append(" UNION " + 
        			MySQLQuery.ALL_PRIVATE_USER_MODELS + " UNION " +
        			MySQLQuery.ALL_GROUP_ACCESS_RESTRICTED_MODELS + " UNION " +
        			MySQLQuery.ALL_RESTRICTED_MODELS_FOR_SUPERVISOR);
        }
        
        sb.append(") AS MODS");
        
        //System.out.println("QI500 "+sb.toString());
        //parQ.setQuerySQL(sb.toString());
		
            //System.out.println("QUERY:\n"+sb.toString()+"\n ");
		try {
			
			 
			prepStat = conn.prepareStatement(sb.toString());//parQ.getPrepStat(conn);
			if(user != null && user.getOid() != null) {
				prepStat.setInt(1, user.getOid());
				prepStat.setInt(2, user.getOid());
				prepStat.setInt(3, user.getOid());
			}
	        resSet = prepStat.executeQuery();
			//stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			//resSet = stmt.executeQuery(MySQLQuery.TOTAL_NUMBER_OF_MODELS);
			
			int totalNumberOfModels = 0;
			
			while(resSet.next()) {
				totalNumberOfModels = resSet.getInt(1);
			}
			return totalNumberOfModels;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeStatement(stmt);
			closeConnection(conn);
		}
	}
	
	@Override
	public ArrayList<String[]> browseModels(int columnIndex, boolean ascending, int offset, int num, User user) {
		
                num = num-offset+1;
                if (num < 0) num = 5;
                
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		PreparedStatement prepStatII = null;
		ResultSet resSetII = null;
        
        //ParamQuery parQ = MySQLQuery.getParamQuery("ALL_MODELS");
        ParamQuery parQ = new ParamQuery("","");
        
        StringBuilder sb = new StringBuilder( 
        		MySQLQuery.OUTER_BROWSE_MODELS_COLS + 
        		" FROM ( " +
        		MySQLQuery.ALL_PUBLIC_MODELS_Q);
        
        if(user != null && user.getOid() != null) {
        	sb.append(" UNION " + 
        			MySQLQuery.ALL_PRIVATE_USER_MODELS + " UNION " +
        			MySQLQuery.ALL_GROUP_ACCESS_RESTRICTED_MODELS + " UNION " +
        			MySQLQuery.ALL_RESTRICTED_MODELS_FOR_SUPERVISOR);
        }
        
        sb.append(") AS MODDET "+ MySQLQuery.MODEL_NAME_PAGINATION);
        
        //String query = MySQLQuery.ALL_MODELS;
        
        String defaultOrder = MySQLQuery.ORDER_BY_MODEL_NAME;
        
        String queryString = this.assembleBrowseModelsQ(columnIndex, sb.toString(), defaultOrder, ascending);
        //parQ.setQuerySQL(queryString);
        
        //System.out.println("BM 1Q:\n"+queryString+"\n ");
        //System.out.println("offset:"+offset+" number:"+num);

        
        try {
        	
			 
			prepStat = conn.prepareStatement(queryString);//parQ.getPrepStat(conn);
			if(user != null && user.getOid() != null) {
				prepStat.setInt(1, user.getOid());
				prepStat.setInt(2, user.getOid());
				prepStat.setInt(3, user.getOid());
				prepStat.setInt(4, offset-1);
                                prepStat.setInt(5, num);
			}
			else {
				prepStat.setInt(1, offset-1);
                                prepStat.setInt(2, num);
			}
	        
	        resSet = prepStat.executeQuery();
	        
	        ResultSetMetaData resSetData = resSet.getMetaData();
			int columnCount = resSetData.getColumnCount(); //subtract 1 because don't need to count "ROWNUMBER" col
			
			//create ArrayList to store each row of results in
			ArrayList<String[]> results = new ArrayList<>();
			
			parQ = MySQLQuery.getParamQuery("MODEL_DESCRIPTION");
			 
			prepStatII = parQ.getPrepStat(conn);
			
                        //System.out.println("BM 2Q:\n"+parQ.getQuerySQL()+"\n ");
			//set the main values in an array
			while (resSet.next()) {
				String[] columns = new String[columnCount+1];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSet.getString(i + 1);
				}
				
				//need to get the description for each model
				prepStatII.setString(1, resSet.getString(1));
				resSetII = prepStatII.executeQuery();
				while(resSetII.next()) {
					columns[columnCount] = resSetII.getString(1);
                                        //System.out.println("DESC FOR: "+resSet.getString(1)+" is: "+columns[columnCount]);
				}
				results.add(columns);
				prepStatII.clearParameters();
			}
			if(results.size() > 0) {
				return results;
			}
	        
	        //result = formatResultSetToArrayList(resSet);
	        
	        //reset the static query string to its original value
            //parQ.setQuerySQL(query);
	        
	        return null;
	        
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closeResultSet(resSetII);
			closePreparedStatement(prepStat);
			closePreparedStatement(prepStatII);
			closeConnection(conn);
		}
	}
	
	private String assembleBrowseModelsQ(int columnIndex, String query,
			String defaultOrder, boolean ascending) {
		
		String queryString = null;
		
		if (columnIndex == -1) {
			queryString = query;// + defaultOrder;
		}
		else {
			//do nothing at the moment - only sorting on one column (accession id)
		}
		
		return queryString;
	}
	
	@Override
	public ArrayList<String[]> browseUsersModels(int columnIndex,
			boolean ascending, int offset, int num, User user) {
		
            
                num = num-offset+1;
                if (num < 0) num = 5;
            
		if(user == null || user.getUserName() == null) {
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		PreparedStatement prepStatII = null;
		ResultSet resSetII = null;
        
        //ParamQuery parQ = new ParamQuery("","");
        
        String queryString = this.assembleBrowseModelsQ(columnIndex, MySQLQuery.ALL_USER_MODELS, MySQLQuery.ORDER_BY_MODEL_NAME, ascending);
        //parQ.setQuerySQL(queryString);
        
        //System.out.println("Offset: "+offset+" num:"+num);
        try {
			 
			prepStat = conn.prepareStatement(queryString);//parQ.getPrepStat(conn);
			prepStat.setString(1, user.getUserName());
			prepStat.setInt(2, offset-1);
	        prepStat.setInt(3, num);
	        resSet = prepStat.executeQuery();
	        
	        ResultSetMetaData resSetData = resSet.getMetaData();
			int columnCount = resSetData.getColumnCount() -1; //subtract 1 because don't need to count "ROWNUMBER" col
			
			//create ArrayList to store each row of results in
			ArrayList<String[]> results = new ArrayList<>();
                        ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_DESCRIPTION");
			 
			prepStatII = parQ.getPrepStat(conn);
			
			//set the main values in an array
			while (resSet.next()) {
				String[] columns = new String[columnCount+1];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSet.getString(i + 1);
				}
				
				//need to get the description for each model
				prepStatII.setString(1, resSet.getString(1));
				resSetII = prepStatII.executeQuery();
				while(resSetII.next()) {
					columns[columnCount] = resSetII.getString(1);
				}
				results.add(columns);
				prepStatII.clearParameters();
			}
			if(results.size() > 0) {
				return results;
			}
			
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closeResultSet(resSetII);
			closePreparedStatement(prepStat);
			closePreparedStatement(prepStatII);
			closeConnection(conn);
		}
	}

	//@Override
	//public StringReader getModelAsInputStream(String accession, String version) {
	public StringReader getModelAsReader(String accession, String version) {
		
		if(accession == null || accession.length() > DataConstants.MAX_ACCESSION_LENGTH){
			return null;
		}
		boolean useLatestVersion = false;
		if(version == null) {
			useLatestVersion = true;
		}
		else {
			int versionNo;
			try {
				versionNo = Integer.parseInt(version);
				if(versionNo < DataConstants.MIN_VERSION_NO || versionNo > DataConstants.MAX_VERSION_NO){
					return null;
				}
			}
			catch(Exception e) {
				return null;
			}
		}
		
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		ParamQuery parQ = null;
		
		
		try {
			
			if(useLatestVersion){
				//query to find the current maximum model version number
	        	parQ = MySQLQuery.getParamQuery("MAX_MODEL_VERSION");
	        	
	        	prepStat = parQ.getPrepStat(conn);
	        	
	        	prepStat.setString(1, accession);
	        	resSet = prepStat.executeQuery();
	        	
	        	//if previous versions of the model already exist
	        	if(resSet.first()) {
	        		version = resSet.getString(1);
	        	}
	        	else {
	        		return null;
	        	}
			}
			
			parQ = MySQLQuery.getParamQuery("MODEL_FILE");
			 
			prepStat = parQ.getPrepStat(conn);
	        prepStat.setString(1, accession);
	        prepStat.setString(2, version);
	        
	        resSet = prepStat.executeQuery();
	        
	        while(resSet.next()) {
	        	String result = resSet.getString(1);
	        	//byte [] bytes = result.getBytes();
	        	//ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	        	return new StringReader(result);//bais;
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		
		return null;
	}
	
	/**
	 * retrieves a specified xml model as a string using the models id and version no.
	 * to retrieve the correct model and version respectively. If no version is specified,
	 * the latest version of the model is retrieved
	 */
	/*@Override
	public String getModelAsString(String accession, String version){
		
		if(accession == null || accession.length() > DataConstants.MAX_ACCESSION_LENGTH){
			return null;
		}
		boolean useLatestVersion = false;
		if(version == null) {
			useLatestVersion = true;
		}
		else {
			int versionNo;
			try {
				versionNo = Integer.parseInt(version);
				if(versionNo < DataConstants.MIN_VERSION_NO || versionNo > DataConstants.MAX_VERSION_NO){
					return null;
				}
			}
			catch(Exception e) {
				return null;
			}
		}
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
        ResultSet resSet = null;
        ParamQuery parQ = null;
        
        try {
			conn.setAutoCommit(false);
			
			if(useLatestVersion){
				//query to find the current maximum model version number
	        	parQ = MySQLQuery.getParamQuery("MAX_MODEL_VERSION");
	        	
	        	prepStat = parQ.getPrepStat(conn);
	        	
	        	prepStat.setString(1, accession);
	        	resSet = prepStat.executeQuery();
	        	
	        	//if previous versions of the model already exist
	        	if(resSet.first()) {
	        		version = resSet.getString(1);
	        	}
	        	else {
	        		return null;
	        	}
			}
			
			parQ = MySQLQuery.getParamQuery("MODEL_FILE");
			 
			prepStat = parQ.getPrepStat(conn);
	        prepStat.setString(1, accession);
	        prepStat.setString(2, version);
	        
	        resSet = prepStat.executeQuery();
	        
	        while(resSet.next()) {
	        	return resSet.getString(1);
	        }
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		
		return null;
	}*/
        
	@Override
	public String getModelAsString(String accession, int versionId){
		
		if(accession == null || accession.length() > DataConstants.MAX_ACCESSION_LENGTH){
			return null;
		}
        
        Connection conn = getConnection();
        try {

			//conn.setAutoCommit(false);
                        

                        ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_FILE");

			 
			PreparedStatement prepStat = parQ.getPrepStat(conn);
                        try {

                        prepStat.setString(1, accession);
                        prepStat.setInt(2, versionId);
	        
                            ResultSet resSet = prepStat.executeQuery();
                            try {
                                if (resSet.first()) return resSet.getString(1);
                                else throw new MissingModel(accession, versionId);
                            } finally {
                                closeResultSet(resSet);
                            }
                        } finally {
                            closePreparedStatement(prepStat);
                        }
	        
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeConnection(conn);
		}
		
	}
        
	
	@Override
	public ArrayList<String[]> getModelComments(String accession) {
		
		//make sure the accession is valid
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_COMMENTS");
		
		try {
			 
			
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			
			resSet = prepStat.executeQuery();
			
			ArrayList<String[]> comments = formatResultSetToArrayList(resSet);
			return comments;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	
	@Override
	public ModelData getModelData(String accession, String version, User user) {
		
		//make sure the accession is valid
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		ParamQuery parQ = null;
		ModelData model = null;
		
		try {
			
			//get the maximum version of this model
			int maxVersion;
			parQ = MySQLQuery.getParamQuery("MAX_MODEL_VERSION");
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			resSet = prepStat.executeQuery();
			//only continue if a max version is found
			if(resSet.first() && resSet.getInt(1) > 0) {
				maxVersion = resSet.getInt(1);
				model = new ModelData();
				model.setMaxVersion(maxVersion);
			}
			else {
				return null;
			}
			
			//if the user hasn't specified a version, retrieve the info for the latest version
			if(version == null || version.trim().equals("")){
				version = String.valueOf(maxVersion);
			}
			else {
				//make sure the version value specified by the user is valid
				int versionNo;
				try {
					versionNo = Integer.parseInt(version);
					if(versionNo > DataConstants.MAX_VERSION_NO || versionNo < DataConstants.MIN_VERSION_NO) {
						return null;
					}
				}
				catch (NumberFormatException e){
					return null;
				}
			}
			
			
			
			//get the rest of the version data for this model
			parQ = MySQLQuery.getParamQuery("MODEL_VERSION_DATA");
			 
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			prepStat.setString(2, version);
			
			resSet = prepStat.executeQuery();
			
			while(resSet.next()) {
				model.setAccession(resSet.getString(1));
				model.setName(resSet.getString(2));
				model.setFormat(resSet.getString(3));
				model.setVersion(resSet.getString(4));
				model.setSubmissionDate(resSet.getString(5));
				//model.setDescription(resSet.getString(6));
				model.setVersionComment(resSet.getString(6));
				if(resSet.getInt(7) == 0) {
					model.setPrivate(true);
				}
				else {
					model.setPrivate(false);
				}
			}
			
			//get all the versions for this model and store in arraylist
			parQ = MySQLQuery.getParamQuery("MODEL_VERSIONS");
			 
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			resSet = prepStat.executeQuery();
			ArrayList<Integer> versions = new ArrayList<>();
			while(resSet.next()) {
				versions.add(resSet.getInt(1));
			}
			model.setVersions(versions);
			
			if(model != null) {
				parQ = MySQLQuery.getParamQuery("MODEL_SUBMITTER");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				
				resSet = prepStat.executeQuery();
				
				resSet.next(); 
				User submitter = new User();
				submitter.setGiven(resSet.getString(1));
				submitter.setFamily(resSet.getString(2));
				submitter.setUserName(resSet.getString(3));
				submitter.setEmail(resSet.getString(4));
				submitter.setOrganisation(resSet.getString(5));
				model.setSubmitter(submitter);
				
				//get the owner of the model
				parQ = MySQLQuery.getParamQuery("MODEL_OWNER");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				
				resSet = prepStat.executeQuery();
				
				resSet.next(); 
				User owner = new User();
				owner.setGiven(resSet.getString(1));
				owner.setFamily(resSet.getString(2));
				owner.setUserName(resSet.getString(3));
				owner.setEmail(resSet.getString(4));
				owner.setOrganisation(resSet.getString(5));
				owner.setOid(resSet.getInt(6));
				//find the supervisor of the model owner (if any)
				parQ = MySQLQuery.getParamQuery("SUPERVISOR_OF_MODEL_OWNER");
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				
				resSet = prepStat.executeQuery();
				
				if(resSet.first()) {
					User supervisor = new User();
					supervisor.setOid(resSet.getInt(1));
					owner.setSupervisor(supervisor);
				}
				
				model.setOwner(owner);
				
				//TODO
				parQ = MySQLQuery.getParamQuery("MODEL_PUBLICATIONS");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				
				resSet = prepStat.executeQuery();
				ArrayList<Publication> publications = new ArrayList<>();
				while(resSet.next()) {
					Publication pub = new Publication();
					pub.setAuthors(resSet.getString(1));
					pub.setPeriodicalName(resSet.getString(2));
					pub.setYear(resSet.getString(3));
					pub.setTitle(resSet.getString(4));
					pub.setAbstract(resSet.getString(5));
					pub.setSecondaryAuthors(resSet.getString(6));
					pub.setSecondaryTitle(resSet.getString(7));
					pub.setPublisher(resSet.getString(8));
					pub.setPages(resSet.getString(9));
					pub.setIsbn(resSet.getString(10));
					pub.setUrl(resSet.getString(11));
					pub.setReferenceType(resSet.getString(12));
					
					publications.add(pub);
					
				}
				model.setPublications(publications);
				//TODO - ADD thumb nail support
				parQ = MySQLQuery.getParamQuery("MODEL_VERSION_IMAGES");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				prepStat.setString(2, version);
				
				resSet = prepStat.executeQuery();
				
				ArrayList<String []> images = formatResultSetToArrayList(resSet);
                                images = convertToImgFilePath(images,false);
				/*while(resSet.next()) {
					DataFile img = new DataFile();
					img.setDataFileFileName(resSet.getString(1));
					img.setDescription(resSet.getString(2));
					images.add(img);
				}*/
				model.setImages(images);
				
				parQ = MySQLQuery.getParamQuery("MODEL_VERSION_SUPP_FILES");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				prepStat.setString(2, version);
				
				resSet = prepStat.executeQuery();
				
				ArrayList<String []> supFiles = formatResultSetToArrayList(resSet);
                                supFiles = convertToDataFilePath(supFiles,false);
				
				/*ArrayList<DataFile> supFiles = new ArrayList<DataFile>();
				while(resSet.next()) {
					DataFile supFile = new DataFile();
					supFile.setDataFileFileName(resSet.getString(1));
					supFile.setDescription(resSet.getString(2));
					supFiles.add(supFile);
				}*/
				model.setSupplementaryFiles(supFiles);
				
				//Get the model id
				parQ = MySQLQuery.getParamQuery("MDL_ID_FROM_ACCESSION");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				String oid = "-1";
				resSet = prepStat.executeQuery();
				if(resSet.next())
					oid = resSet.getString(1);
				
				//Add the current model read (access) permissions in here
				//Only those that the user has permission to see
				List<GroupAttributes> list = new ArrayList<>();
				if(user != null){
					parQ = MySQLQuery.getParamQuery("GROUP_NAMES_WITH_MODEL_PERMISSION");
					 
					prepStat = parQ.getPrepStat(conn);
					prepStat.setString(1, oid);
					prepStat.setInt(2, user.getOid());
					
					resSet = prepStat.executeQuery();
					
					while(resSet.next()){
						GroupAttributes ga = new GroupAttributes();
						ga.setAccess(false);
						ga.setEdit(false);
						ga.setName(resSet.getString(1));
						if(resSet.getString(2) != null){
							String per = resSet.getString(2);
							if(per.compareTo("read")==0 || per.compareTo("write")==0)
								ga.setAccess(true);
							if(per.compareTo("write") == 0)
								ga.setEdit(true);
						}
	
						list.add(ga);
					}
				}
				model.setGroupAttributes(list);
				
				
				parQ = MySQLQuery.getParamQuery("MODEL_ATTRIBUTES");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				
				resSet = prepStat.executeQuery();
				
				ArrayList<Attribute> attributes = new ArrayList<>();
				while(resSet.next()){
					
					if(resSet.getString(1).equals("Description")) {
						model.setDescription(resSet.getString(2));
					}
					else {
						Attribute att = new Attribute();
						att.setName(resSet.getString(1));
						att.setValue(resSet.getString(2));
						attributes.add(att);
					}
				}
				model.setAttributes(attributes);
				
				parQ = MySQLQuery.getParamQuery("MODEL_TRANSFORM_OPTIONS");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				prepStat.setString(2, version);
				
				resSet = prepStat.executeQuery();
				
				ArrayList<String[]> transformOptions = formatResultSetToArrayList(resSet);
				model.setTransformOptions(transformOptions);
			}
			return model;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		
	}

	@Override
	public String getModelNameFromAccession(String accession) {
		if(accession == null || accession.trim().equals("")) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_NAME_FROM_ACCESSION");
		
		ResultSet resSet = null;
        PreparedStatement prepStat = null;
        
        try {
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			
			resSet = prepStat.executeQuery();
			if(resSet.first()) {
				return resSet.getString(1);
			}
			else {
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		} finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public VersionSubmission getModelDataForEditVersionDisplay(String accession, String version) {
		
		//make sure the accession no. is valid
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		VersionSubmission model = null;
		
		ParamQuery parQ = null;
		
		try {
			//if the user hasn't specified a version no, get the latest version for the model with the
			//specified accession
			if(version == null || version.trim().equals("")){
				parQ = MySQLQuery.getParamQuery("MAX_MODEL_VERSION");
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				resSet = prepStat.executeQuery();
				if(resSet.first()) {
					version = String.valueOf(resSet.getInt(1));
				}
				else {
					return null;
				}
			}
			//otherwise, make sure the version provided is valid
			else {
				int versionNo;
				try {
					versionNo = Integer.parseInt(version);
					if(versionNo > DataConstants.MAX_VERSION_NO || versionNo < DataConstants.MIN_VERSION_NO) {
						return null;
					}
				}
				catch (NumberFormatException e){
					return null;
				}
			}
			
			//get the relevant version data
			parQ = MySQLQuery.getParamQuery("MODEL_VERSION_DATA_FOR_EDIT");
			 
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			prepStat.setString(2, version);
			
			resSet = prepStat.executeQuery();
			
			//if data exists, store in 'ModelData' object
			while(resSet.next()) {
				model = new VersionSubmission();
				model.setAccession(accession);
				model.setVersion(version);
				model.setName(resSet.getString(1));
				model.setFormat(resSet.getString(2));
				//model.setSubmissionDate(resSet.getString(3));
				model.setComments(resSet.getString(4));
			}
			
			return model;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	
	//TODO
	@Override
	public ModelSubmission getModelDataForEditModelDisplay(String accession){
		
		//make sure the accession no. is valid
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		ModelSubmission model = null;
		
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_NAME_FROM_ACCESSION");
		
		try {
			//get the name of the model 
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			
			resSet = prepStat.executeQuery();
			
			//if data exists, store it
			if(resSet.first()) {
				model = new ModelSubmission();
				model.setAccession(accession);
				model.setName(resSet.getString(1));
				
				int isPrivate = resSet.getInt(2);
				
				if(isPrivate == 0) {
					model.setPrivate(true);
					//if the model is set to private then get the ids of 
					//user groups who can still access the model
					parQ = MySQLQuery.getParamQuery("GROUP_IDS_WITH_ACCESS_TO_MODEL");
					prepStat = parQ.getPrepStat(conn);
					prepStat.setString(1, accession);
					resSet = prepStat.executeQuery();
                                        
                                        List<Integer> accessGroups = new ArrayList<>();
                                        while(resSet.next()) {
                                                accessGroups.add(resSet.getInt(1));
                                        }
                                        int[] groups = new int[accessGroups.size()];
                                        for (int i =0;i<groups.length;i++) groups[i] = accessGroups.get(i);
                                        
                                        model.setAccessGroupIds(groups);

                                        /*
					//check a result exists
					if(resSet.last()) {
						int [] accessGroups = new int[resSet.getRow()];
						//reset the cursor
						resSet.beforeFirst();
						int index = 0;
						while(resSet.next()) {
							accessGroups[index] = resSet.getInt(1);
							index++;
						}
						
						model.setAccessGroupIds(accessGroups);
						
					} */
				}
				else {
					model.setPrivate(false);
				}
				
				//get attributes for the model
				parQ = MySQLQuery.getParamQuery("MODEL_ATTRIBUTES");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);
				
				resSet = prepStat.executeQuery();
				
				ArrayList<Attribute> attributes = new ArrayList<>();
				while(resSet.next()){
					
					if(resSet.getString(1).equals("Description")) {
						model.setDescription(resSet.getString(2));
					}
					else {
						Attribute att = new Attribute();
						att.setName(resSet.getString(1));
						att.setValue(resSet.getString(2));
						attributes.add(att);
					}
				}
				model.setAttributes(attributes);
			}
			return model;
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		} finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public ModelData getModelDataForNewVersionDisplay(String accession) {
		//make sure the accession no is valid
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		ModelData model = null;
		
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_NAME_FROM_ACCESSION");
		
		try {
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			
			resSet = prepStat.executeQuery();
			if(resSet.first()) {
				model = new ModelData();
				model.setAccession(accession);
				model.setName(resSet.getString(1));
				
				parQ = MySQLQuery.getParamQuery("MODEL_IMAGES");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);

				resSet = prepStat.executeQuery();

				ArrayList<String []> images = formatResultSetToArrayList(resSet);
                                images = convertToImgFilePath(images,true);
				/*while(resSet.next()) {
					DataFile img = new DataFile();
					img.setId(resSet.getString(1));
					img.setDataFileFileName(resSet.getString(2));
					img.setDescription(resSet.getString(3));
					images.add(img);
				}*/
				model.setImages(images);

				parQ = MySQLQuery.getParamQuery("MODEL_SUPP_FILES");
				 
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, accession);

				resSet = prepStat.executeQuery();

				ArrayList<String []> supFiles = formatResultSetToArrayList(resSet);
                                supFiles = convertToDataFilePath(supFiles,true);
				/*ArrayList<DataFile> supFiles = new ArrayList<DataFile>();
				while(resSet.next()) {
					DataFile supFile = new DataFile();
					supFile.setId(resSet.getString(1));
					supFile.setDataFileFileName(resSet.getString(2));
					supFile.setDescription(resSet.getString(3));
					supFiles.add(supFile);
				}*/
				model.setSupplementaryFiles(supFiles);
			}
			return model;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		} finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	/**
	 * retrieves the ids of models with a specific name 
	 */
	@Override
	public ArrayList<String> getModelIdsByName(String name) {
		
		if(name == null || name.length() > DataConstants.MODEL_NAME) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_ACCESSION_FROM_NAME");
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		try {
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, name);
			
			resSet = prepStat.executeQuery();
                        ArrayList <String> results = new ArrayList<>();
                        while(resSet.next()) {
                                results.add(resSet.getString(1));
                        }
                        return (results.isEmpty() ? null : results);
                        
                        /*
			if(resSet.first()) {
				ArrayList <String> results = new ArrayList<>();
				
				resSet.beforeFirst();
				while(resSet.next()) {
					results.add(resSet.getString(1));
				}
				return results;
			}
			else {
				return null;
			}*/
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException();
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}

	@Override
	/**
	 * get a list of ids of all models in the database
	 */
	public ArrayList<String> getAllModelIds() {
		
		Connection conn = getConnection();
		
		Statement stmt = null;
        ResultSet resSet = null;
        
        try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			resSet = stmt.executeQuery(MySQLQuery.ALL_MODEL_IDS);
			
			if(resSet.first()) {
				ArrayList <String> results = new ArrayList<>();
				
				resSet.beforeFirst();
				while(resSet.next()) {
					results.add(resSet.getString(1));
				}
				return results;
			}
			else {
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException();
		}
		finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closeConnection(conn);
		}
	}
	
	/**
	 * get the model name from the accession id
	 */
	@Override
	public String getModelName(String accession) {
		//make sure the accession no is valid
		if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_NAME_FROM_ACCESSION");
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		try {
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accession);
			
			resSet = prepStat.executeQuery();
			if(resSet.first()) {
				return resSet.getString(1);
			}
			else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}

	@Override
	public ArrayList<String> getModelIdsByFormat(String format) {
		
		if(format == null || format.length() > DataConstants.FORMAT_NAME) {
			return null;
		}
		
		Connection conn = getConnection();
		
		ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_ACCESSION_FROM_LATEST_VERSION_FORMAT");
		PreparedStatement prepStat = null;
		ResultSet resSet = null;
		
		try {
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, format);
			
			resSet = prepStat.executeQuery();
                        ArrayList <String> results = new ArrayList<>();
                        while(resSet.next()) {
                                results.add(resSet.getString(1));
                        }
                        return (results.isEmpty() ? null : results);
                        
                        /*
			if(resSet.first()) {
				ArrayList <String> results = new ArrayList<>();
				
				resSet.beforeFirst();
				while(resSet.next()) {
					results.add(resSet.getString(1));
				}
				return results;
			}
			else {
				return null;
			}*/
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}

	@Override
	public List<RefGroup> getUserGroups() {
		
		Connection conn = getConnection();
		Statement stmt = null;
        ResultSet resSet = null;
		
        try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			resSet = stmt.executeQuery(MySQLQuery.ALL_USER_GROUPS);
			List<RefGroup> grpList = new ArrayList<>();
			while(resSet.next()) {
				//set the id, name and desc in the group object
				RefGroup grp = new RefGroup(resSet.getInt(1), resSet.getString(2), resSet.getString(3));
				grpList.add(grp);
			}
			
			return grpList;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closeConnection(conn);
		}
	}

    protected ArrayList<String[]> convertToDataFilePath(ArrayList<String[]> supFiles,boolean withId) {
        if (supFiles == null) return null;
        ArrayList<String[]> res = new ArrayList<>(supFiles.size());
        
        String pref = Constants.SUBMITTED_DATA_FILES_URL;
        int expLength = withId ? 4 : 3;
        for (String[] desc : supFiles) {
            if (desc.length != expLength) {
                System.out.println("Wrong number of desc parts in: "+Arrays.toString(desc));
                continue;
            }
            String id = withId ? desc[0] : "";
            String path = withId ? desc[1] : desc[0];
            String file = withId ? desc[2] : desc[1];
            String name = withId ? desc[3] : desc[2];
            String link = pref+path+file;
            if (withId)
                res.add(new String[]{id,link,name});
            else
                res.add(new String[]{link,name});
        }
        return res;
    }

    private ArrayList<String[]> convertToImgFilePath(ArrayList<String[]> images,boolean withId) {
        if (images == null) return null;
        ArrayList<String[]> res = new ArrayList<>(images.size());
        
        String pref = Constants.SUBMITTED_DATA_FILES_URL;
        String thumbP = Constants.THUMBNAILS_URL_SUFFIX;
        int expLength = withId ? 4 : 3;
        for (String[] desc : images) {
            if (desc.length != expLength) {
                System.out.println("Wrong number of desc parts in: "+Arrays.toString(desc));
                continue;
            }
            String id = withId ? desc[0] : "";
            String path = withId ? desc[1] : desc[0];
            String file = withId ? desc[2] : desc[1];
            String name = withId ? desc[3] : desc[2];
            
            String link = pref+path+file;
            String thumb = pref+path+thumbP+file;
            if (withId)
                res.add(new String[]{id,link,thumb,name});
            else 
                res.add(new String[]{link,thumb,name});
        }
        return res;
    }

    @Override
    public int getVersionId(String accession, String version) {
        
        //make sure the accession is valid
        if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
                throw new MissingModel(accession,version);
        }

        Integer versionNr = null;
        if (version != null) {
            try {
                versionNr = Integer.parseInt(version);
                if(versionNr < DataConstants.MIN_VERSION_NO) versionNr = DataConstants.MIN_VERSION_NO;
                if(versionNr > DataConstants.MAX_VERSION_NO) versionNr = DataConstants.MAX_VERSION_NO;
            } catch (NumberFormatException e) {};
        }
        
        if (versionNr != null) return versionNr;

        Connection conn = getConnection();
        try {

            if (versionNr == null) //get the max
            {
                ParamQuery parQ = MySQLQuery.getParamQuery("MAX_MODEL_VERSION");

                PreparedStatement prepStat = parQ.getPrepStat(conn); 
                try {

                    prepStat.setString(1, accession);
                    ResultSet resSet = prepStat.executeQuery();
                    try {
                        if(resSet.first()) versionNr = resSet.getInt(1);
                        else throw new MissingModel(accession,version);
                    } finally {
                        closeResultSet(resSet);
                    }                                
                } finally {
                    closePreparedStatement(prepStat);
                }
            }
            
            if (versionNr == null) throw new MissingModel(accession,version);
            return versionNr;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem getting version: "+e.getMessage(),e);
        }
        finally {
            closeConnection(conn);
        }
			
    }

    @Override
    public long getFormatId(String accession, int versionId) {
        
        Connection conn = getConnection();
        try {

                ParamQuery parQ = MySQLQuery.getParamQuery(MySQLQuery.GET_FORMAT_ID_FROM_ACCESSION_VERSION);

                PreparedStatement prepStat = parQ.getPrepStat(conn); 
                try {

                    prepStat.setString(1, accession);
                    prepStat.setInt(2, versionId);
                    ResultSet resSet = prepStat.executeQuery();
                    try {
                        if(resSet.first()) return resSet.getLong(1);
                        else throw new MissingModel(accession,versionId);
                    } finally {
                        closeResultSet(resSet);
                    }                                
                } finally {
                    closePreparedStatement(prepStat);
                }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem getting format nr: "+e.getMessage(),e);
        }
        finally {
            closeConnection(conn);
        }
    }

    @Override
    public List<Publication> getModelPublications(String accession) {
        
        if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
                throw new MissingModel(accession,"");
        }
        
        Connection conn = getConnection();
        try {

                ParamQuery parQ = MySQLQuery.getParamQuery(MySQLQuery.MODEL_PUBLICATIONS);
                 

                PreparedStatement prepStat = parQ.getPrepStat(conn); 
                try {

                    prepStat.setString(1, accession);
                    ResultSet resSet = prepStat.executeQuery();
                    try {
                        ArrayList<Publication> publications = new ArrayList<>();
                        while(resSet.next()) {
                                Publication pub = assemblePublication(resSet);
                                publications.add(pub);

                        }
                        return publications;
                         
                    } finally {
                        closeResultSet(resSet);
                    }                                
                } finally {
                    closePreparedStatement(prepStat);
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem getting publications: "+e.getMessage(),e);
        }
        finally {
            closeConnection(conn);
        }
    }

    @Override
    public Publication getPublication(String accession, long pubOID) {
        if(accession == null || accession.trim().equals("") || accession.length() > DataConstants.MAX_ACCESSION_LENGTH) {
                throw new MissingModel(accession,"");
        }
        
        Connection conn = getConnection();
        try {

                ParamQuery parQ = MySQLQuery.getParamQuery(MySQLQuery.MODEL_PUBLICATION);
                 

                PreparedStatement prepStat = parQ.getPrepStat(conn); 
                try {

                    prepStat.setString(1, accession);
                    prepStat.setLong(2, pubOID);
                    ResultSet resSet = prepStat.executeQuery();
                    try {
                        if(!resSet.first()) throw new MissingPublication(accession,pubOID);
                        
                        Publication pub = assemblePublication(resSet);
                        return pub;
                         
                    } finally {
                        closeResultSet(resSet);
                    }                                
                } finally {
                    closePreparedStatement(prepStat);
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem getting publications: "+e.getMessage(),e);
        }
        finally {
            closeConnection(conn);
        }
    }

    private Publication assemblePublication(ResultSet resSet) throws SQLException {
            Publication pub = new Publication();
            pub.setAuthors(resSet.getString(1));
            pub.setPeriodicalName(resSet.getString(2));
            pub.setYear(resSet.getString(3));
            pub.setTitle(resSet.getString(4));
            pub.setAbstract(resSet.getString(5));
            pub.setSecondaryAuthors(resSet.getString(6));
            pub.setSecondaryTitle(resSet.getString(7));
            pub.setPublisher(resSet.getString(8));
            pub.setPages(resSet.getString(9));
            pub.setIsbn(resSet.getString(10));
            pub.setUrl(resSet.getString(11));
            pub.setReferenceType(resSet.getString(12));
            pub.setPubOID(resSet.getLong(13));  
            return pub;
    }
    
    
    public static class MissingModel extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public MissingModel(String accession,String version) {
            super("Missing model: "+accession+"; V:"+version);
        }
        public MissingModel(String accession,int version) {
            super("Missing model: "+accession+"; V:"+version);
        }
    }
    
    public static class MissingPublication extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public MissingPublication(String accession,long pub) {
            super("Missing publication in model: "+accession+"; P:"+pub);
        }
    }
    

}
