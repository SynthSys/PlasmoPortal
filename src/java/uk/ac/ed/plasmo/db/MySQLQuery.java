package uk.ac.ed.plasmo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import uk.ac.ed.plasmo.utility.Constants;
/**
 * The {@code DBQuery} class contains all SQL used to query the database
 * @author ctindal
 *
 */
public class MySQLQuery {
	
	final static List<String> SEARCH_COLS = new ArrayList<>();
    
	
	static {
		SEARCH_COLS.add(" AND UCASE(MDL1.MDL_NAME) LIKE ? ");
		SEARCH_COLS.add(" AND UCASE(FMT_FORMAT) LIKE ? ");
		SEARCH_COLS.add(" AND UCASE(USR_USERNAME) LIKE ? ");
		SEARCH_COLS.add(" AND UCASE(USR_GIVEN) || ' ' || UCASE(USR_FAMILY) LIKE ? ");
		SEARCH_COLS.add("  AND UCASE(ATT_VALUE) LIKE ?  ");
		SEARCH_COLS.add(" AND UCASE(MDL1.MDL_ACCESSION_ID) LIKE ? ");
	}
	
	//final static String XLOCK_MODEL = "LOCK TABLE MOD_MODEL IN EXCLUSIVE MODE";
	
	//final static String XLOCK_IMAGE = "LOCK TABLE MOD_IMAGE IN EXCLUSIVE MODE";
	
	//final static String XLOCK_SUPPL_FILE = "LOCK TABLE MOD_SUPPL_FILE IN EXCLUSIVE MODE";
	
	//final static String XLOCK_ATTRIBUTE = "LOCK TABLE MOD_MODEL_ATTRIBUTE WRITE";
	
	final static String XLOCK_ATTRIBUTE_NAME = "LOCK TABLES LKP_ATTRIBUTE_NAME WRITE";
	
	final static String XLOCK_USER = "LOCK TABLES REF_USER WRITE";
        
        //final static String XLOCK_FULL_MODEL = "LOCK TABLES MOD_MODEL WRITE, MOD_IMAGE WRITE, MOD_SUPPL_FILE WRITE, MOD_MODEL_ATTRIBUTE WRITE, LKP_ATTRIBUTE_NAME WRITE, MOD_MODEL_VERSION WRITE, MOD_VERSION_IMAGE WRITE, MOD_VERSION_SUPFILE WRITE, MOD_INTERIM_MODEL WRITE, MOD_INTERIM_MODEL AS TMP1 READ, MOD_INTERIM_MODEL AS TMP2 READ";
	
        //final static String XLOCK_VERSION_MODEL = "LOCK TABLES MOD_MODEL READ, MOD_IMAGE WRITE, MOD_SUPPL_FILE WRITE, MOD_VERSION_IMAGE WRITE, MOD_VERSION_SUPFILE WRITE";
        
        final static String XUNLOCK ="UNLOCK TABLES";
        
	//final static String MAX_MODEL_ACCESSION = "SELECT MAX(CAST(SUBSTR(MDL_ACCESSION_ID, INSTR(MDL_ACCESSION_ID,'_')+1) AS BIGINT)) FROM MOD_MODEL";
        //final static String MAX_MODEL_ACCESSION = "SELECT MAX(CAST(SUBSTR(MDL_ACCESSION_ID, INSTR(MDL_ACCESSION_ID,'_')+1)  AS SIGNED) ) FROM MOD_MODEL";
	
	final static String ALL_MODEL_IDS = "SELECT DISTINCT MDL_ACCESSION_ID FROM MOD_MODEL WHERE MDL_IS_PUBLIC = 1 AND MDL_IS_DELETED = 0";
	
	final static String ALL_MODEL_FORMATS = "SELECT DISTINCT FMT_FORMAT FROM MOD_MODEL_FORMAT ORDER BY FMT_FORMAT";
	
	final static String ALL_USER_GROUPS = "SELECT GRP_OID, GRP_NAME, GRP_DESCRIPTION FROM REF_GROUP ORDER BY GRP_NAME";
	
	final static String TOTAL_NUMBER_OF_MODELS = "SELECT COUNT(DISTINCT MDL_ACCESSION_ID) FROM MOD_MODEL WHERE MDL_IS_PUBLIC = 1";
	
	//final static String MAX_INTERIM_OID = "SELECT MAX(INT_OID) FROM MOD_INTERIM_MODEL";
	
	final static String ORDER_BY_MODEL_ACCESSION = " ORDER BY CAST(SUBSTR(MDL_ACCESSION_ID, INSTR(MDL_ACCESSION_ID,'_')+1) AS SIGNED) ";
	
	final static String ORDER_BY_MODEL_NAME = " ORDER BY UCASE(TRIM(MDL_NAME))";
	
	//final static String PAGINATION_CONTROL_START = "SELECT * FROM (";
	        
	//final static String PAGINATION_CONTROL_END = ") WHERE RN BETWEEN ? AND ?";
	
        final static String MODEL_NAME_PAGINATION = " ORDER BY UCASE( TRIM( MDL_NAME ) ) LIMIT ?,? ";
        
