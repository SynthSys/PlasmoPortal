package uk.ac.ed.plasmo.db;

/**
 * List of data constants relevant to the database. Used by DAOs,
 * for example, when uploading data to ensure the user has not tried to submit
 * data which contains more characters that the database table column being
 * submitted to permits. 
 * @author ctindal
 *
 */
public class DataConstants {
	
	//model
	public static final int MAX_ACCESSION_LENGTH = 15;
	public static final int MODEL_NAME = 150;
	
	//model version
	public static final int MAX_VERSION_NO = 32767;
	public static final int MIN_VERSION_NO = -32768;
	
	//all files (images, supp files, style sheets, schemas)
	public static final int FILE_SHORT_DESCRIPTION = 500;
	public static final int FILE_FILENAME = 150;
	public static final int FILE_DESCRIPTION = 2000;
	
	//attribute
	//public static final int ATTRIBUTE_VALUE = 7000;
	public static final int ATTRIBUTE_NAME = 150;
	
	//publications
	public static final int PUBLICATION_AUTHORS = 500;
	public static final int PUBLICATION_PERIODICAL_NAME = 200;
	public static final int PUBLICATION_TITLE = 200;
	//public static final int PUBLICATION_ABSTRACT = 7000;
	public static final int PUBLICATION_PUBLISHER = 255;
	public static final int PUBLICATION_PAGES = 15;
	public static final int PUBLICATION_ISBN = 15;
	public static final int PUBLICATION_URL = 255;
	
	//model version
	//public static final int VERSION_COMMENT = 7000;
	
	//model comments
	//public static final int MODEL_COMMENT = 7000;
	
	//user table
	public static final int MAX_USER_USERNAME_LENGTH = 75;
	public static final int MAX_USER_PASSWORD_LENGTH = 75;
	public static final int USER_UUID_LENGTH = 36;
	public static final int MAX_USER_EMAIL_LENGTH = 80;
	
	//format
	public static final int FORMAT_NAME = 150;
	 
}
