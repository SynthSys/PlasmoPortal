package uk.ac.ed.plasmo.utility;

import java.util.ResourceBundle;

/**
 * Class containing constants used throughout the application
 * @author ctindal
 *
 */
public class Constants {
	
	static ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	
	public static final String PROJECT_NAME = "PlaSMo";
	//public static final String PROJECT_URL = "http://www.plasmo.ed.ac.uk/";
	
	public static final String UPLOAD_BASE_DIR = bundle.getString("UPLOAD_BASE_DIR");//"/home/ctindal/work/plasmo/files/web/tmpsubmitted/";
	//public static final String UPLOAD_BASE_DIR = "/disk/data/web/html/plasmoweb/portal_data/";
	
	public static final String UPLOAD_SCHEMA_DIR = UPLOAD_BASE_DIR + "schema/";
	
	public static final String UPLOAD_XSLT_DIR = UPLOAD_BASE_DIR +"xslt/";
	
	public static final String UPLOAD_DATA_DIR = UPLOAD_BASE_DIR + "data/";
	
	//public static final String PROJECT_BASE_URL = "http://mook.inf.ed.ac.uk/plasmoweb/";
	public static final String PROJECT_BASE_URL = bundle.getString("PROJECT_BASE_URL");//"http://localhost/plasmoweb/";
	
	public static final String SUBMITTED_FILES_URL = PROJECT_BASE_URL + bundle.getString("SUBMITTED_FILES_URL");//"tmpsubmitted/";
	//public static final String SUBMITTED_FILES_URL = PROJECT_BASE_URL + "portal_data/";
	
	public static final String SUBMITTED_DATA_FILES_URL = SUBMITTED_FILES_URL + "data/";
	
	public static final String THUMBNAILS_URL_SUFFIX = "thumbnails/";
	
	public static final int THUMBNAIL_DIMENSION = 130;
	
	public static final String DB_ID_PREFIX = "PLM";
	
	public static final String MAIL_SERVER = "mail";
	
	public static final boolean PWD_ENCRYPTED = true;
	
	public static final String RECAPTCHA_PRIVATE_KEY = bundle.getString("RECAPTCHA_PRIVATE_KEY");
	public static final String RECAPTCHA_PUBLIC_KEY = bundle.getString("RECAPTCHA_PUBLIC_KEY");
	
	public static final String ADMIN_EMAIL = "admin@web";
        public static final String TOOL_EMAIL = "admin@web";
        public static final String TOOL_NAME = "BioDare";
	
	public static final String SIMILEWEB_PLASMO_URL = "http://www.simileweb.com/models/plasmo/";
    
        /**
         * Name of the xslt file that transforms models upond download to insert their plasmo identifiers
         */
        public static final String ANNOTATIONS_TEMPLATE = "annotation.xsl";

        /**
         * Name of the xslt file that transforms models upond download to remove unnecessary annotations
         */
        public static String SIMPLIFICATION_TEMPLATE = "simplification.xsl";
        
        /**
         * Name of the XSTL parameter in tranforamtation template that stores plasmo id
         */
        public static final String XSLT_ACC_PARAM = "PLASMO_ACC";
    
        /**
         * Name of the XSTL parameter in tranforamtation template that stores model version
         */
        public static final String XSLT_VER_PARAM="PLASMO_VER";
        
	
	public static final String [] getBrowseModelsHeaderTitles() {
		return new String [] {Constants.PROJECT_NAME+ " Entry Details", "Name", "Submission Date", "Submitted By"};
	}
	
}