	//final static String ROW_NUMBER_COUNT = ", ROWNUMBER() OVER(ORDER BY UCASE(TRIM(MDL_NAME))) AS RN ";
	
	final static String OUTER_BROWSE_MODELS_COLS = "SELECT MDL_ACCESSION_ID, MDL_NAME, FMT_FORMAT, VSN_VERSION, USR_USERNAME, USR_EMAIL, MDL_SUBMISSION_DATE, USR_GIVEN, USR_FAMILY, MDL_IS_PUBLIC ";
	
	final static String BROWSE_MODELS_COLUMNS = "SELECT MDL1.MDL_ACCESSION_ID, MDL1.MDL_NAME, FMT_FORMAT, VSN_VERSION, USR_USERNAME, USR_EMAIL, MDL1.MDL_SUBMISSION_DATE, USR_GIVEN, USR_FAMILY, MDL1.MDL_IS_PUBLIC ";
	
	final static String BROWSE_MODELS_BASE_TABLES = " FROM MOD_MODEL MDL1 " +
	"JOIN MOD_MODEL_VERSION " +
	"ON MDL_OID = VSN_MODEL_FK " +
	"JOIN MOD_MODEL_FORMAT " +
	"ON VSN_FORMAT_FK = FMT_OID " +
	"JOIN REF_USER " +
	"ON USR_OID = MDL_OWNER_FK ";
	
	final static String BROWSE_MODELS_TABLES = " FROM MOD_MODEL MDL1 " +
			"JOIN MOD_MODEL_VERSION " +
			"ON MDL_OID = VSN_MODEL_FK " +
			"JOIN MOD_MODEL_FORMAT " +
			"ON VSN_FORMAT_FK = FMT_OID " +
			"JOIN REF_USER " +
			"ON USR_OID = MDL_SUBMITTER_FK " +
			"WHERE VSN_VERSION = (SELECT MAX(VSN2.VSN_VERSION) FROM MOD_MODEL_VERSION VSN2, MOD_MODEL MDL2 WHERE MDL2.MDL_OID = MDL1.MDL_OID AND VSN2.VSN_MODEL_FK = MDL2.MDL_OID)";
	
	
	
	final static String SEARCH_MODEL_ATTRIBUTES_TABLES = " FROM MOD_MODEL MDL1 " +
	"JOIN MOD_MODEL_VERSION " +
	"ON MDL_OID = VSN_MODEL_FK " +
	"JOIN MOD_MODEL_FORMAT " +
	"ON VSN_FORMAT_FK = FMT_OID " +
	"JOIN REF_USER " +
	"ON USR_OID = MDL_SUBMITTER_FK " +
	"LEFT JOIN MOD_MODEL_ATTRIBUTE " +
	"ON ATT_MODEL_FK = MDL1.MDL_OID " +
	"WHERE VSN_VERSION = (SELECT MAX(VSN2.VSN_VERSION) FROM MOD_MODEL_VERSION VSN2, MOD_MODEL MDL2 WHERE MDL2.MDL_OID = MDL1.MDL_OID AND VSN2.VSN_MODEL_FK = MDL2.MDL_OID) ";
	
	final static String ALL_PUBLIC_MODELS_Q = BROWSE_MODELS_COLUMNS + 
			BROWSE_MODELS_BASE_TABLES +
			"WHERE VSN_VERSION = (SELECT MAX(VSN2.VSN_VERSION) " +
			"FROM MOD_MODEL_VERSION VSN2, MOD_MODEL MDL2 " +
			"WHERE MDL2.MDL_OID = MDL1.MDL_OID AND VSN2.VSN_MODEL_FK = MDL2.MDL_OID) " +
			"AND MDL1.MDL_IS_PUBLIC = 1 AND MDL1.MDL_IS_DELETED=0 ";
	
	final static String ALL_PRIVATE_USER_MODELS = BROWSE_MODELS_COLUMNS +
			BROWSE_MODELS_BASE_TABLES +
			"WHERE VSN_VERSION = (SELECT MAX(VSN2.VSN_VERSION) " +
			"FROM MOD_MODEL_VERSION VSN2, MOD_MODEL MDL2 " +
			"WHERE MDL2.MDL_OID = MDL1.MDL_OID AND VSN2.VSN_MODEL_FK = MDL2.MDL_OID) " +
			"AND MDL_IS_PUBLIC = 0 AND MDL1.MDL_IS_DELETED=0 " +
			"AND USR_OID = ? ";
	
