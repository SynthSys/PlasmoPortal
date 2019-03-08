package uk.ac.ed.plasmo.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code DBUpdate} class contains all SQL used to update/modify the database
 * @author ctindal
 *
 */
public class MySQLUpdate {
	
	final static String DELETE_OLD_INTERIM_MODELS = "DELETE FROM MOD_INTERIM_MODEL WHERE INT_SUBMISSION_DATE < (DATE_SUB(NOW(),INTERVAL 2 DAY))";
	
        final static String GET_MODEL_ID = "GET_MODEL_ID";
        final static String GET_MODEL_ID_QUERY = "SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?";
        
        
	final static String name0 = "NEW_MODEL_FORMAT";
	final static String query0 = "INSERT INTO MOD_MODEL_FORMAT (FMT_FORMAT, FMT_SCHEMA_FILE, FMT_DESCRIPTION, FMT_SUBMITTER_FK) " +
			"VALUES (?, ?, ?, (SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?))";
	
	final static String name1 = "NEW_MODEL";
	final static String query1 = "INSERT INTO MOD_MODEL (MDL_ACCESSION_ID, MDL_NAME, MDL_IS_PUBLIC, MDL_SUBMITTER_FK, MDL_OWNER_FK) " +
					"VALUES (?,?,?,?,?)";// +
					//"(SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?), (SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?))"; 
		//"INSERT INTO MOD_MODEL (MDL_ACCESSION_ID, MDL_MODEL_FILE, MDL_SUBMITTER_FK, MDL_FORMAT_FK) " +
			//"VALUES (?, ?, (SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?), (SELECT FMT_OID FROM MOD_MODEL_FORMAT WHERE FMT_FORMAT = ?))";
	
	final static String name2 = "UPDATE_MODEL_METADATA";
	final static String query2 = "UPDATE MOD_MODEL SET MDL_NAME = ?, MDL_DESCRIPTION = ?, MDL_IS_PUBLIC = 1 WHERE MDL_ACCESSION_ID = ?";
	
	final static String name3 = "ADD_IMAGE";
	final static String query3 = "INSERT INTO MOD_IMAGE (IMG_DESCRIPTION, IMG_URI, IMG_FILENAME, IMG_MODEL_FK) " +
			"VALUES (?, ?, ?, (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?))";
	
	final static String name4 = "SET_MODEL_DELETED";
	final static String query4 = "UPDATE MOD_MODEL SET MDL_IS_DELETED = ? WHERE MDL_ACCESSION_ID = ?";
	
	final static String name5 = "NEW_INTERIM_MODEL";
	final static String query5 = "INSERT INTO MOD_INTERIM_MODEL (INT_OID, INT_MODEL_FILE, INT_FORMAT_FK) " +
			"VALUES (?,?, (SELECT FMT_OID FROM MOD_MODEL_FORMAT WHERE FMT_FORMAT = ?))";
	
	final static String name6 = "REMOVE_INTERIM_MODEL";
	final static String query6 = "DELETE FROM MOD_INTERIM_MODEL WHERE INT_OID = ?";
	
	final static String name7 = "NEW_MODEL_VERSION_1";
	final static String query7 = "INSERT INTO MOD_MODEL_VERSION (VSN_MODEL_FK, VSN_VERSION, VSN_MODEL_FILE, VSN_FORMAT_FK) " +
			"VALUES ((SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?),?," +
			"(SELECT INT_MODEL_FILE FROM MOD_INTERIM_MODEL AS TMP1 WHERE INT_OID = ?)," +
			"(SELECT INT_FORMAT_FK FROM MOD_INTERIM_MODEL AS TMP2 WHERE INT_OID = ?))";
	
	final static String name8 = "ADD_MODEL_ATTRIBUTE";
	final static String query8 = "INSERT INTO MOD_MODEL_ATTRIBUTE (ATT_MODEL_FK, ATT_NAME_FK, ATT_VALUE) " +
			"VALUES ((SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?), ?, ?)";
	
	final static String name9 = "ADD_ATTRIBUTE_NAME";
	final static String query9 = "INSERT INTO LKP_ATTRIBUTE_NAME (ANM_OID) VALUES (?)";
	
	final static String name10 = "ADD_ATTRIBUTE_VALUE";
	final static String query10 = "INSERT INTO MOD_MODEL_ATTRIBUTE (ATT_MODEL_FK, ATT_NAME_FK, ATT_VALUE) " +
			"VALUES ((SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?),?,?)";
	
