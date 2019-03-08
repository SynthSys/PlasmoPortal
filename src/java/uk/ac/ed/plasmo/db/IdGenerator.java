/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.plasmo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closeConnection;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closePreparedStatement;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closeResultSet;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.getConnection;

/**
 *
 * @author tzielins
 */
public class IdGenerator extends RelationalDBDAO{
    
    private static final IdGenerator INSTANCE = new IdGenerator();
    
    final static String MODEL_GENERATOR = "MODEL_GENERATOR";
    final static String IMG_GENERATOR = "IMG_GENERATOR";
    final static String FILE_GENERATOR = "FILE_GENERATOR";
    
    final Map<String,Queue<Long>> idsM;
    //final Map<String,AtomicLong> limits;
    final Map<String,Integer> defSizes;
    
    //final AtomicInteger increament;
    //final AtomicLong base;
    //final AtomicLong limit;
    //final int DEF_SIZE = 10;
    
    protected static IdGenerator getInstance() {
        return INSTANCE;
    }
    
    private IdGenerator() {
        idsM = new HashMap<>();
        defSizes = new HashMap<>();
        
        addGenerator(MODEL_GENERATOR,10);
        addGenerator(IMG_GENERATOR,10);
        addGenerator(FILE_GENERATOR,10);
      
    }
    
    private void addGenerator(String name,int defSize) {
        
        idsM.put(name, new ConcurrentLinkedQueue<Long>());
        //limits.put(name, new AtomicLong(0));
        defSizes.put(name, (defSize));
        //increament = new AtomicInteger(LIMIT);
    }
    

    public long getNextId(String generator) {
        
        Queue<Long> ids = idsM.get(generator);
        Long id = ids.poll();
        while (id == null) {
            loadIds(generator);
            id = ids.poll();
        }
        
        return id;        
    }

    protected synchronized void loadIds(String generator) {
        
        Queue<Long> ids = idsM.get(generator);
        if (!ids.isEmpty()) return;
        
        
        try {
            while (true) {
                long nextBase = getCountValue(generator);
                long nextLimit = nextBase+defSizes.get(generator)-1;
                long nextFree = nextLimit+1;
            
                if (updateCountValue(nextFree,nextBase,generator)) {                
                    //ok so we updated correctly and we can add those values to the ids and quit
                    for (long id = nextBase;id<=nextLimit;id++) ids.add(id);
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Problems with generator "+generator+", "+e.getMessage());
            e.printStackTrace(System.out);
            throw new RuntimeException("Could not refresh id generator "+generator+", due to "+e.getMessage());
        }
    }

    protected long getCountValue(String generator) throws SQLException {
        
            Connection conn = getConnection();


            try {
                    ParamQuery parQ = MySQLQuery.getParamQuery(MySQLQuery.GENERATOR_VALUE);
                    PreparedStatement prepStat = null;
                    
                    try {
                    prepStat = parQ.getPrepStat(conn);
                    prepStat.setString(1, generator);

                    ResultSet resSet = null;
                    try {
                        resSet = prepStat.executeQuery();
                        if(resSet.first()) {
                                return resSet.getLong(1);
                        }
                        else {
                                throw new SQLException("Could not find value for the : "+generator+", please insert the right value into GENERATOR table");
                        }
                    } finally {
                        closeResultSet(resSet);
                    }
                    } finally {
                        closePreparedStatement(prepStat);                        
                    }
            } 
            finally {
                    
                    closeConnection(conn);
            }
        
    }

    protected boolean updateCountValue(long newValue, long oldValue,String generator) throws SQLException {
        
        
            Connection conn = getConnection();
            
            

            try {
                    ParamQuery parQ = MySQLQuery.getParamQuery(MySQLQuery.UPDATE_GENERATOR_VALUE);
                    PreparedStatement prepStat = null;

                    try {
                    conn.setAutoCommit(true);
                    prepStat = parQ.getPrepStat(conn);
                    prepStat.setLong(1, newValue);
                    prepStat.setString(2, generator);
                    prepStat.setLong(3, oldValue);
                    int updated = prepStat.executeUpdate();
                    return updated == 1;
                    } finally {
                        closePreparedStatement(prepStat);                        
                    }
            } 
            finally {
                    closeConnection(conn);
            }
    }
    
    public static long getNextModelId() {
        return getInstance().getNextId(MODEL_GENERATOR);
    }
    
    public static long getNextImageId() {
        return getInstance().getNextId(IMG_GENERATOR);
    }

    public static long getNextFileId() {
        return getInstance().getNextId(FILE_GENERATOR);
    }
    
}