	final static String ALL_GROUP_ACCESS_RESTRICTED_MODELS = BROWSE_MODELS_COLUMNS +
			BROWSE_MODELS_BASE_TABLES + 
			"JOIN MOD_GROUP_MODEL_PERMISSION " +
			"ON MDL_OID = GMP_MODEL_FK " +
			"JOIN REF_GROUP " +
			"ON GRP_OID = GMP_GROUP_FK " +
			"WHERE VSN_VERSION = " +
			"(SELECT MAX(VSN2.VSN_VERSION) " +
			"FROM MOD_MODEL_VERSION VSN2, MOD_MODEL MDL2 " +
			"WHERE MDL2.MDL_OID = MDL1.MDL_OID AND VSN2.VSN_MODEL_FK = MDL2.MDL_OID) " +
			"AND MDL1.MDL_IS_PUBLIC = 0 AND MDL1.MDL_IS_DELETED=0 " +
			"AND GRP_OID IN " +
			"(SELECT DISTINCT GRP_OID " +
			"FROM MOD_MODEL MDL2, MOD_GROUP_MODEL_PERMISSION, REF_GROUP, REF_USER, REF_USER_GROUP " +
			"WHERE MDL1.MDL_OID = MDL2.MDL_OID " +
			"AND MDL2.MDL_OID = GMP_MODEL_FK " +
			"AND GRP_OID = GMP_GROUP_FK " +
			"AND GMP_PERMISSION_FK = 'read' " +
			"AND USR_OID = UGP_USER_FK AND " +
			"GRP_OID = UGP_GROUP_FK " +
			"AND USR_OID = ?) ";
	
	final static String ALL_RESTRICTED_MODELS_FOR_SUPERVISOR = BROWSE_MODELS_COLUMNS +
	BROWSE_MODELS_BASE_TABLES +
	"JOIN REF_SUPERVISES " +
	"ON USR_OID = SUP_USER_FK " +
	"WHERE VSN_VERSION = " +
	"(SELECT MAX(VSN2.VSN_VERSION) " +
	"FROM MOD_MODEL_VERSION VSN2, MOD_MODEL MDL2 " +
	"WHERE MDL2.MDL_OID = MDL1.MDL_OID AND VSN2.VSN_MODEL_FK = MDL2.MDL_OID) " +
	"AND MDL1.MDL_IS_PUBLIC = 0 AND MDL1.MDL_IS_DELETED=0 " +
	"AND SUP_SUPERVISOR_FK = ?";
	
	final static String SEARCH_MODELS_BASE = 
			BROWSE_MODELS_COLUMNS + BROWSE_MODELS_TABLES + " AND UCASE(MDL_NAME) = ? UNION " +
			BROWSE_MODELS_COLUMNS + BROWSE_MODELS_TABLES +  " AND UCASE(FMT_FORMAT) = ? UNION " +
			BROWSE_MODELS_COLUMNS + BROWSE_MODELS_TABLES +  " AND UCASE(USR_USERNAME) = ? UNION " +
			BROWSE_MODELS_COLUMNS + BROWSE_MODELS_TABLES +  " AND UCASE(USR_GIVEN) = ? UNION " +
			BROWSE_MODELS_COLUMNS + BROWSE_MODELS_TABLES +  " AND UCASE(USR_FAMILY) = ? UNION " +
			BROWSE_MODELS_COLUMNS + SEARCH_MODEL_ATTRIBUTES_TABLES +  " AND UCASE(ATT_VALUE) LIKE ? ";
	
	final static String SEARCH_MODELS_BASE_II = BROWSE_MODELS_COLUMNS + BROWSE_MODELS_BASE_TABLES;
	
	final static String LATEST_VERSION_CRITERIA = " VSN_VERSION = (SELECT MAX(VSN2.VSN_VERSION) FROM MOD_MODEL_VERSION VSN2, MOD_MODEL MDL2 WHERE MDL2.MDL_OID = MDL1.MDL_OID AND VSN2.VSN_MODEL_FK = MDL2.MDL_OID) ";
	
	final static String USER_PERMISSIONS_TABLES = " LEFT JOIN MOD_GROUP_MODEL_PERMISSION ON MDL_OID = GMP_MODEL_FK LEFT JOIN REF_GROUP ON GRP_OID = GMP_GROUP_FK LEFT JOIN REF_SUPERVISES ON USR_OID = SUP_USER_FK ";
	
	final static String USER_PERMISSIONS_JOIN_CRITERIA = " AND (MDL1.MDL_IS_PUBLIC = 1 " +
			"OR (MDL_IS_PUBLIC = 0 AND USR_OID = ?) " +
			"OR (MDL1.MDL_IS_PUBLIC = 0 " +
			"AND GRP_OID IN " +
			"(SELECT DISTINCT GRP_OID " +
			"FROM MOD_MODEL MDL2, MOD_GROUP_MODEL_PERMISSION, REF_GROUP, REF_USER, REF_USER_GROUP " +
			"WHERE MDL1.MDL_OID = MDL2.MDL_OID " +
			"AND MDL2.MDL_OID = GMP_MODEL_FK " +
			"AND GRP_OID = GMP_GROUP_FK " +
			"AND GMP_PERMISSION_FK = 'read' " +
			"AND USR_OID = UGP_USER_FK " +
			"AND GRP_OID = UGP_GROUP_FK " +
			"AND USR_OID = ?)) " +
			"OR (MDL1.MDL_IS_PUBLIC = 0 AND SUP_SUPERVISOR_FK = ?)) ";
	