	final static String name11 = "ADD_NEW_MODEL_VERSION";
	final static String query11 = "INSERT INTO MOD_MODEL_VERSION (VSN_MODEL_FK, VSN_VERSION, VSN_MODEL_FILE, VSN_COMMENT, VSN_FORMAT_FK) " +
			"VALUES ((SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?),?,?,?," +
			"(SELECT FMT_OID FROM MOD_MODEL_FORMAT WHERE FMT_FORMAT = ?))";
	
	final static String name12 = "DELETE_MODEL";
	final static String query12 = "DELETE FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?";
	
	final static String name13 = "EDIT_VERSION_NEW_MODEL";
	final static String query13 = "UPDATE MOD_MODEL_VERSION " +
			"SET VSN_MODEL_FILE = ?, " +
			"VSN_COMMENT = ?, " +
			"VSN_FORMAT_FK = (SELECT FMT_OID FROM MOD_MODEL_FORMAT WHERE FMT_FORMAT = ?) " +
			"WHERE VSN_MODEL_FK = (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?) " +
			"AND VSN_VERSION = ?";
	
	final static String name14 = "EDIT_VERSION_NO_MODEL";
	final static String query14 = "UPDATE MOD_MODEL_VERSION " +
			"SET VSN_COMMENT = ? " +
			"WHERE VSN_MODEL_FK = (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?) " +
			"AND VSN_VERSION = ?";
	
	final static String name15 = "UPDATE_MODEL_NAME_AND_STATUS";
	final static String query15 = "UPDATE MOD_MODEL " +
			"SET MDL_NAME = ?, MDL_IS_PUBLIC = ? " +
			"WHERE MDL_ACCESSION_ID = ?";
	
	final static String name16 = "DELETE_MODEL_ATTRIBUTES";
	final static String query16 = "DELETE FROM MOD_MODEL_ATTRIBUTE " +
			"WHERE ATT_MODEL_FK = (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?)";
	
	final static String name17 = "ADD_MODEL_COMMENT";
	final static String query17 = "INSERT INTO MOD_COMMENT (CMT_COMMENT, CMT_MODEL_FK, CMT_COMMENTER_FK) " +
			"VALUES (?, " +
			"(SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?), " +
			"(SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?))";
	
        
        final static String DELETE_MODEL_PUBLICATION = "DELETE_MODEL_PUBLICATION";
        final static String DELETE_MODEL_PUBLICATION_QUERY = "DELETE FROM MOD_PUBLICATION "+
                "WHERE PUB_MODEL_FK = ? AND PUB_OID = ?";
                
        final static String UPDATE_MODEL_PUBLICATION = "UPDATE_MODEL_PUBLICATION";
        final static String UPDATE_MODEL_PUBLICATION_QUERY = "UPDATE MOD_PUBLICATION "+
                "SET PUB_AUTHORS = ?, PUB_PERIODICAL_NAME =?, PUB_YEAR =?, PUB_TITLE =?, PUB_ABSTRACT=?, PUB_SECONDARY_AUTHORS=?, PUB_SECONDARY_TITLE=?, PUB_PUBLISHER=?, PUB_PAGES=?, PUB_ISBN=?, PUB_URL=?, PUB_TYPE_FK=? " +
                "WHERE PUB_MODEL_FK = ? AND PUB_OID = ?";

        final static String INSERT_MODEL_PUBLICATION = "INSERT_MODEL_PUBLICATION";
        final static String INSERT_MODEL_PUBLICATION_QUERY = "INSERT INTO MOD_PUBLICATION "+
                "(PUB_AUTHORS, PUB_PERIODICAL_NAME, PUB_YEAR, PUB_TITLE, PUB_ABSTRACT, PUB_SECONDARY_AUTHORS, PUB_SECONDARY_TITLE, PUB_PUBLISHER, PUB_PAGES, PUB_ISBN, PUB_URL, PUB_TYPE_FK, PUB_MODEL_FK) " +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
	final static String name18 = "NEW_USER";
	final static String query18 = "INSERT INTO REF_USER (USR_GIVEN, USR_FAMILY, USR_EMAIL, USR_ORGANISATION, USR_USERNAME, USR_PASSWORD, USR_ACTIVATION_ID) " +
			"VALUES (?,?,?,?,?,?,?)";
	
	final static String name19 = "ACTIVATE_USER";
	final static String query19 = "UPDATE REF_USER SET USR_UUID = NULL, " +
			"USR_IS_ACTIVATED = 1 " +
			"WHERE USR_USERNAME = ?";
	
	final static String name20 = "UPDATE_USER_UUID_BY_EMAIL";
	final static String query20 = "UPDATE REF_USER " +
			"SET USR_UUID = ? " +
			"WHERE USR_EMAIL = ?";
	
