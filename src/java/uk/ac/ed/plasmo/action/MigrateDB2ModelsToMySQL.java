/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.plasmo.action;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import uk.ac.ed.plasmo.db.RelationalDBDAO;

/**
 *
 * @author tzielins
 */
public class MigrateDB2ModelsToMySQL extends ActionSupport {
    
    String msg;
    int db2Size;
    int mysqlSize;
    int migrated;
    
    
    private DataSource db2DS;
    //private DataSource mysqlDS;
    //private Connection MySQLWrite;
    List<Throwable> errors = new ArrayList<Throwable>();
    
    final String ID_QUERY = "SELECT VSN_MODEL_FK, MDL_ACCESSION_ID, VSN_VERSION FROM MOD_MODEL_VERSION JOIN MOD_MODEL ON VSN_MODEL_FK = MDL_OID ORDER BY VSN_MODEL_FK";
    final String FILE_QUERY = "SELECT VSN_MODEL_FILE FROM MOD_MODEL_VERSION WHERE VSN_MODEL_FK = ? AND VSN_VERSION = ?";
    final String FILE_UPDATE ="UPDATE MOD_MODEL_VERSION SET VSN_MODEL_FILE=? WHERE VSN_MODEL_FK = ? AND VSN_VERSION = ?";
    
    final String DB2_URl ="jdbc:db2://gengy.inf.ed.ac.uk:50001/MODELS:currentSchema=DB2INST;";
    final String DB2_USER ="db2inst";
    final String DB2_PASS ="password";
    
    
    @Override
    public String execute() {
        
        /*if (true) {
            System.out.println("Migration action is disabled");
            addActionError("Migration action is disabled");
            return ERROR;
        }*/
        try {
        Set<ModelId> mysqlModelsIds = getMySQLModelsIds();
        Set<ModelId> db2ModelsIds = getDB2ModelsIds();
        
        db2Size = db2ModelsIds.size();
        mysqlSize = mysqlModelsIds.size();
        
        migrated = 0;
        
        for (ModelId myId : mysqlModelsIds) {
            if (!db2ModelsIds.contains(myId)) {
                System.out.println("Ignoring: "+myId+" as not in DB2");
                continue;
            }
            if (migrateModel(myId))
                migrated++;
        }
        
        if (!errors.isEmpty()) {
            msg = "There were: "+errors.size()+" errors, check log for details";
            addActionError(msg);
        } else {
            msg = "Migration OK";
        }
        Set<ModelId> notMigrated = new HashSet<ModelId>(db2ModelsIds);
        notMigrated.removeAll(mysqlModelsIds);
        
        
        if (!notMigrated.isEmpty()) {
            String msg1 = "Not all models present in local DB: "+notMigrated.size();
            System.out.println(msg1);
            System.out.println(notMigrated);
            addActionError(msg1);
            msg+="; "+msg1;
        }
        
        for (Throwable e : errors) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            System.out.println("\n");
        }
        
        if (!errors.isEmpty()) {
            return ERROR;
        } else {        
            return SUCCESS;
        }
        } catch (SQLException e) {
            msg = "Migration problem: "+e.getMessage();
            System.out.println(msg);
            e.printStackTrace(System.out);
            addActionError(msg);
            return ERROR;
        } finally {
        }
    }

    public int getMigrated() {
        return migrated;
    }

    
    public String getMsg() {
        return msg;
    }

    public int getDb2Size() {
        return db2Size;
    }

    public int getMysqlSize() {
        return mysqlSize;
    }

    private Set<ModelId> getMySQLModelsIds() throws SQLException {
        
         
        Connection conn = getMySQLConnection();
        try {
            return getModelsIds(conn);             
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Could not close the connection: "+e.getMessage());
            }
        }
        
    }
    
    private Set<ModelId> getDB2ModelsIds() throws SQLException {
        
        Connection conn = getDB2Connection();
        try {
            return getModelsIds(conn);             
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Could not close the connection: "+e.getMessage());
            }
        }
    }

    private Connection getMySQLConnection() throws SQLException {
        return RelationalDBDAO.getConnection();
        /*if (mysqlDS == null) {
            mysqlDS = initMySQLDS();
        }
        return mysqlDS.getConnection();
        //*/
    }
    
    private Connection getDB2Connection() throws SQLException {
        if (db2DS == null) {
            db2DS = initDB2DS();
        }
        return db2DS.getConnection();
    }
    

    private Set<ModelId> getModelsIds(Connection conn) throws SQLException {

       Set<ModelId> ids = new HashSet<ModelId>();
       
       Statement stmnt = conn.createStatement();
       ResultSet results = stmnt.executeQuery(ID_QUERY);
       if (results.getMetaData().getColumnCount() != 3)
            throw new SQLException("Was expecting 3 columns in the query results: "+ID_QUERY);
       //results.beforeFirst();
       while(results.next()) {
           ModelId id = new ModelId(results.getLong(1),results.getString(2),results.getInt(3));
           ids.add(id);
       }

       return ids;
    }

    private DataSource initDB2DS() {
        
	    BasicDataSource bs = new BasicDataSource();
	    bs.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
	    bs.setUrl(DB2_URl);
	    bs.setUsername(DB2_USER);
	    bs.setPassword(DB2_PASS);
            bs.setInitialSize(2);
	    bs.setMaxTotal(30);
	    bs.setMaxIdle(8);
            bs.setMaxWaitMillis(10000);
            bs.setRemoveAbandonedOnBorrow(true);
            bs.setRemoveAbandonedTimeout(600);
            bs.setTestOnBorrow(true);
            bs.setValidationQuery("SELECT COUNT(*) FROM MOD_MODEL");
            bs.setValidationQueryTimeout(1000);
            
            return bs;
    }
    
    /*private DataSource initMySQLDS() {
        
	    BasicDataSource bs = new BasicDataSource();
	    bs.setDriverClassName("com.mysql.jdbc.Driver");
	    bs.setUrl(MYSQL_URL);
	    bs.setUsername(MYSQL_USER);
	    bs.setPassword(MYSQL_PASS);
            bs.setInitialSize(2);
	    bs.setMaxActive(30);
	    bs.setMaxIdle(8);
	    bs.setMaxWait(10000);
            bs.setRemoveAbandoned(true);
            bs.setRemoveAbandonedTimeout(600);
            bs.setTestOnBorrow(true);
            bs.setValidationQuery("SELECT 1");
            bs.setValidationQueryTimeout(1000);
            
            return bs;
    }
    //*/


    private boolean migrateModel(ModelId myId)  {
        
        try {
            Connection conn = getMySQLConnection();
            try {
                String localModel = getModelFile(conn,myId);
                if (localModel != null && !localModel.isEmpty()) {
                    if (!localModel.startsWith("com.ibm.db2")) {
                        System.out.println("Ignoring: "+myId+" as alread contains a model");
                        return false;
                    }
                }
            } finally {
                conn.close();
            }

            String model;
            conn = getDB2Connection();
            try {
                model = getModelFile(conn,myId);
                if (model == null || model.isEmpty()) {
                        System.out.println("Ignoring: "+myId+" as remote model is empty");            
                        return false;
                }
            } finally {
                conn.close();
            }
            if (!updateModelFile(myId,model)) return false;

            conn = getMySQLConnection();
            try {
                String localModel = getModelFile(conn,myId);
                if (!model.equals(localModel)) {
                    System.out.println("WARNING local version of model file for "+myId+" do not match remote one");
                }
            } finally {
                conn.close();
            };
            return true;
        } catch (Throwable e) {
            System.out.println("FAILED: "+myId+" due to error: "+e.getMessage());
            errors.add(new Exception("Error in: "+myId+": "+e.getMessage(),e));
            //errors.add(e);
            return false;
        }
    }

    private String getModelFile(Connection conn,ModelId model) throws SQLException {
        
        PreparedStatement stmt = conn.prepareStatement(FILE_QUERY);
        stmt.setLong(1, model.id);
        stmt.setLong(2, model.version);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            return results.getString(1);
        } else {
            System.out.println("There were no files for model: "+model);
            return null;
        }
        
    }

    private boolean updateModelFile(ModelId id, String modelContent) throws SQLException {
        
        Connection conn = getMySQLConnection();
        conn.setAutoCommit(false);
        try {
            PreparedStatement stmt = conn.prepareStatement(FILE_UPDATE);
            //Reader content = new StringReader(modelContent);
            stmt.setString(1, modelContent);
            stmt.setLong(2, id.id);
            stmt.setLong(3, id.version);
            System.out.println("Updated model "+id);
            int up = stmt.executeUpdate();
            if (up != 1) conn.rollback();
            else conn.commit();
            return (up == 1);
        } catch (Throwable e) {
            conn.rollback();
            System.out.println("Error when updating model: "+id+", "+e.getMessage());
            /*e.printStackTrace(System.out);
            if (e.getCause() != null) {
                System.out.println("Caused: ");
                e.getCause().printStackTrace(System.out);
            }*/
            if (e instanceof SQLException) throw (SQLException)e;
            else throw new SQLException(e);
        }
        finally {
            conn.close();
        }
    }

    /*private Connection getMySQLWriteConnection() throws SQLException {
        if (MySQLWrite == null) {
          
            Properties connectionProps = new Properties();
            connectionProps.put("user", MYSQL_USER);
            connectionProps.put("password", MYSQL_PASS);
            MySQLWrite = DriverManager.getConnection(MYSQL_URL, connectionProps);
        };
        return MySQLWrite;
    }*/

    
    
    static class ModelId {
        long id;
        int version;
        String accession;

        public ModelId(long id, String accession, int version) {
            this.id = id;
            this.version = version;
            this.accession = accession;
        }

        
        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
            hash = 97 * hash + this.version;
            hash = 97 * hash + (this.accession != null ? this.accession.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ModelId other = (ModelId) obj;
            if (this.id != other.id) {
                return false;
            }
            if (this.version != other.version) {
                return false;
            }
            if ((this.accession == null) ? (other.accession != null) : !this.accession.equals(other.accession)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Model["+ id + " " + accession+" V" + version +  ']';
        }

        
    }
}