	final static String JOIN_ATTRIBUTE_TABLE = " LEFT JOIN MOD_MODEL_ATTRIBUTE ON ATT_MODEL_FK = MDL1.MDL_OID ";
	
	/*final static String SEARCH_MODELS = PAGINATION_CONTROL_START + OUTER_BROWSE_MODELS_COLS + ROW_NUMBER_COUNT + " FROM (" +
	SEARCH_MODELS_BASE + ") "+ PAGINATION_CONTROL_END;*/
	final static String SEARCH_MODELS = OUTER_BROWSE_MODELS_COLS +" FROM (" +
	SEARCH_MODELS_BASE + ") AS MODDET "+ MODEL_NAME_PAGINATION;
	
	/*final static String ALL_MODELS = PAGINATION_CONTROL_START + BROWSE_MODELS_COLUMNS + 
										ROW_NUMBER_COUNT+ BROWSE_MODELS_TABLES +
										"AND MDL_IS_PUBLIC = 1 AND MDL_IS_DELETED=0"
										+ PAGINATION_CONTROL_END;*/
	
	final static String ALL_MODELS = BROWSE_MODELS_COLUMNS + BROWSE_MODELS_TABLES + 
                            "AND MDL_IS_PUBLIC = 1 AND MDL_IS_DELETED=0" +
			    MODEL_NAME_PAGINATION;
        
	/*final static String ALL_USER_MODELS = PAGINATION_CONTROL_START + BROWSE_MODELS_COLUMNS + 
	ROW_NUMBER_COUNT+ BROWSE_MODELS_TABLES +
	"AND USR_OID = (SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?) AND MDL_IS_DELETED=0 "
	+ PAGINATION_CONTROL_END;*/
	final static String ALL_USER_MODELS = BROWSE_MODELS_COLUMNS + BROWSE_MODELS_TABLES +
	"AND USR_OID = (SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?) AND MDL_IS_DELETED=0 "
	+ MODEL_NAME_PAGINATION;
	
	//final static String MAX_IMAGE_OID = "SELECT MAX(IMG_OID) FROM MOD_IMAGE";
	
	//final static String MAX_SUP_FILE_OID = "SELECT MAX(SUP_OID) FROM MOD_SUPPL_FILE";
	
	final static String MODEL_ACCESSION_FROM_LATEST_VERSION_FORMAT = "SELECT DISTINCT MDL_ACCESSION_ID " +
	"FROM MOD_MODEL, MOD_MODEL_FORMAT, MOD_MODEL_VERSION " +
	"WHERE VSN_FORMAT_FK = FMT_OID " +
	"AND MDL_OID = VSN_MODEL_FK " +
	"AND VSN_VERSION = (SELECT MAX(VSN_VERSION) FROM MOD_MODEL_VERSION WHERE VSN_MODEL_FK = MDL_OID) " +
	"AND MDL_IS_PUBLIC = 1 " +
	"AND FMT_FORMAT ";
	
	final static String LIKE_Q = "LIKE ? ";
	
	final static String EQUALS_Q = " = ?";
	
	
	final static String name0 = "USER_LOGIN";
	final static String query0 = "SELECT USR_USERNAME, USR_PASSWORD, USR_EMAIL, USR_FAMILY, USR_GIVEN, USR_ORGANISATION, USR_OID " +
			"FROM REF_USER " +
			"WHERE USR_IS_ACTIVATED = 1 " +
			"AND USR_USERNAME = ? " +
			"AND USR_PASSWORD =  ?";
	
	final static String name1 = "MODEL_FORMAT_NAME";
	final static String query1 = "SELECT FMT_FORMAT FROM MOD_MODEL_FORMAT WHERE UPPER(FMT_FORMAT) = ?";
	
	final static String name2 = "PUB_TYPE_ID_FROM_TYPE";
	final static String query2 = "SELECT PTP_OID FROM REF_PUBLICATION_TYPE WHERE PTP_TYPE = ?";
	
	final static String name3 = "MDL_ID_FROM_ACCESSION";
	final static String query3 = "SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?";
	
	final static String name4 = "FORMAT_ID_FROM_NAME";
	final static String query4 = "SELECT FMT_OID FROM MOD_MODEL_FORMAT WHERE FMT_FORMAT = ?";
	
	final static String name5 = "SCHEMA_LOCATION";
	final static String query5 = "SELECT FMT_OID, FMT_SCHEMA_FILE FROM MOD_MODEL_FORMAT WHERE FMT_FORMAT = ?";
	
	final static String name6 = "ALL_MODELS";
	//final static String query6 = PAGINATION_CONTROL_START + BROWSE_MODELS_COLUMNS +ROW_NUMBER_COUNT+ BROWSE_MODELS_TABLES + PAGINATION_CONTROL_END;
	final static String query6 = BROWSE_MODELS_COLUMNS + BROWSE_MODELS_TABLES + MODEL_NAME_PAGINATION;
	