	final static String name21 = "SAVE_NEW_USER_PASSWORD";
	final static String query21 = "UPDATE REF_USER " +
			"SET USR_PASSWORD = ? " +
			"WHERE USR_USERNAME = ?";
	
	final static String name22 = "REMOVE_GROUP_READ_PRIVILEGES_FOR_MODEL";
	final static String query22 = "DELETE FROM MOD_GROUP_MODEL_PERMISSION " +
			"WHERE GMP_MODEL_FK = (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?)";
	
	final static String name23 = "UPDATE_MODEL_OWNER";
	final static String query23 = "UPDATE MOD_MODEL SET MDL_OWNER_FK = (SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?) WHERE MDL_ACCESSION_ID = ?";
	
	final static String name24 = "DELETE_MODEL_GROUP_PERMISSION";
	final static String query24 = "DELETE FROM MOD_GROUP_MODEL_PERMISSION WHERE GMP_MODEL_FK IN"+
									" (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?)"+
									" AND GMP_GROUP_FK IN (SELECT GRP_OID FROM REF_GROUP WHERE GRP_NAME = ?)";

	final static String name25 = "ADD_MODEL_GROUP_READ_ACCESS";
	final static String query25 = "INSERT INTO MOD_GROUP_MODEL_PERMISSION (GMP_MODEL_FK, GMP_GROUP_FK, GMP_PERMISSION_FK)"+
									" VALUES((SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?),(SELECT GRP_OID FROM REF_GROUP WHERE GRP_NAME = ?),'read')";

	final static String name26 = "ADD_MODEL_GROUP_WRITE_ACCESS";
	final static String query26 = "INSERT INTO MOD_GROUP_MODEL_PERMISSION (GMP_MODEL_FK, GMP_GROUP_FK, GMP_PERMISSION_FK)"+
									" VALUES((SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?),(SELECT GRP_OID FROM REF_GROUP WHERE GRP_NAME = ?),'edit')";
	
        public final static String HIDE_MODEL = "HIDE_MODEL";
        public final static String HIDE_MODEL_QUERY = "UPDATE MOD_MODEL SET MDL_IS_DELETED = 1 WHERE MDL_ACCESSION_ID = ?";
        
        //public final static String GET_SUPERVISOR_ID = "GET_SUPERVISOR";
        //public final static String GET_SUPERVISOR_ID_QUERY = "";
        
	// creates an array of all ParamQueries
	/*static ParamQuery pqList[] = {
		new ParamQuery(name0, query0),
		new ParamQuery(name1, query1),
		new ParamQuery(name2, query2),
		new ParamQuery(name3, query3),
		new ParamQuery(name4, query4),
		new ParamQuery(name5, query5),
		new ParamQuery(name6, query6),
		new ParamQuery(name7, query7),
		new ParamQuery(name8, query8),
		new ParamQuery(name9, query9),
		new ParamQuery(name10, query10),
		new ParamQuery(name11, query11),
		new ParamQuery(name12, query12),
		new ParamQuery(name13, query13),
		new ParamQuery(name14, query14),
		new ParamQuery(name15, query15),
		new ParamQuery(name16, query16),
		new ParamQuery(name17, query17),
		new ParamQuery(name18, query18),
		new ParamQuery(name19, query19),
		new ParamQuery(name20, query20),
		new ParamQuery(name21, query21),
		new ParamQuery(name22, query22),
		new ParamQuery(name23, query23),
		new ParamQuery(name24, query24),
		new ParamQuery(name25, query25),
		new ParamQuery(name26, query26),
                new ParamQuery(HIDE_MODEL,HIDE_MODEL_QUERY),
                new ParamQuery(UPDATE_MODEL_PUBLICATION, UPDATE_MODEL_PUBLICATION_QUERY),
                new ParamQuery(GET_MODEL_ID, GET_MODEL_ID_QUERY),
                new ParamQuery(INSERT_MODEL_PUBLICATION, INSERT_MODEL_PUBLICATION_QUERY),
                new ParamQuery(DELETE_MODEL_PUBLICATION, DELETE_MODEL_PUBLICATION_QUERY),
                
                //new ParamQuery(GET_SUPERVISOR_ID,GET_SUPERVISOR_ID_QUERY)
	};*/

        final static Map<String,ParamQuery> queries = new ConcurrentHashMap<>();
        
