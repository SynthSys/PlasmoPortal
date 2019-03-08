package uk.ac.ed.plasmo.db;

import java.io.File;
import java.util.HashMap;

import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.entity.VersionSubmission;
import uk.ac.ed.plasmo.entity.WSModelSubmission;

/**
 * DAO responsible for adding new data to the database 
 * @author ctindal
 *
 */
public interface NewItemDAO {
	
	/**
	 * adds a new model format to the database
	 * @param <b>format</b> the name of the model format
	 * @param <b>description</b> optional description of the model format
	 * @param <b>schema</b> optional schema defining the model format
	 * @param <b>schemaFileName</b> the original name of uploaded schema file
	 * @param <b>user</b> the person submitting the format
	 * @return a String containing any error messages or null if the operation was successful
	 */
	public String addNewModelFormat(String format, String description, File schema, String schemaFileName, User user);
	
	/**
	 * adds a model file to an interim table in the database. 
	 * The model is housed here until the user completes their submission when
	 * it is moved to a models table 
	 */
	public String addInterimModel(File model, String format);
	
	/**
	 * adds a new model to the database
	 * @param submission
	 * @param user
	 * @return
	 */
	public String addNewModel(WSModelSubmission submission, User user);
	
	/**
	 * adds a model stored in an interim table to the database to a permanent store. 
	 * Meta data is also added to the database
	 * 
	 */
	public String completeModelSubmission(ModelSubmission submission, User user);
	
	/**
	 * add a new version of a model to the db
	 */
	public String addNewModelVersion(VersionSubmission submission, User user);
	
	/**
	 * edits a version of a model already submitted to the db
	 * @param submission contains the data to be used to edit the current version
	 * @param user the original submitter of the model/version
	 */
	public String editModelVersion(VersionSubmission submission, User user);
	
	/**
	 * edits model meta data which is relevant to the model as a whole (i.e. not specific to 
	 * a particular version of a model) storing changes made in the database.
	 * @param submission contains meta data about a specific model
	 * @return
	 */
	public String editModel(ModelSubmission submission);
	
	/**
	 * adds a user comment about a model to the database
	 * @param accession the model's accession no.
	 * @param comment the comment about the model
	 * @param user the user who made the comment
	 */
	public void addModelComment(String accession, String comment, User user);
	
	/**
	 * adds a new user to the database
	 */
	public HashMap<Boolean, Object> createNewUser(User user);

    public void updatePublication(Publication publication, String accession);

    public void insertPublication(Publication publication, String accession);

    public void removePublication(Publication publication, String accession);
}