	final static String name7 = "MODEL_FILE";
	//final static String query7 = "SELECT XMLSERIALIZE(VSN_MODEL_FILE AS CLOB(30M) INCLUDING XMLDECLARATION) FROM MOD_MODEL_VERSION, MOD_MODEL WHERE MDL_OID = VSN_MODEL_FK AND MDL_ACCESSION_ID = ? AND VSN_VERSION = ?";
	final static String query7 = "SELECT VSN_MODEL_FILE FROM MOD_MODEL_VERSION, MOD_MODEL WHERE MDL_OID = VSN_MODEL_FK AND MDL_ACCESSION_ID = ? AND VSN_VERSION = ?";
	
	final static String name8 = "ATTRIBUTE_NAME";
	final static String query8 = "SELECT ANM_OID FROM LKP_ATTRIBUTE_NAME WHERE ANM_OID = ?";
	
	final static String name9 = "MODEL_SUBMITTER";
	final static String query9 = "SELECT USR_GIVEN, USR_FAMILY, USR_USERNAME, USR_EMAIL, USR_ORGANISATION " +
			"FROM REF_USER, MOD_MODEL " +
			"WHERE MDL_SUBMITTER_FK = USR_OID " +
			"AND MDL_ACCESSION_ID = ?";
	
	final static String name10 = "MODEL_VERSION_DATA";
	final static String query10 = "SELECT MDL_ACCESSION_ID, MDL_NAME, FMT_FORMAT, VSN_VERSION, MDL_SUBMISSION_DATE, VSN_COMMENT, MDL_IS_PUBLIC " +
			"FROM MOD_MODEL " +
			"JOIN MOD_MODEL_VERSION " +
			"ON VSN_MODEL_FK = MDL_OID " +
			"JOIN MOD_MODEL_FORMAT ON FMT_OID = VSN_FORMAT_FK " +
			"WHERE MDL_ACCESSION_ID = ? AND VSN_VERSION = ?";
	
	final static String MODEL_PUBLICATIONS = "MODEL_PUBLICATIONS";
	final static String MODEL_PUBLICATIONS_QUERY = "SELECT PUB_AUTHORS, PUB_PERIODICAL_NAME, PUB_YEAR, PUB_TITLE, PUB_ABSTRACT, PUB_SECONDARY_AUTHORS, PUB_SECONDARY_TITLE, PUB_PUBLISHER, PUB_PAGES, PUB_ISBN, PUB_URL, PUB_TYPE_FK, PUB_OID " +
			"FROM MOD_PUBLICATION " +
			"JOIN MOD_MODEL " +
			"ON MDL_OID = PUB_MODEL_FK " +
			"WHERE MDL_ACCESSION_ID = ?";
        
        
	final static String MODEL_PUBLICATION = "MODEL_PUBLICATION";
	final static String MODEL_PUBLICATION_QUERY = "SELECT PUB_AUTHORS, PUB_PERIODICAL_NAME, PUB_YEAR, PUB_TITLE, PUB_ABSTRACT, PUB_SECONDARY_AUTHORS, PUB_SECONDARY_TITLE, PUB_PUBLISHER, PUB_PAGES, PUB_ISBN, PUB_URL, PUB_TYPE_FK, PUB_OID " +
			"FROM MOD_PUBLICATION " +
			"JOIN MOD_MODEL " +
			"ON MDL_OID = PUB_MODEL_FK " +
			"WHERE MDL_ACCESSION_ID = ? AND PUB_OID = ?";
        
	
	final static String name12 = "MODEL_VERSION_IMAGES";
	final static String query12 = "SELECT DISTINCT IMG_FILEPATH, IMG_FILENAME, " +
			"IMG_DESCRIPTION " +
			"FROM MOD_IMAGE " +
			"JOIN MOD_VERSION_IMAGE ON VIM_IMAGE_FK = IMG_OID " +
			"JOIN MOD_MODEL_VERSION ON VIM_VERSION_FK = VSN_VERSION AND VIM_MODEL_FK = VSN_MODEL_FK " +
			"JOIN MOD_MODEL ON MDL_OID = VSN_MODEL_FK " +
			"WHERE MDL_ACCESSION_ID = ? AND VSN_VERSION = ?";
	
	final static String name13 = "MODEL_VERSION_SUPP_FILES";
	final static String query13 = "SELECT SUP_FILEPATH, SUP_FILENAME, " +
			"SUP_DESCRIPTION " +
			"FROM MOD_SUPPL_FILE " +
			"JOIN MOD_VERSION_SUPFILE ON VSF_SUP_FILE_FK = SUP_OID " +
			"JOIN MOD_MODEL_VERSION ON VSF_VERSION_FK = VSN_VERSION AND VSF_MODEL_FK = VSN_MODEL_FK " +
			"JOIN MOD_MODEL ON MDL_OID = VSN_MODEL_FK " +
			"WHERE MDL_ACCESSION_ID = ? AND VSN_VERSION = ?";
	
	final static String name14 = "MODEL_ATTRIBUTES";
	final static String query14 = "SELECT ATT_NAME_FK, ATT_VALUE " +
			"FROM MOD_MODEL_ATTRIBUTE " +
			"JOIN MOD_MODEL " +
			"ON ATT_MODEL_FK = MDL_OID " +
			"WHERE MDL_ACCESSION_ID = ?";
	