        static {
            
            ParamQuery pqList[] = {
		new ParamQuery(name0, query0),
		new ParamQuery(name1, query1),
		new ParamQuery(name2, query2),
		new ParamQuery(name3, query3),
		new ParamQuery(name4, query4),
		new ParamQuery(name5, query5),
		new ParamQuery(name6, query6),
		new ParamQuery(name7, query7),
		new ParamQuery(name8, query8),
		new ParamQuery(name9, query9),
		new ParamQuery(name10, query10),
		new ParamQuery(name11, query11),
		new ParamQuery(name12, query12),
		new ParamQuery(name13, query13),
		new ParamQuery(name14, query14),
		new ParamQuery(name15, query15),
		new ParamQuery(name16, query16),
		new ParamQuery(name17, query17),
		new ParamQuery(name18, query18),
		new ParamQuery(name19, query19),
		new ParamQuery(name20, query20),
		new ParamQuery(name21, query21),
		new ParamQuery(name22, query22),
		new ParamQuery(name23, query23),
		new ParamQuery(name24, query24),
		new ParamQuery(name25, query25),
		new ParamQuery(name26, query26),
                new ParamQuery(HIDE_MODEL,HIDE_MODEL_QUERY),
                new ParamQuery(UPDATE_MODEL_PUBLICATION, UPDATE_MODEL_PUBLICATION_QUERY),
                new ParamQuery(GET_MODEL_ID, GET_MODEL_ID_QUERY),
                new ParamQuery(INSERT_MODEL_PUBLICATION, INSERT_MODEL_PUBLICATION_QUERY),
                new ParamQuery(DELETE_MODEL_PUBLICATION, DELETE_MODEL_PUBLICATION_QUERY),
                
                //new ParamQuery(GET_SUPERVISOR_ID,GET_SUPERVISOR_ID_QUERY)
            };  
            
            for (ParamQuery q : pqList) queries.put(q.getQueryName(),q);
        }
        
	// finds ParamQuery object by name and returns
	public static ParamQuery getParamQuery(String name) {
		/*for (int i = 0; i < pqList.length; i++) {
			if (pqList[i].getQueryName().equals(name)) {
				return pqList[i];
			}
		}
		return null;
                */
                return queries.get(name);
	}
	
	final static String ADD_PUBLICATION_1 = "INSERT INTO MOD_PUBLICATION (PUB_AUTHORS, PUB_YEAR, PUB_PERIODICAL_NAME, PUB_TITLE, " +
			"PUB_ABSTRACT, PUB_SECONDARY_AUTHORS, PUB_SECONDARY_TITLE, PUB_PUBLISHER, PUB_PAGES, PUB_ISBN, PUB_URL, " +
			"PUB_TYPE_FK, PUB_MODEL_FK) " +
			"VALUES ";
	
	final static String ADD_PUBLICATION_2 = "(?,?,?,?,?,?,?,?,?,?,?,?," +
			"(SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?)) ";
	
	final static String ADD_IMAGE_1 = "INSERT INTO MOD_IMAGE (IMG_OID, IMG_DESCRIPTION, IMG_FILEPATH, IMG_FILENAME, IMG_MODEL_FK) " +
			"VALUES ";
	
	final static String ADD_IMAGE_2 = "(?,?,?,?,?) ";
	
	final static String ADD_VERSION_IMAGE_1 = "INSERT INTO MOD_VERSION_IMAGE (VIM_MODEL_FK, VIM_VERSION_FK, VIM_IMAGE_FK) " +
	"VALUES ";

	final static String ADD_VERSION_IMAGE_2 = "(?,?,?) ";
	
	final static String ADD_SUP_FILE_1 = "INSERT INTO MOD_SUPPL_FILE (SUP_OID, SUP_DESCRIPTION, SUP_FILEPATH, SUP_FILENAME, SUP_MODEL_FK) " +
			"VALUES ";
	
	final static String ADD_SUP_FILE_2 = "(?,?,?,?, ?) ";
	
	final static String ADD_VERSION_SUP_FILE_1 = "INSERT INTO MOD_VERSION_SUPFILE (VSF_MODEL_FK, VSF_VERSION_FK, VSF_SUP_FILE_FK) " +
			"VALUES ";
	
	final static String ADD_VERSION_SUP_FILE_2 = "(?,?,?) ";
	
	final static String ADD_MODEL_GROUP_READ_ACCESS_1 = "INSERT INTO MOD_GROUP_MODEL_PERMISSION " +
			"(GMP_MODEL_FK, GMP_GROUP_FK, GMP_PERMISSION_FK) " +
			"VALUES ";
	
	final static String ADD_MODEL_GROUP_READ_ACCESS_2 = "(?,?,?) ";
	
	final static String ADD_MODEL_GROUP_READ_ACCESS_3 = "((SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?),?,?) ";
	
	
	
}
