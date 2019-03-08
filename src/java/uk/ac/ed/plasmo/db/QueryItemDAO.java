package uk.ac.ed.plasmo.db;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ed.plasmo.entity.ModelData;
import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.entity.VersionSubmission;
import uk.ac.ed.plasmo.persistence.RefGroup;

/**
 * DAO responsible for querying database data 
 * @author ctindal
 *
 */
public interface QueryItemDAO {
	
	/**
	 * retrieves a list of all the model formats from the database
	 * @return a list of all model formats in the database
	 */
	public List<String> getAllModelFormats();
	
	/**
	 * get the location of the schema file for a specified format
	 * @param formatName the name of the format
	 * @return if a schema exists, return the location on the file system of the schema as a string;
	 * if no schema file exists, return an empty string;
	 * if no entry can be found for the specified format name, return null
	 */
	public String getSchemaLocation(String formatName);
	
	/**
	 * gets the number of models in the database
	 * @param user the user accessing the list of models
	 * @return
	 */
	public int getTotalNumberOfModels(User user);
	
	/**
	 * get info on all the models in the database for browsing
	 * @param columnIndex
	 * @param ascending flag to indicate whether the set is order by ascending or descending value
	 * @param offset
	 * @param num
	 * @param user the user querying the database (required to gain retrieve relevant
	 * private models as part of the result set) 
	 * @return
	 */
	public ArrayList<String []> browseModels(int columnIndex, boolean ascending, int offset, int num, User user);
	
	/**
	 * gets the number of models submitted by a specific user
	 * @param user
	 * @return
	 */
	public int getTotalNumberOfUserModels(User user);
	
	/**
	 * get model meta data for all models submitted by a specific user
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @param user
	 * @return
	 */
	public ArrayList<String []> browseUsersModels(int columnIndex, boolean ascending, int offset, int num, User user);
	
	/**
	 * gets a model from the database and returns it as type InputStream
	 * @param accession
	 * @param version
	 * @return
	 */
        
	/**
	 * gets a model from the database and returns it as a String 
	 * @param accession
	 * @param version if null most recent version will be obtained
	 * @return
	 */
	//public String getModelAsString(String accession, String version);

	/**
	 * gets a model from the database and returns it as a String 
	 * @param accession
	 * @param version if null most recent version will be obtained
	 * @return
	 */
	public String getModelAsString(String accession, int version);
        
        /**
         * Gives db id of the required version
         * @param accession
         * @param version
         * @return 
         */
        public int getVersionId(String accession, String version);
        
	/**
	 * gets data about a model format from the database
	 * @param accession the accession id of the model
	 * @param version the version of the model
	 * @return id of the format assigned to the model
	 */
	public long getFormatId(String accession, int versionId);
        
	/**
	 * gets data about a model from the database for display to user
	 * @param accession the accession id of the model
	 * @param version the version of the model
	 * @return ModelData an object containing all the data about a model
	 */
	public ModelData getModelData(String accession, String version, User user);
	
	/**
	 * gets all the comments for a specific model from the database
	 * @param accession
	 * @return
	 */
	public ArrayList<String[]> getModelComments(String accession);
	
	/**
	 * searches for models based on a specific query parameter
	 * @param queryParam
	 * @param columnIndex
	 * @param ascending
	 * @param offset
	 * @param num
	 * @param user
	 * @return
	 */
	public ArrayList<String []> searchModels(String queryParam, int columnIndex, boolean ascending, int offset, int num, User user);
	
	/**
	 * gets the number of models in the database from specific search
	 * @param user the user accessing the list of models
	 * @param queryParam the query string used to search for models
	 * @return
	 */
	public int getTotalModelsFromSearch(User user, String queryParam);
	
	/**
	 * gets the name of a specific model based on it's unique accession
	 * @param accession the model's unique accession id
	 * @return the name of the model
	 */
	public String getModelNameFromAccession(String accession);
	
	/**
	 * get all relevant model data for display to a user who is about to submit a new 
	 * version of a model
	 * @param accession the model's unique accession id
	 * @return
	 */
	public ModelData getModelDataForNewVersionDisplay(String accession);
	
	/**
	 * gets meta data about a specific version of a model to be displayed to
	 * the user to allow them to modify and resubmit the data
	 * @param accession the accession no. of the model
	 * @param version the version no. of the model
	 * @return
	 */
	public VersionSubmission getModelDataForEditVersionDisplay(String accession, String version);
	
	/**
	 * gets meta data about a specific model to be displayed to the
	 * user to allow them to modify and resubmit the relevant data
	 * @param accession the accesssion no. of the model
	 * @return
	 */
	public ModelSubmission getModelDataForEditModelDisplay(String accession);
	
	/**
	 * gets the file system location from the database of an xsl file used to transform a
	 * specific model from it's original xml into another format
	 * @param id the database id of the xsl file location
	 * @param accession the accession id of the model
	 * @param version the version of the model
	 * @return
	 */
	public String getXSLTFileLocForModelVersion(String id, String accession, String version);
	
	/**
	 * gets a list of the ids of all the public models in the datbase
	 * @return
	 */
	public ArrayList<String> getAllModelIds();
	
	/**
	 * gets the name of a model using it's accession id
	 * @param accession the accession id of the model
	 * @return
	 */
	public String getModelName(String accession);
	
	/**
	 * gets a list of ids of all the public models in the database matching a specific name
	 * @param name the name of the model
	 * @return
	 */
	public ArrayList<String> getModelIdsByName(String name);
	
	/**
	 * gets a list of ids of all the public models in the database of a specific format
	 * @param format the format of the model
	 * @return
	 */
	public ArrayList<String> getModelIdsByFormat(String format);
	
	/**
	 * retrieves a list of all the user groups in the database 
	 * @return
	 */
	public List<RefGroup> getUserGroups();

    public List<Publication> getModelPublications(String accession);

    public Publication getPublication(String accession, long pubOID);
}