	final static String name15 = "MODEL_DESCRIPTION";
	final static String query15 = "SELECT ATT_VALUE " +
			"FROM MOD_MODEL_ATTRIBUTE " +
			"WHERE ATT_NAME_FK = 'Description' " +
			"AND ATT_MODEL_FK = (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?)";
	
	final static String name16 = "NUM_MODELS_FOUND_FROM_SEARCH";
	final static String query16 = "SELECT COUNT(*) FROM (" + SEARCH_MODELS_BASE + ")";
	
	final static String name17 = "MODEL_NAME_FROM_ACCESSION";
	final static String query17 = "SELECT MDL_NAME, MDL_IS_PUBLIC FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?";
	
	final static String name18 = "MODEL_IMAGES";
	final static String query18 = "SELECT DISTINCT IMG_OID, IMG_FILEPATH, IMG_FILENAME, " +
			"IMG_DESCRIPTION " +
			"FROM MOD_IMAGE " +
			"JOIN MOD_MODEL " +
			"ON MDL_OID = IMG_MODEL_FK " +
			"WHERE MDL_ACCESSION_ID = ?";
	
	final static String name19 = "MODEL_SUPP_FILES";
	final static String query19 = "SELECT DISTINCT SUP_OID, SUP_FILEPATH, SUP_FILENAME, " +
					"SUP_DESCRIPTION " +
			"FROM MOD_SUPPL_FILE " +
			"JOIN MOD_MODEL " +
			"ON MDL_OID = SUP_MODEL_FK " +
			"WHERE MDL_ACCESSION_ID = ?";
	
	final static String name20 = "MAX_MODEL_VERSION";
	final static String query20 = "SELECT MAX(VSN_VERSION) " +
			"FROM MOD_MODEL_VERSION " +
			"WHERE VSN_MODEL_FK = (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?)";
	
	final static String name21 = "MODEL_VERSIONS";
	final static String query21 = "SELECT DISTINCT VSN_VERSION FROM MOD_MODEL_VERSION, MOD_MODEL WHERE VSN_MODEL_FK = MDL_OID AND MDL_ACCESSION_ID = ?";
	
	final static String name22 = "MODEL_VERSION_DATA_FOR_EDIT";
	final static String query22 = "SELECT MDL_NAME, FMT_FORMAT, VSN_SUBMISSION_DATE,VSN_COMMENT " +
			"FROM MOD_MODEL " +
			"JOIN MOD_MODEL_VERSION " +
			"ON MDL_OID = VSN_MODEL_FK " +
			"JOIN MOD_MODEL_FORMAT " +
			"ON FMT_OID = VSN_FORMAT_FK " +
			"WHERE MDL_ACCESSION_ID = ? " +
			"AND VSN_VERSION = ?";
	
	final static String name23 = "XSLT_FILE_FOR_MODEL";
	final static String query23 = "SELECT DISTINCT STY_OID, STY_FILENAME " +
			"FROM MOD_STYLESHEET " +
			"JOIN MOD_MODEL_VERSION " +
			"ON VSN_FORMAT_FK = STY_FORMAT_FK " +
			"JOIN MOD_MODEL " +
			"ON MDL_OID = VSN_MODEL_FK " +
			"WHERE STY_OID = ? " +
			"AND MDL_ACCESSION_ID = ? " +
			"AND VSN_VERSION = ?";
	
	final static String name24 = "MODEL_TRANSFORM_OPTIONS";
	final static String query24 = "SELECT DISTINCT STY_OID, STY_DESCRIPTION " +
			"FROM MOD_STYLESHEET " +
			"JOIN MOD_MODEL_VERSION " +
			"ON VSN_FORMAT_FK = STY_FORMAT_FK " +
			"JOIN MOD_MODEL " +
			"ON MDL_OID = VSN_MODEL_FK " +
			"WHERE MDL_ACCESSION_ID = ? " +
			"AND VSN_VERSION = ? " +
			"ORDER BY STY_DESCRIPTION";
	
	final static String name25 = "MODEL_COMMENTS";
	final static String query25 = "SELECT CMT_COMMENT, CMT_DATE, CMT_COMMENTER_FK, USR_USERNAME, USR_EMAIL " +
			"FROM MOD_COMMENT " +
			"JOIN MOD_MODEL " +
			"ON MDL_OID = CMT_MODEL_FK " +
			"JOIN REF_USER " +
			"ON USR_OID = CMT_COMMENTER_FK " +
			"WHERE MDL_ACCESSION_ID = ? " +
			"ORDER BY CMT_DATE DESC";
	
	final static String name26 = "USER_BY_USERNAME";
	final static String query26 = "SELECT USR_USERNAME, USR_PASSWORD, USR_EMAIL, USR_FAMILY, USR_GIVEN, USR_ORGANISATION, USR_OID " +
			"FROM REF_USER " +
			"WHERE USR_IS_ACTIVATED = 1 " +
			"AND USR_USERNAME = ?";
	
	final static String name27 = "USER_BY_EMAIL";
	final static String query27 = "SELECT USR_OID, USR_USERNAME, USR_EMAIL, USR_GIVEN, USR_FAMILY, USR_ORGANISATION, USR_UUID " +
			"FROM REF_USER WHERE USR_EMAIL = ?";
	
	final static String name28 = "USER_BY_ACTIVATION_ID";
	final static String query28 = "SELECT USR_USERNAME, USR_PASSWORD, USR_EMAIL, USR_FAMILY, USR_GIVEN, USR_ORGANISATION, USR_OID " +
			"FROM REF_USER " +
			"WHERE USR_IS_ACTIVATED = 1 " +
			"AND USR_UUID = ?";
	
	final static String name29 = "MODEL_ACCESSION_FROM_NAME";
	final static String query29 = "SELECT DISTINCT MDL_ACCESSION_ID FROM MOD_MODEL WHERE UCASE(MDL_NAME) = UCASE(?) AND MDL_IS_PUBLIC = 1";
	
	final static String name30 = "MODEL_ACCESSION_FROM_LATEST_VERSION_FORMAT";
	final static String query30 = MODEL_ACCESSION_FROM_LATEST_VERSION_FORMAT + EQUALS_Q;
	
	final static String name31 = "MODEL_IS_PUBLIC_AND_OWNER_ID";
	final static String query31 = "SELECT MDL_IS_PUBLIC, MDL_OWNER_FK FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?";
	
	//final static String name32 = "TOTAL_NUMBER_OF_MODELS";
	//final static String query32 = "SELECT COUNT(DISTINCT MDL_ACCESSION_ID) FROM MOD_MODEL, REF_USER WHERE USR_OID = MDL_SUBMITTER_FK AND (MDL_IS_PUBLIC = 1 OR (MDL_IS_PUBLIC = 0 AND USR_OID = (SELECT USR_OID FROM REF_USER WHERE USR_USERNAME = ?)))";
	
	final static String name32 = "TOTAL_NUMBER_OF_USER_MODELS";
	final static String query32 = "SELECT COUNT(DISTINCT MDL_ACCESSION_ID) FROM MOD_MODEL, REF_USER WHERE USR_OID = MDL_SUBMITTER_FK AND USR_USERNAME = ?";
	
	final static String name33 = "GROUP_IDS_WITH_ACCESS_TO_MODEL";
	final static String query33 = "select GMP_GROUP_FK from MOD_GROUP_MODEL_PERMISSION where GMP_MODEL_FK = (SELECT MDL_OID FROM MOD_MODEL WHERE MDL_ACCESSION_ID = ?) AND GMP_PERMISSION_FK = 'read'";
	
	final static String name34 = "USERS_GROUPS";
	final static String query34 = "SELECT DISTINCT GRP_NAME FROM REF_USER, REF_GROUP, REF_USER_GROUP WHERE USR_OID = UGP_USER_FK AND GRP_OID = UGP_GROUP_FK AND USR_OID = ?";
	
	final static String name35 = "GROUPS_WITH_MODEL_READ_ACCESS";
	final static String query35 = "SELECT DISTINCT GRP_NAME FROM REF_GROUP, MOD_GROUP_MODEL_PERMISSION, MOD_MODEL WHERE GRP_OID = GMP_GROUP_FK AND GMP_MODEL_FK = MDL_OID AND GMP_PERMISSION_FK = 'read' AND MDL_ACCESSION_ID = ?";
	
	final static String name36 = "SUPERVISOR_OF_MODEL_OWNER";
	final static String query36 = "SELECT SUP.USR_OID FROM REF_USER SUP, REF_USER OWNER, MOD_MODEL, REF_SUPERVISES WHERE SUP_SUPERVISOR_FK = SUP.USR_OID AND SUP_USER_FK = OWNER.USR_OID AND MDL_OWNER_FK = OWNER.USR_OID AND MDL_ACCESSION_ID = ?";
	
	final static String name37 = "MODEL_OWNER";
	final static String query37 = "SELECT USR_GIVEN, USR_FAMILY, USR_USERNAME, USR_EMAIL, USR_ORGANISATION, USR_OID " +
								"FROM REF_USER, MOD_MODEL " +
								"WHERE MDL_OWNER_FK = USR_OID " +
								"AND MDL_ACCESSION_ID = ?";
	
	final static String name38 = "SUPERVISED_USERS_AND_SELF";
	final static String query38 = "SELECT USR_OID, USR_USERNAME, USR_GIVEN, USR_FAMILY, USR_ORGANISATION FROM REF_USER, REF_SUPERVISES WHERE SUP_SUPERVISOR_FK = ? AND SUP_USER_FK = USR_OID " +
								"UNION " +
								"SELECT USR_OID, USR_USERNAME, USR_GIVEN, USR_FAMILY, USR_ORGANISATION FROM REF_USER WHERE USR_OID = ? ORDER BY USR_GIVEN, USR_FAMILY";
	
	final static String name39 = "MODEL_OWNER_AND_SUPERVISOR_OR_OWNER_SUPERVISOR";
	final static String query39 = "SELECT USR_OID, USR_USERNAME, USR_GIVEN, USR_FAMILY, USR_ORGANISATION FROM REF_USER, MOD_MODEL, REF_SUPERVISES WHERE USR_OID = SUP_SUPERVISOR_FK AND SUP_USER_FK = MDL_OWNER_FK AND MDL_ACCESSION_ID = ? AND SUP_SUPERVISOR_FK = ? " +
			"UNION " +
			"SELECT USR_OID, USR_USERNAME, USR_GIVEN, USR_FAMILY, USR_ORGANISATION FROM REF_USER, REF_SUPERVISES, MOD_MODEL WHERE SUP_SUPERVISOR_FK = USR_OID AND MDL_OWNER_FK = USR_OID AND MDL_ACCESSION_ID = ? AND USR_OID = ?";

	final static String name40 = "GROUP_NAMES_WITH_MODEL_PERMISSION";
	final static String query40 = "SELECT GRP_NAME, GMP_PERMISSION_FK, GMP_GROUP_FK FROM REF_GROUP" +
								" LEFT OUTER JOIN MOD_GROUP_MODEL_PERMISSION ON GRP_OID = GMP_GROUP_FK AND GMP_MODEL_FK = ?" +
								" WHERE GRP_OID IN (SELECT UGP_GROUP_FK FROM REF_USER_GROUP WHERE UGP_USER_FK = ?)";
	
        final static String GENERATOR_VALUE = "GENERATOR_VALUE";
        final static String GENERATOR_VALUE_QUERY = "SELECT COUNTER FROM GENERATOR WHERE NAME = ?";
        
        final static String UPDATE_GENERATOR_VALUE = "UPDATE_GENERATOR_VALUE";
        final static String UPDATE_GENERATOR_VALUE_QUERY = "UPDATE GENERATOR SET COUNTER=? WHERE NAME=? AND COUNTER=?";
        
        public final static String GET_FORMAT_ID_FROM_ACCESSION_VERSION = "GET_FORMAT_ID_FROM_ACCESSION_VERSION";
        final static String GET_FORMAT_ID_FROM_ACCESSION_VERSION_QR = "SELECT VSN_FORMAT_FK " +
			"FROM MOD_MODEL " +
			"JOIN MOD_MODEL_VERSION " +
			"ON VSN_MODEL_FK = MDL_OID " +
			"WHERE MDL_ACCESSION_ID = ? AND VSN_VERSION = ?"; 
        
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
		new ParamQuery(MODEL_PUBLICATIONS, MODEL_PUBLICATIONS_QUERY),
		new ParamQuery(MODEL_PUBLICATION, MODEL_PUBLICATION_QUERY),
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
		new ParamQuery(name27, query27),
		new ParamQuery(name28, query28),
		new ParamQuery(name29, query29),
		new ParamQuery(name30, query30),
		new ParamQuery(name31, query31),
		new ParamQuery(name32, query32),
		new ParamQuery(name33, query33),
		new ParamQuery(name34, query34),
		new ParamQuery(name35, query35),
		new ParamQuery(name36, query36),
		new ParamQuery(name37, query37),
		new ParamQuery(name38, query38),
		new ParamQuery(name39, query39),
		new ParamQuery(name40, query40),
                new ParamQuery(GENERATOR_VALUE, GENERATOR_VALUE_QUERY),
                new ParamQuery(UPDATE_GENERATOR_VALUE, UPDATE_GENERATOR_VALUE_QUERY),
                new ParamQuery(GET_FORMAT_ID_FROM_ACCESSION_VERSION,GET_FORMAT_ID_FROM_ACCESSION_VERSION_QR)
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
                        new ParamQuery(MODEL_PUBLICATIONS, MODEL_PUBLICATIONS_QUERY),
                        new ParamQuery(MODEL_PUBLICATION, MODEL_PUBLICATION_QUERY),
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
                        new ParamQuery(name27, query27),
                        new ParamQuery(name28, query28),
                        new ParamQuery(name29, query29),
                        new ParamQuery(name30, query30),
                        new ParamQuery(name31, query31),
                        new ParamQuery(name32, query32),
                        new ParamQuery(name33, query33),
                        new ParamQuery(name34, query34),
                        new ParamQuery(name35, query35),
                        new ParamQuery(name36, query36),
                        new ParamQuery(name37, query37),
                        new ParamQuery(name38, query38),
                        new ParamQuery(name39, query39),
                        new ParamQuery(name40, query40),
                        new ParamQuery(GENERATOR_VALUE, GENERATOR_VALUE_QUERY),
                        new ParamQuery(UPDATE_GENERATOR_VALUE, UPDATE_GENERATOR_VALUE_QUERY),
                        new ParamQuery(GET_FORMAT_ID_FROM_ACCESSION_VERSION,GET_FORMAT_ID_FROM_ACCESSION_VERSION_QR)
                };            
            for (ParamQuery q : pqList) queries.put(q.getQueryName(), q);
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
}
