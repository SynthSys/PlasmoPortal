package uk.ac.ed.plasmo.db;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.josephoconnell.html.HTMLInputFilter;
import com.liferay.portal.security.pwd.PwdEncryptor;
import java.util.Collection;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closeConnection;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closePreparedStatement;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.closeResultSet;
import static uk.ac.ed.plasmo.db.RelationalDBDAO.getConnection;

import uk.ac.ed.plasmo.entity.Attribute;
import uk.ac.ed.plasmo.entity.DataFile;
import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.entity.VersionSubmission;
import uk.ac.ed.plasmo.entity.WSModelSubmission;
import uk.ac.ed.plasmo.utility.Constants;
import uk.ac.ed.plasmo.utility.FileUtility;
import uk.ac.ed.plasmo.utility.ImageUtility;

/**
 * MySQL specific implementation of {@link NewItemDAO}
 * @author ctindal
 *
 */
public class MySQLNewItemDAOImpl extends RelationalDBDAO implements NewItemDAO {
	
	
	@Override
	public HashMap<Boolean, Object> createNewUser(User user) {

		if(user == null) {
			return null;
		}
		
		Connection conn = getConnection();
		Statement stmt = null;
		PreparedStatement prepStat = null;
        ResultSet resSet = null;
        
        ParamQuery parQ = null;
        
        try {   
        	
        	HashMap<Boolean, Object> result = new HashMap<>();
        	
        	boolean created = true;
        	
        	conn.setAutoCommit(false);
			
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        	
        	stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	
        	stmt.execute(MySQLQuery.XLOCK_USER);
        	
        	parQ = MySQLQuery.getParamQuery("USER_BY_USERNAME");
        	
			prepStat = parQ.getPrepStat(conn);
			
			prepStat.setString(1, user.getUserName());
			
			resSet = prepStat.executeQuery();
			
			ArrayList<String> errors = new ArrayList<>();
			
			if(resSet.first()) {
				errors.add("The selected username is already being used by another user.");
				created = false;
			}
			
			parQ = MySQLQuery.getParamQuery("USER_BY_EMAIL");
			
			prepStat = parQ.getPrepStat(conn);
			
			prepStat.setString(1, user.getEmail());
			
			resSet = prepStat.executeQuery();
			
			if(resSet.first()) {
				errors.add("The email you have provided is already being used by another user.");
				created = false;
			}
			
			if(created) {
				parQ = MySQLUpdate.getParamQuery("NEW_USER");
				
				prepStat = parQ.getPrepStat(conn);
				
				String uuid = UUID.randomUUID().toString();
				
				prepStat.setString(1, user.getGiven());
				prepStat.setString(2, user.getFamily());
				prepStat.setString(3, user.getEmail());
				prepStat.setString(4, user.getOrganisation());
				prepStat.setString(5, user.getUserName());
				prepStat.setString(6, PwdEncryptor.encrypt(user.getPassword()));
				prepStat.setString(7, uuid);
				
				prepStat.executeUpdate();
				
				result.put(created, uuid);
			}
			else {
				result.put(created, errors);
			}
			
			conn.commit();
                        stmt.executeQuery(MySQLQuery.XUNLOCK);
			conn.setAutoCommit(true);

			return result;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public String addNewModelFormat(String format, String description, File schema, String schemaFileName, User user) {
		Connection conn = getConnection();
		
        PreparedStatement prepStat = null;
        ResultSet resSet = null;
		
		try {

			//check if there is already a model in the database with the same name
			ParamQuery parQ = MySQLQuery.getParamQuery("MODEL_FORMAT_NAME");
			
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, format.toUpperCase());
			
			resSet = prepStat.executeQuery();
			
			//if a model with this name already exists, alert user
			if(resSet.first()) {
				//commit the transaction
				//conn.commit();
				//conn.setAutoCommit(true);
				return "The model format name \""+format+"\" is already in use. Please specify another format name.";
			}
			
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			prepStat.clearParameters();
			
			//add new format details to the database
			parQ = MySQLUpdate.getParamQuery("NEW_MODEL_FORMAT");
			prepStat = parQ.getPrepStat(conn);
			
			prepStat.setString(1, format);
			if(schema != null)
				prepStat.setString(2, schemaFileName);
			else {
				prepStat.setString(2, null);
			}
			prepStat.setString(3, description);
			prepStat.setString(4, user.getUserName());
			
			prepStat.executeUpdate();
			
			//if the user has specified a schema for this format
			if(schema != null) {
				
				//need to add the schema to the file system under a unique directory so get the id for the schema
				parQ = MySQLQuery.getParamQuery("FORMAT_ID_FROM_NAME");
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, format);
				resSet = prepStat.executeQuery();
				//move to first row of result set
				resSet.first();
				
				//use format id as a directory name and add file to directory
				String formatId = resSet.getString(1);
				String schemaDir = Constants.UPLOAD_SCHEMA_DIR + formatId + "/";
				FileUtility.createDirectory(schemaDir);
				FileUtility.uploadFile(schemaDir + schemaFileName, schema);
			}
			
			//commit the transaction
			conn.commit();
			conn.setAutoCommit(true);
		} catch (    SQLException | IOException e) {
			e.printStackTrace();
			return "An unexpected error occurred. Please try again";
		}
		finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
		return null;
	}
	
	@Override
	public String addInterimModel(File model, String format) {
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		Statement stmt = null;
        ResultSet resSet = null;
		
		try {
			String uniqueId = UUID.randomUUID().toString();
			//create param query to insert new model into db
			ParamQuery parQ = MySQLUpdate.getParamQuery("NEW_INTERIM_MODEL");
			
			prepStat = parQ.getPrepStat(conn);
			
			//set the file containing the model to a binary stream so it can be put in the database
			int fileLength = (int) model.length();
			InputStream is = (InputStream) new FileInputStream(model);
			
			//set the params
			prepStat.setString(1, uniqueId);
			prepStat.setBinaryStream(2, is, fileLength);
			prepStat.setString(3, format);
			
			
			prepStat.executeUpdate();
			
			//remove old models from the interim db table
			stmt = conn.createStatement();
			stmt.executeUpdate(MySQLUpdate.DELETE_OLD_INTERIM_MODELS);
			
			return uniqueId;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
	public String addNewModel(WSModelSubmission submission, User user) {
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		Statement stmt = null;

                ParamQuery parQ = null;
                String accessionId = null;
        
        try {
                        //int oid = 1;
                        long accessionNr = IdGenerator.getNextModelId();
                        String idString = ""+accessionNr;
                        accessionId = Constants.DB_ID_PREFIX+"_"+idString;
                        
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			//get the highest accession id for a model in the database. This value will be used to 
			//set the accession id of the model currently being added
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			/*stmt.execute(MySQLQuery.XLOCK_FULL_MODEL);
			
			resSet = stmt.executeQuery(MySQLQuery.MAX_MODEL_ACCESSION);
			
                        
			//if there is already a model in the database, get the numerical part of it's id
			if(resSet.first()) {
				oid = resSet.getInt(1) +1;
			}
			
			String idString = String.valueOf(oid);
			accessionId = Constants.DB_ID_PREFIX+"_"+idString;
                        */
			int version = 1;
			
			//create param query to insert new model into db
			parQ = MySQLUpdate.getParamQuery("NEW_MODEL");
			
			prepStat = parQ.getPrepStat(conn);
			
			//set the params
			prepStat.setString(1, accessionId);
			prepStat.setString(2, submission.getName());
			
			//check if the submission is private or public
			if(submission.isPrivate()) {
				prepStat.setInt(3, 0);
			}
			else {
				prepStat.setInt(3, 1);
			}
			prepStat.setString(4, user.getUserName());
			prepStat.setString(5, user.getUserName());
			
			prepStat.executeUpdate();
			
			//parQ = MySQLQuery.getParamQuery("MDL_ID_FROM_ACCESSION");
			//prepStat = parQ.getScrollablePrepStat(conn);
			//prepStat.setString(1, accessionId);
			
			//resSet = prepStat.executeQuery();
			
			//resSet.next();
			
			//create a new version for the model
			parQ = MySQLUpdate.getParamQuery("ADD_NEW_MODEL_VERSION");
			
			prepStat = parQ.getPrepStat(conn);
			
			//set the version params
			prepStat.setString(1, accessionId);
			prepStat.setInt(2, version);
			prepStat.setString(3, submission.getModelAsString());
			prepStat.setString(4, null);
			prepStat.setString(5, submission.getFormat());
			
			prepStat.executeUpdate();
			
			//add attribute data
			if(submission.getDescription() != null && !submission.getDescription().equals("")) {
				
				//remove unwanted tags
				submission.setDescription(new HTMLInputFilter().filter(submission.getDescription()));

				parQ = MySQLUpdate.getParamQuery("ADD_MODEL_ATTRIBUTE");
				
				prepStat = parQ.getPrepStat(conn);
			
				prepStat.setString(1, accessionId);
				prepStat.setString(2, "Description");
				prepStat.setString(3, submission.getDescription());
				
				prepStat.executeUpdate();
			}
			
			conn.commit();
                        //stmt.executeQuery(MySQLQuery.XUNLOCK);
			conn.setAutoCommit(true);
                        
			
			
			return accessionId;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		finally {
			//closeResultSet(resSet);
			closeStatement(stmt);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public String completeModelSubmission(ModelSubmission submission, User user) {
		
		//String uuid = UUID.randomUUID().toString();
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		Statement stmt = null;
                ResultSet resSet = null;
                ParamQuery parQ = null;
                String accessionId = null;
		
		try {
                        List<String> attributesNames = new ArrayList<>();
                        if (submission.getAttributes() != null ) {
                            for (Attribute att : submission.getAttributes()) 
                                if (att.getName() != null ) attributesNames.add(att.getName().trim());
                        }
                        addAttributesNames(attributesNames,conn);
                        
                        long accessionNr = IdGenerator.getNextModelId();
                        String idString = ""+accessionNr;
                        accessionId = Constants.DB_ID_PREFIX+"_"+idString;
                        
			//int oid = 1;
			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                        
                        int userId = user.getOid();
                         
			//get the highest accession id for a model in the database. This value will be used to 
			//set the accession id of the model currently being added
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			/*stmt.execute(MySQLQuery.XLOCK_FULL_MODEL);

			resSet = stmt.executeQuery(MySQLQuery.MAX_MODEL_ACCESSION);
			
			//if there is already a model in the database, get the numerical part of it's id
			if(resSet.first()) {
				oid = resSet.getInt(1) +1;
			}
			
			String idString = String.valueOf(oid);
			accessionId = Constants.DB_ID_PREFIX+"_"+idString;
                        */
                        
			int version = 1;
			//create param query to insert new model into db
			parQ = MySQLUpdate.getParamQuery("NEW_MODEL");
			
			prepStat = parQ.getPrepStat(conn);
			
			//set the params
			prepStat.setString(1, accessionId);
			prepStat.setString(2, submission.getName());
			//check if the submission is private or public
			if(submission.isPrivate()) {
				prepStat.setInt(3, 0);
			}
			else {
				prepStat.setInt(3, 1);
			}
			prepStat.setInt(4, userId);
			prepStat.setInt(5, userId);
			
			prepStat.executeUpdate();
			
			parQ = MySQLQuery.getParamQuery("MDL_ID_FROM_ACCESSION");
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, accessionId);
			
			resSet = prepStat.executeQuery();
			
			resSet.first();
			int modelOid = resSet.getInt(1);
			
			//conn.commit();
			
			//create a new version for the model
			parQ = MySQLUpdate.getParamQuery("NEW_MODEL_VERSION_1");
			
			prepStat = parQ.getPrepStat(conn);

			//set the version params
			prepStat.setString(1, accessionId);
			prepStat.setInt(2, version);
			prepStat.setString(3, submission.getTempId());
			prepStat.setString(4, submission.getTempId());
			
			prepStat.executeUpdate();

			//add attribute data
			if(submission.getDescription() != null && !submission.getDescription().equals("")) {
				
				//remove unwanted tags
				submission.setDescription(new HTMLInputFilter().filter(submission.getDescription()));

				parQ = MySQLUpdate.getParamQuery("ADD_MODEL_ATTRIBUTE");
				
				prepStat = parQ.getPrepStat(conn);
			
				prepStat.setString(1, accessionId);
				prepStat.setString(2, "Description");
				prepStat.setString(3, submission.getDescription());
				
				prepStat.executeUpdate();
			}
			
			//add user-defined model attributes to the db
			List<Attribute> attributes = submission.getAttributes();
			if(attributes != null && attributes.size() > 0) {

				for(int i=0;i<attributes.size();i++){
					
					Attribute att = attributes.get(i);
					//make sure there are no null values
					if(att!= null && att.getName() != null && !att.getName().trim().equals("") 
							&& att.getValue() != null && !att.getValue().trim().equals("")) {
						
                                                /*         
						stmt.execute(MySQLQuery.XLOCK_ATTRIBUTE_NAME);
						
						//check the attribtue name is in the db
						parQ = MySQLQuery.getParamQuery("ATTRIBUTE_NAME");
						parQ.setScrollablePrepStat(conn);
						prepStat = parQ.getPrepStat(conn);
						prepStat.setString(1, att.getName().trim());
						resSet = prepStat.executeQuery();
						
						//if the name isn't in the db, put it in
						if(!resSet.first()) {
                                                    
							parQ = MySQLUpdate.getParamQuery("ADD_ATTRIBUTE_NAME");
							
							prepStat = parQ.getPrepStat(conn);
							prepStat.setString(1, att.getName());
							prepStat.executeUpdate();
                                                      
                                                    //throw new RuntimeException("Cannot add attribute value, as attribute: "+att.getName()+" is not defined in the system");
						}
						*/
						String attValue = att.getValue();
						//remove unwanted/dangerous html
						attValue = new HTMLInputFilter().filter(attValue);
						
						//insert the attribute value into the db
						parQ = MySQLUpdate.getParamQuery("ADD_ATTRIBUTE_VALUE");
						
						prepStat = parQ.getPrepStat(conn);
						prepStat.setString(1, accessionId);
						prepStat.setString(2, att.getName());
						prepStat.setString(3, attValue);
						prepStat.executeUpdate();
						
						//conn.commit();
					}
					
				}
			}
			
			//delete the model from the interim space
			parQ = MySQLUpdate.getParamQuery("REMOVE_INTERIM_MODEL");
			
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, submission.getTempId());
			prepStat.executeUpdate();
			
			//create a param query object to hold dynamically created param query strings
			//ParamQuery dParQ = new ParamQuery("", "");
			int numParams;
			
			//set the group read permissions on this model, if any 
			int [] accessGroupIds = submission.getAccessGroupIds();
			if(accessGroupIds != null && accessGroupIds.length > 0) {
				
				StringBuilder accessGroupsInsert = new StringBuilder(MySQLUpdate.ADD_MODEL_GROUP_READ_ACCESS_1);
				for(int i=0;i<accessGroupIds.length;i++) {
					accessGroupsInsert.append(MySQLUpdate.ADD_MODEL_GROUP_READ_ACCESS_2);
					if(i != accessGroupIds.length-1) {
						accessGroupsInsert.append(", ");
					}
				}
				//dParQ.setQuerySQL(accessGroupsInsert.toString());
				//dParQ.setPrepStat(conn);
				prepStat =  conn.prepareStatement(accessGroupsInsert.toString());//dParQ.getPrepStat(conn);
				//set the sql params
				numParams = 3;
				for(int i=0;i<accessGroupIds.length;i++) {
					prepStat.setInt((i*numParams)+1, modelOid);
					prepStat.setInt((i*numParams)+2, accessGroupIds[i]);
					prepStat.setString((i*numParams)+3, "read");
				}
				
				prepStat.executeUpdate();
			}
						
			StringBuilder publicationInsert = new StringBuilder("");
			//add the publication data to the database
			List<Publication> publs =  submission.getPublications();
			
			//add publication data, if any, to database
			if(publs != null && publs.size() > 0) {
			
			publicationInsert.append(MySQLUpdate.ADD_PUBLICATION_1);
			//need to set the params for the query based on the no. of publications submitted
			for(int i=0;i<publs.size();i++){
				publicationInsert.append(MySQLUpdate.ADD_PUBLICATION_2);
				if(i != publs.size()-1) {
					publicationInsert.append(", ");
				}
			}
			//dParQ.setQuerySQL(publicationInsert.toString());
			//dParQ.setPrepStat(conn);
			prepStat = conn.prepareStatement(publicationInsert.toString());// dParQ.getPrepStat(conn);
			numParams = 13;
			//set the params for each publication
			for(int i=0;i<publs.size();i++) {
				
				Publication pub = publs.get(i);
				
				prepStat.setString((i*numParams)+1, pub.getAuthors());
				prepStat.setString((i*numParams)+2, pub.getYear());
				prepStat.setString((i*numParams)+3, pub.getPeriodicalName());
				prepStat.setString((i*numParams)+4, pub.getTitle());
				prepStat.setString((i*numParams)+5, pub.getAbstract());
				prepStat.setString((i*numParams)+6, pub.getSecondaryAuthors());
				prepStat.setString((i*numParams)+7, pub.getSecondaryTitle());
				prepStat.setString((i*numParams)+8, pub.getPublisher());
				prepStat.setString((i*numParams)+9, pub.getPages());
				prepStat.setString((i*numParams)+10, pub.getIsbn());
				prepStat.setString((i*numParams)+11, pub.getUrl());
				prepStat.setString((i*numParams)+12, pub.getReferenceType());
				prepStat.setString((i*numParams)+13, accessionId);
			}
			
			prepStat.executeUpdate();
			}
			
			
			/*--add image info to the database--*/
			List<DataFile> imgs = submission.getImages();
			if(imgs != null) {
				for(int i=0;i<imgs.size();i++) {
					DataFile df = imgs.get(i);
					if(df == null) {
						imgs.remove(i);
						i--;
					}
					else {
						if(df.getDataFile() == null) {
							imgs.remove(i);
							i--;
						}
					}
				}
			}
			
			//if the user has submitted image(s)
			if(imgs != null && imgs.size() > 0){
				
				//stmt.execute(MySQLQuery.XLOCK_IMAGE);
				/*resSet = stmt.executeQuery(MySQLQuery.MAX_IMAGE_OID);
				
				int oid = 1;
				if(resSet.first()) {
					oid = resSet.getInt(1) +1;
				}*/
				
				StringBuilder imageInsert = new StringBuilder("");
				StringBuilder imageVersionInsert = new StringBuilder("");
				
				imageInsert.append(MySQLUpdate.ADD_IMAGE_1);
				imageVersionInsert.append(MySQLUpdate.ADD_VERSION_IMAGE_1);
				
				//need to set the params for the query based on the no. of images submitted
				for(int i=0;i<imgs.size();i++) {
					imageInsert.append(MySQLUpdate.ADD_IMAGE_2);
					imageVersionInsert.append(MySQLUpdate.ADD_VERSION_IMAGE_2);
					if(i != imgs.size()-1) {
						imageInsert.append(", ");
						imageVersionInsert.append(", ");
					}
				}
				
				//dParQ.setQuerySQL(imageInsert.toString());
				//dParQ.setPrepStat(conn);			
				prepStat = conn.prepareStatement(imageInsert.toString());// dParQ.getPrepStat(conn);
				numParams = 5;
				
                                List<Long> imgIds = new ArrayList<>();
                                
				//set the sql params for each image 
				for(int i=0;i<imgs.size();i++) {
					DataFile df = imgs.get(i);
					
                                        long imgId = IdGenerator.getNextImageId();
                                        imgIds.add(imgId);
					//set the name of the image
					df.setDataFileFileName(String.valueOf(imgId)+df.getDataFileFileName());				
					imgs.set(i, df);
					
					//set sql params
					prepStat.setLong((i*numParams)+1, imgId);
					prepStat.setString((i*numParams)+2, df.getDescription());
					prepStat.setString((i*numParams)+3, Constants.DB_ID_PREFIX+idString+"/images/");
					prepStat.setString((i*numParams)+4, df.getDataFileFileName());
					prepStat.setInt((i*numParams)+5, modelOid);
				}
				
				prepStat.executeUpdate();
				//conn.commit();
				
				//tie the model images to the version of the model
				//dParQ.setQuerySQL(imageVersionInsert.toString());
				//dParQ.setPrepStat(conn);
				prepStat = conn.prepareStatement(imageVersionInsert.toString());// dParQ.getPrepStat(conn);
				//set the sql params
				numParams = 3;
				for(int i=0;i<imgs.size();i++) {
					prepStat.setInt((i*numParams)+1, modelOid);
					prepStat.setInt((i*numParams)+2, version);
					prepStat.setLong((i*numParams)+3, imgIds.get(i));
				}
				
				prepStat.executeUpdate();
				
				//directory name to contain files associated with this submission
				String modelDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX+idString;
				//create the base directory to house model image and supplementary data files
				FileUtility.createDirectory(modelDir);
				//TODO - common code - create new method?
				//add each image to the file system
				FileUtility.createDirectory(modelDir+"/images/");
				FileUtility.createDirectory(modelDir+"/images/"+Constants.THUMBNAILS_URL_SUFFIX);
				for(int i=0;i<imgs.size();i++) {
					DataFile df = imgs.get(i);
					FileUtility.uploadFile(modelDir+"/images/"+df.getDataFileFileName(), df.getDataFile());
					BufferedImage img = ImageIO.read(new File(modelDir+"/images/"+df.getDataFileFileName()));
					int [] dimensions = ImageUtility.getTargetDimensions(img, Constants.THUMBNAIL_DIMENSION);
					img = ImageUtility.resize(img, dimensions[0], dimensions[1], true);
					ImageUtility.writeImageToFile(img, "png", modelDir+"/images/"+Constants.THUMBNAILS_URL_SUFFIX, df.getDataFileFileName());
				}
			}
			
			
			//add supplementary data file info to the database
			List<DataFile> supFiles = submission.getSupplementaryFiles();
			if(supFiles != null) {
				for(int i=0;i<supFiles.size();i++) {
					DataFile df = supFiles.get(i);
					if(df == null) {
						supFiles.remove(i);
						i--;
					}
					else {
						if(df.getDataFile() == null) {
							supFiles.remove(i);
							i--;
						}
					}
				}
			}
			
			//need to set the params for the query based on the no. of files submitted
			if(supFiles != null && supFiles.size() > 0){
				
				//stmt.execute(MySQLQuery.XLOCK_SUPPL_FILE);
				/*resSet = stmt.executeQuery(MySQLQuery.MAX_SUP_FILE_OID);
				int oid = 1;
				if(resSet.first()) {
					oid = resSet.getInt(1) +1;
				}*/
				
				StringBuilder supFileInsert = new StringBuilder("");
				StringBuilder supFileVersionInsert = new StringBuilder("");
				
				supFileInsert.append(MySQLUpdate.ADD_SUP_FILE_1);
				supFileVersionInsert.append(MySQLUpdate.ADD_VERSION_SUP_FILE_1);
				for(int i=0;i<supFiles.size();i++) {
					supFileInsert.append(MySQLUpdate.ADD_SUP_FILE_2);
					supFileVersionInsert.append(MySQLUpdate.ADD_VERSION_SUP_FILE_2);
					if(i != supFiles.size()-1) {
						supFileInsert.append(", ");
						supFileVersionInsert.append(", ");
					}
				}
				
				//dParQ.setQuerySQL(supFileInsert.toString());
				//dParQ.setPrepStat(conn);
				prepStat = conn.prepareStatement(supFileInsert.toString());//dParQ.getPrepStat(conn);
				//set the sql params for each file
				numParams = 5;
                                
                                List<Long> supIds = new ArrayList<>();
				for(int i=0;i<supFiles.size();i++) {
					DataFile df = supFiles.get(i);
					
                                        long supId = IdGenerator.getNextFileId();
                                        supIds.add(supId);
					//set the name of the file
					df.setDataFileFileName(String.valueOf(supId)+df.getDataFileFileName());
					supFiles.set(i, df);
					
					//set sql params
					prepStat.setLong((i*numParams)+1, supId);
					prepStat.setString((i*numParams)+2, df.getDescription());
					prepStat.setString((i*numParams)+3, Constants.DB_ID_PREFIX+idString+"/data/");
					prepStat.setString((i*numParams)+4, df.getDataFileFileName());
					prepStat.setInt((i*numParams)+5, modelOid);
				}
				
				prepStat.executeUpdate();
				//conn.commit();
				//tie the supplementary files to the version of the model
				//dParQ.setQuerySQL(supFileVersionInsert.toString());
				//dParQ.setPrepStat(conn);
				prepStat = conn.prepareStatement(supFileVersionInsert.toString());//dParQ.getPrepStat(conn);
				//set the sql params
				numParams = 3;

				for(int i=0;i<supFiles.size();i++) {
					prepStat.setInt((i*numParams)+1, modelOid);
					prepStat.setInt((i*numParams)+2, version);
					prepStat.setLong((i*numParams)+3, supIds.get(i));
				}
				prepStat.executeUpdate();
				
				//directory name to contain files associated with this submission
				String modelDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX+idString;
				//create the base directory to house model image and supplementary data files
				FileUtility.createDirectory(modelDir);
				//add each file to the file system
				FileUtility.createDirectory(modelDir+"/data/");
				for(int i=0;i<supFiles.size();i++) {
					DataFile df = supFiles.get(i);
					FileUtility.uploadFile(modelDir+"/data/"+df.getDataFileFileName(), df.getDataFile());
				}	
			}
			//commit the transaction
			conn.commit();
                        //stmt.execute(MySQLQuery.XUNLOCK);
			conn.setAutoCommit(true);
			
			//return the id of the submission
			return accessionId;
			
		} catch (SQLException | IOException e) {
			e.printStackTrace(System.out);
			
			//because exception has occurred, need to delete the model if already submitted to the db
			if(accessionId != null) {
				parQ = MySQLUpdate.getParamQuery("DELETE_MODEL");
				try {
					
					prepStat = parQ.getPrepStat(conn);					
					//set the params
					prepStat.setString(1, accessionId);
					prepStat.executeUpdate();
					
					//also need to delete data directory for this model if it exists
					String dataDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX + accessionId.substring((Constants.DB_ID_PREFIX+"_").length());
					File dir = new File(dataDir);
					
					if(dir.exists()) {
						FileUtility.deleteDirectory(dir);
					}
					
					conn.commit();
					conn.setAutoCommit(true);
					return null;
				} catch (SQLException e1) {
					e1.printStackTrace(System.out);
					throw new RuntimeException(e1);
				}				
			}
			return null;

		} finally {
			closeResultSet(resSet);
			closePreparedStatement(prepStat);
			closeStatement(stmt);
			closeConnection(conn);
		}
	}
	
	@Override
	public String editModel(ModelSubmission submission) {
		
		if(submission == null) {
			return "An unexpected error occurred. Please try again";
		}
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		Statement stmt = null;
        ResultSet resSet = null;
        
        try {
            
                        List<String> attributesNames = new ArrayList<>();
                        if (submission.getAttributes() != null ) {
                            for (Attribute att : submission.getAttributes()) 
                                if (att.getName() != null ) attributesNames.add(att.getName().trim());
                        }
                        addAttributesNames(attributesNames,conn);
            
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			String name = submission.getName();
			if(name == null || name.trim().equals("")) {
				return "Unable to update model.";
			}
			else {
				ParamQuery parQ = MySQLUpdate.getParamQuery("UPDATE_MODEL_NAME_AND_STATUS");
				
				
				prepStat = parQ.getPrepStat(conn);
				
				prepStat.setString(1, name);
				//set the status of the model
				boolean isPrivate = submission.isPrivate();
				//assume if no value is set, the model is public
				if(isPrivate) {
					prepStat.setInt(2, 0);
				}
				else {
					prepStat.setInt(2, 1);
				}
				prepStat.setString(3,submission.getAccession());
				
				int rowsUpdated = prepStat.executeUpdate();
				if(rowsUpdated == 0){
					conn.rollback();
					return "Unable to update model version.";
				}
				
				
				//delete group access data and update if required
				parQ = MySQLUpdate.getParamQuery("REMOVE_GROUP_READ_PRIVILEGES_FOR_MODEL");
				
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1,submission.getAccession());

				prepStat.executeUpdate();

				int [] accessGroupIds = submission.getAccessGroupIds();
				if(accessGroupIds != null && accessGroupIds.length > 0) {
					//create a param query object to hold dynamically created param query strings
					ParamQuery dParQ = new ParamQuery("", "");

					StringBuilder accessGroupsInsert = new StringBuilder(MySQLUpdate.ADD_MODEL_GROUP_READ_ACCESS_1);
					for(int i=0;i<accessGroupIds.length;i++) {
						accessGroupsInsert.append(MySQLUpdate.ADD_MODEL_GROUP_READ_ACCESS_3);
						if(i != accessGroupIds.length-1) {
							accessGroupsInsert.append(", ");
						}
					}
					//dParQ.setQuerySQL(accessGroupsInsert.toString());
					//dParQ.setPrepStat(conn);
					prepStat = conn.prepareStatement(accessGroupsInsert.toString());//dParQ.getPrepStat(conn);
					//set the sql params
					int numParams = 3;
					for(int i=0;i<accessGroupIds.length;i++) {
						prepStat.setString((i*numParams)+1, submission.getAccession());
						prepStat.setInt((i*numParams)+2, accessGroupIds[i]);
						prepStat.setString((i*numParams)+3, "read");
					}
					prepStat.executeUpdate();
				}
					

				
				//delete all attribute data currently associated with the model
				parQ = MySQLUpdate.getParamQuery("DELETE_MODEL_ATTRIBUTES");
				
				prepStat = parQ.getPrepStat(conn);
				prepStat.setString(1, submission.getAccession());
				
				prepStat.executeUpdate();
				
				
				//add attribute data
				if(submission.getDescription() != null && !submission.getDescription().equals("")) {
					
					//remove unwanted/dangerous html
					submission.setDescription(new HTMLInputFilter().filter(submission.getDescription()));
					
					parQ = MySQLUpdate.getParamQuery("ADD_MODEL_ATTRIBUTE");
					
					prepStat = parQ.getPrepStat(conn);
				
					prepStat.setString(1, submission.getAccession());
					prepStat.setString(2, "Description");
					prepStat.setString(3, submission.getDescription());
					
					prepStat.executeUpdate();
				}
				
				//add user-defined model attributes to the db
				List<Attribute> attributes = submission.getAttributes();
				if(attributes != null && attributes.size() > 0) {
					
					stmt = conn.createStatement();
					//stmt.execute(MySQLQuery.XLOCK_FULL_MODEL);
					
					for(int i=0;i<attributes.size();i++){
						Attribute att = attributes.get(i);
						//make sure there are no null values
						if(att!= null && att.getName() != null && !att.getName().trim().equals("") 
								&& att.getValue() != null && !att.getValue().trim().equals("")) {
							
                                                        /*
                                                        //check the attribtue name is in the db
							parQ = MySQLQuery.getParamQuery("ATTRIBUTE_NAME");
							parQ.setScrollablePrepStat(conn);
							prepStat = parQ.getPrepStat(conn);
							prepStat.setString(1, att.getName());
							resSet = prepStat.executeQuery();
							
							//if the name isn't in the db, put it in
							if(!resSet.first()) {
								parQ = MySQLUpdate.getParamQuery("ADD_ATTRIBUTE_NAME");
								
								prepStat = parQ.getPrepStat(conn);
								prepStat.setString(1, att.getName());
								prepStat.executeUpdate();
							}
							*/
							String attValue = att.getValue();
							//remove unwanted/dangerous html tags
							attValue = new HTMLInputFilter().filter(attValue);
							
							//insert the attribute value into the db
							parQ = MySQLUpdate.getParamQuery("ADD_ATTRIBUTE_VALUE");
							
							prepStat = parQ.getPrepStat(conn);
							prepStat.setString(1, submission.getAccession());
							prepStat.setString(2, att.getName().trim());
							prepStat.setString(3, attValue);
							prepStat.executeUpdate();
						}
					}
				}
			}
			
			//commit the transaction
			conn.commit();
                        if (stmt == null) conn.createStatement();
                        //stmt.execute(MySQLQuery.XUNLOCK);
			conn.setAutoCommit(true);
			//return value of 'null' indicates success
			return null;
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			return "An unexpected error occurred. Unable to update model.";
		}
		finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public String editModelVersion(VersionSubmission submission, User user) {
		
		if(submission == null || user == null) {
			return "An unexpected error occurred. Please try again";
		}
		
		//make sure the version no supplied is valid. If not, error
		int versionNo;
		String version = submission.getVersion();
		try {
			versionNo = Integer.parseInt(version);
			if(versionNo < DataConstants.MIN_VERSION_NO || versionNo > DataConstants.MAX_VERSION_NO){
				return "Unable to update model version.";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return "Unable to update model version. " + e.getMessage();
		}
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		Statement stmt = null;
                ResultSet resSet = null;
                ParamQuery parQ = null;
        
        try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			//System.out.println("\n\n\n\nvalue of comments: "+submission.getComments());
			
			File model = submission.getModel();
			
			//if the user has submitted a new model file
			if(model != null) {
				
				parQ = MySQLUpdate.getParamQuery("EDIT_VERSION_NEW_MODEL");
				
				prepStat = parQ.getPrepStat(conn);
				
				//set the file containing the model to a binary stream so it can be put in the database
				int fileLength = (int) model.length();
				InputStream is = (InputStream) new FileInputStream(model);
				
				//remove dangerous html
				String comments = submission.getComments();
				if(comments != null) {
					comments = new HTMLInputFilter().filter(comments);
				}
				
				prepStat.setBinaryStream(1, is, fileLength);
				prepStat.setString(2, comments);
				prepStat.setString(3, submission.getFormat());
				prepStat.setString(4,submission.getAccession());
				prepStat.setString(5, version);
			}
			//if the model is stored as a string
			else if (submission.getModelAsString() != null && !submission.getModelAsString().trim().equals("")) {
				parQ = MySQLUpdate.getParamQuery("EDIT_VERSION_NEW_MODEL");
				
				prepStat = parQ.getPrepStat(conn);
				
				//remove dangerous html
				String comments = submission.getComments();
				if(comments != null) {
					comments = new HTMLInputFilter().filter(comments);
				}
				
				prepStat.setString(1, submission.getModelAsString());
				prepStat.setString(2, comments);
				prepStat.setString(3, submission.getFormat());
				prepStat.setString(4,submission.getAccession());
				prepStat.setString(5, version);
			}
			//otherwise only update the comments field
			else {
				parQ = MySQLUpdate.getParamQuery("EDIT_VERSION_NO_MODEL");
				
				prepStat = parQ.getPrepStat(conn);
				
				//remove dangerous html
				String comments = submission.getComments();
				if(comments != null) {
					comments = new HTMLInputFilter().filter(comments);
				}
				
				prepStat.setString(1, comments);
				prepStat.setString(2,submission.getAccession());
				prepStat.setString(3, version);
			}
			
			//the number of rows updated will be > 0 if the query was successful
			//if not then the accession or version no were not valid values so return error message
			
			if(prepStat != null) {
				int rowsUpdated = prepStat.executeUpdate();
				if(rowsUpdated == 0){
					conn.rollback();
					return "Unable to update model version.";
				}
			}
			
			parQ = MySQLQuery.getParamQuery("MDL_ID_FROM_ACCESSION");
			prepStat = parQ.getPrepStat(conn);
			prepStat.setString(1, submission.getAccession());
			
			resSet = prepStat.executeQuery();
			
			resSet.first();
			int modelOid = resSet.getInt(1);
			
			//create a param query object to hold dynamically created param query strings
			//ParamQuery dParQ = new ParamQuery("", "");
			
			//id string is the accession id without the accession prefix
			String idString = submission.getAccession().substring((Constants.DB_ID_PREFIX+"_").length());
			
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
                        //stmt.execute(MySQLQuery.XLOCK_VERSION_MODEL);
			//list of new images to be associated with this version
			List<DataFile> imgs = submission.getImages();
			if(imgs != null && imgs.size() > 0) {

				/*--add image info to the database--*/
				//need to lock the table to get the max id and ensure concurrent users do not read the same max id
				//stmt.execute(MySQLQuery.XLOCK_IMAGE);
				
				//get the current max id in the image table
				/*resSet = stmt.executeQuery(MySQLQuery.MAX_IMAGE_OID);
				int imgOid = 1;
				if(resSet.first()) {
					imgOid = resSet.getInt(1) +1;
				}*/

				StringBuilder imageInsert = new StringBuilder("");
				StringBuilder imageVersionInsert = new StringBuilder("");

				imageInsert.append(MySQLUpdate.ADD_IMAGE_1);
				imageVersionInsert.append(MySQLUpdate.ADD_VERSION_IMAGE_1);
				//need to set the params for the query based on the no. of images submitted
				for(int i=0;i<imgs.size();i++) {
					imageInsert.append(MySQLUpdate.ADD_IMAGE_2);
					imageVersionInsert.append(MySQLUpdate.ADD_VERSION_IMAGE_2);
					if(i != imgs.size()-1) {
						imageInsert.append(", ");
						imageVersionInsert.append(", ");
					}
				}

				//dParQ.setQuerySQL(imageInsert.toString());
				//dParQ.setPrepStat(conn);			
				prepStat = conn.prepareStatement(imageInsert.toString());//dParQ.getPrepStat(conn);
				int numParams = 5;
                                
                                List<Long> imgIds = new ArrayList<>();
				//set the sql params for each image 
				for(int i=0;i<imgs.size();i++) {
					DataFile df = imgs.get(i);

                                        long imgId = IdGenerator.getNextImageId();
                                        imgIds.add(imgId);
					//set the name of the image
					df.setDataFileFileName(String.valueOf(imgId)+df.getDataFileFileName());				
					imgs.set(i, df);

					//set sql params
					prepStat.setLong((i*numParams)+1, imgId);
					prepStat.setString((i*numParams)+2, df.getDescription());
					prepStat.setString((i*numParams)+3, Constants.DB_ID_PREFIX+idString+"/images/");
					prepStat.setString((i*numParams)+4, df.getDataFileFileName());
					prepStat.setInt((i*numParams)+5, modelOid);
				}

				prepStat.executeUpdate();
				
				//tie the newly inserted model images to this version of the model
				//dParQ.setQuerySQL(imageVersionInsert.toString());
				//dParQ.setPrepStat(conn);
				prepStat = conn.prepareStatement(imageVersionInsert.toString());//dParQ.getPrepStat(conn);
				//set the sql params
				numParams = 3;
				for(int i=0;i<imgs.size();i++) {
					prepStat.setInt((i*numParams)+1, modelOid);
					prepStat.setString((i*numParams)+2, version);
					prepStat.setLong((i*numParams)+3, imgIds.get(i));
				}
				prepStat.executeUpdate();
				
				String modelDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX+idString;
				FileUtility.createDirectory(modelDir);
				FileUtility.createDirectory(modelDir+"/images/");
				FileUtility.createDirectory(modelDir+"/images/"+Constants.THUMBNAILS_URL_SUFFIX);
				//TODO - common code create new method?
				for(int i=0;i<imgs.size();i++) {
					DataFile df = imgs.get(i);
					FileUtility.uploadFile(modelDir+"/images/"+df.getDataFileFileName(), df.getDataFile());
					BufferedImage img = ImageIO.read(new File(modelDir+"/images/"+df.getDataFileFileName()));
    				int [] dimensions = ImageUtility.getTargetDimensions(img, Constants.THUMBNAIL_DIMENSION);
    				img = ImageUtility.resize(img, dimensions[0], dimensions[1], true);
    				ImageUtility.writeImageToFile(img, "png", modelDir+"/images/"+Constants.THUMBNAILS_URL_SUFFIX, df.getDataFileFileName());
				}
			}
			
			//add supplementary data file info to the database
			List<DataFile> supFiles = submission.getSupplementaryFiles();
			
			//make sure all data file objects in the array are valid. If not remove them.
			if(supFiles != null) {
				for(int i=0;i<supFiles.size();i++) {
					DataFile df = supFiles.get(i);
					if(df == null) {
						supFiles.remove(i);
						i--;
					}
					else {
						if(df.getDataFile() == null) {
							supFiles.remove(i);
							i--;
						}
					}
				}
			}
			
			//need to set the params for the query based on the no. of files submitted
			if(supFiles != null && supFiles.size() > 0){
				
				//stmt.execute(MySQLQuery.XLOCK_SUPPL_FILE);
				/*resSet = stmt.executeQuery(MySQLQuery.MAX_SUP_FILE_OID);
				int supOid = 1;
				if(resSet.first()) {
					supOid = resSet.getInt(1) +1;
				}*/
				
				StringBuilder supFileInsert = new StringBuilder("");
				StringBuilder supFileVersionInsert = new StringBuilder("");
				
				supFileInsert.append(MySQLUpdate.ADD_SUP_FILE_1);
				supFileVersionInsert.append(MySQLUpdate.ADD_VERSION_SUP_FILE_1);
				for(int i=0;i<supFiles.size();i++) {
					supFileInsert.append(MySQLUpdate.ADD_SUP_FILE_2);
					supFileVersionInsert.append(MySQLUpdate.ADD_VERSION_SUP_FILE_2);
					if(i != supFiles.size()-1) {
						supFileInsert.append(", ");
						supFileVersionInsert.append(", ");
					}
				}
				
				//dParQ.setQuerySQL(supFileInsert.toString());
				//dParQ.setPrepStat(conn);
				prepStat = conn.prepareStatement(supFileInsert.toString());//dParQ.getPrepStat(conn);
				//set the sql params for each file
				int numParams = 5;
                                List<Long> supIds = new ArrayList<>();
				for(int i=0;i<supFiles.size();i++) {
					DataFile df = supFiles.get(i);
					
                                        long supId = IdGenerator.getNextFileId();
                                        supIds.add(supId);
					//set the name of the file
					df.setDataFileFileName(String.valueOf(supId)+df.getDataFileFileName());
					supFiles.set(i, df);
					
					//set sql params
					prepStat.setLong((i*numParams)+1, supId);
					prepStat.setString((i*numParams)+2, df.getDescription());
					prepStat.setString((i*numParams)+3, Constants.DB_ID_PREFIX+idString+"/data/");
					prepStat.setString((i*numParams)+4, df.getDataFileFileName());
					prepStat.setInt((i*numParams)+5, modelOid);
				}
				
				prepStat.executeUpdate();
				
				//tie the supplementary files to the version of the model
				//dParQ.setQuerySQL(supFileVersionInsert.toString());
				//dParQ.setPrepStat(conn);
				prepStat = conn.prepareStatement(supFileVersionInsert.toString());//dParQ.getPrepStat(conn);
				//set the sql params
				numParams = 3;

				for(int i=0;i<supFiles.size();i++) {
					prepStat.setInt((i*numParams)+1, modelOid);
					prepStat.setString((i*numParams)+2, version);
					prepStat.setLong((i*numParams)+3, supIds.get(i));
				}
				prepStat.executeUpdate();
				
				String modelDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX+idString;
				
				//add each file to the file system
				FileUtility.createDirectory(modelDir);
				FileUtility.createDirectory(modelDir+"/data/");
				for(int i=0;i<supFiles.size();i++) {
					DataFile df = supFiles.get(i);
					FileUtility.uploadFile(modelDir+"/data/"+df.getDataFileFileName(), df.getDataFile());
				}	
			}
                        
			//commit the transaction
			conn.commit();
			conn.setAutoCommit(true);
			//return value of 'null' indicates success
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			return "An unexpected error occurred. Unable to update model version.";
		} catch (Exception e) {
                        /*try {
                            conn.rollback();
                        } catch (SQLException e2) {
                            e2.printStackTrace(System.out);
                        }*/
			e.printStackTrace(System.out);
			return "An unexpected error occurred. Unable to update model version.";
		} finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public void addModelComment(String accession, String comment, User user) {
		// TODO Auto-generated method stub
		
		if(accession == null || accession.length() > DataConstants.MAX_ACCESSION_LENGTH 
				|| comment == null || comment.trim().equals("") || user == null) {
			return;
		}
		
		Connection conn = getConnection();
		ParamQuery parQ = MySQLUpdate.getParamQuery("ADD_MODEL_COMMENT");
		PreparedStatement prepStat = null;
		
		try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			
			prepStat = parQ.getPrepStat(conn);
			
			prepStat.setString(1, comment);
			prepStat.setString(2, accession);
			prepStat.setString(3, user.getUserName());
			
			prepStat.executeUpdate();
			
			conn.commit();
			conn.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally {
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}
	
	@Override
	public String addNewModelVersion(VersionSubmission submission, User user) {
		
		if(submission == null || user == null) {
			return "An unexpected error occurred. Please try again";
		}
		
		Connection conn = getConnection();
		PreparedStatement prepStat = null;
		Statement stmt = null;
        ResultSet resSet = null;
        
        try {
        	
        	int versionNo = 1;
        	
        	//query to find the current maximum model version number
        	ParamQuery parQ = MySQLQuery.getParamQuery("MAX_MODEL_VERSION");
        	
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
        	prepStat = parQ.getPrepStat(conn);
        	
        	prepStat.setString(1, submission.getAccession());
        	resSet = prepStat.executeQuery();
        	
        	//if previous versions of the model already exist
        	if(resSet.first()) {
        		versionNo += resSet.getInt(1);
        		
        		//check if the specified format for this model exists in the db
        		//parQ = MySQLQuery.getParamQuery("MODEL_FORMAT_NAME");
    			
    			//parQ.setScrollablePrepStat(conn);
    			//prepStat = parQ.getPrepStat(conn);
    			//prepStat.setString(1, submission.getFormat().toUpperCase());
    			
    			//resSet = prepStat.executeQuery();
    			
    			//if the format is found, continue
    			//if(resSet.first()) {
    				//add the new model version to the db
    				parQ = MySQLUpdate.getParamQuery("ADD_NEW_MODEL_VERSION");
        			
        			prepStat = parQ.getPrepStat(conn);
        			
        			//remove dangerous html
    				String comments = submission.getComments();
    				if(comments != null) {
    					comments = new HTMLInputFilter().filter(comments);
    				}
        			
        			prepStat.setString(1, submission.getAccession());
        			prepStat.setInt(2, versionNo);
        			
        			//is the model a file or a String
        			if(submission.getModel() != null) {
        				//set the file containing the model to a binary stream so it can be put in the database
            			int fileLength = (int) submission.getModel().length();
            			InputStream is = (InputStream) new FileInputStream(submission.getModel());
            			prepStat.setBinaryStream(3, is, fileLength);
        			}
        			else if(submission.getModelAsString() !=null && !submission.getModelAsString().trim().equals("")) {
        				prepStat.setString(3, submission.getModelAsString());
        			}
        			else {
        				throw new SQLException();
        			}
        			prepStat.setString(4, comments);
        			prepStat.setString(5, submission.getFormat());
        			
        			prepStat.executeUpdate();
        			
        			parQ = MySQLQuery.getParamQuery("MDL_ID_FROM_ACCESSION");
        			prepStat = parQ.getPrepStat(conn);
        			prepStat.setString(1, submission.getAccession());
        			
        			resSet = prepStat.executeQuery();
        			
        			resSet.first();
        			int modelOid = resSet.getInt(1);
        			
        			//add the images for this version
        			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        			
        			//stmt.execute(MySQLQuery.XLOCK_VERSION_MODEL);
        			
        			/*resSet = stmt.executeQuery(MySQLQuery.MAX_IMAGE_OID);
        			int oid = 1;
        			if(resSet.first()) {
        				oid = resSet.getInt(1) +1;
        			}*/
                                
        			//id string is the accession id without the accession prefix
        			String idString = submission.getAccession().substring((Constants.DB_ID_PREFIX+"_").length());
        			//create a param query object to hold dynamically created param query strings
        			//ParamQuery dParQ = new ParamQuery("", "");
        			
        			//string to contain the query to add images to the db
        			StringBuilder imageInsert = new StringBuilder("");
        			
        			//list of all image ids for this model version
        			ArrayList<Long> imgIds = new ArrayList<>();
        			List<DataFile> imgs = submission.getImages();
        			
        			//if the user has chosen to upload new images
        			if(imgs != null && imgs.size() > 0) {
        				imageInsert.append(MySQLUpdate.ADD_IMAGE_1);
        				
            			//need to set the params for the query based on the no. of new images uploaded
            			for(int i=0;i<imgs.size();i++) {
            				imageInsert.append(MySQLUpdate.ADD_IMAGE_2);
            				if(i != imgs.size()-1) {
            					imageInsert.append(", ");
            				}
            			}
            			
            			//dParQ.setQuerySQL(imageInsert.toString());
            			//dParQ.setPrepStat(conn);			
            			prepStat = conn.prepareStatement(imageInsert.toString());//dParQ.getPrepStat(conn);
            			int numParams = 5;
            			
                                //List<Long> imgIds = new A
            			//set the sql params for each image 
            			for(int i=0;i<imgs.size();i++) {
            				
                                        long imgId = IdGenerator.getNextImageId();
            				//add the id of this image to the list of all image ids for this version
            				imgIds.add(imgId);
            				
            				DataFile df = imgs.get(i);
            				
            				//set the name of the image
            				df.setDataFileFileName(String.valueOf(imgId)+df.getDataFileFileName());				
            				imgs.set(i, df);
            				
            				//set sql params
            				prepStat.setLong((i*numParams)+1, imgId);
            				prepStat.setString((i*numParams)+2, df.getDescription());
            				prepStat.setString((i*numParams)+3, Constants.DB_ID_PREFIX+idString+"/images/");
            				prepStat.setString((i*numParams)+4, df.getDataFileFileName());
            				prepStat.setInt((i*numParams)+5, modelOid);
            			}
            			prepStat.executeUpdate();
            			//conn.commit();
        			}
        			
        			/*execute query to associate new images and preselected images with this version*/ 
        			
        			//first get ALL the image ids for this model
        			parQ = MySQLQuery.getParamQuery("MODEL_IMAGES");
        			
        			prepStat = parQ.getPrepStat(conn);
        			prepStat.setString(1, submission.getAccession());
        			
        			resSet = prepStat.executeQuery();
        			
        			ArrayList<Long> allImgIds = new ArrayList<>();
        			while(resSet.next()) {
        				allImgIds.add(resSet.getLong(1));
        			}
        			
        			//get the list of user-selected images already associated with the model
        			String [] selectedImgIds = submission.getImageIds();
        			//if the user has selected images
        			if(selectedImgIds != null && selectedImgIds.length > 0) {
        				//for each selected image id, check that it exists  
        				//in the db and it is associated with this particular model
        				for(int i=0;i<selectedImgIds.length;i++){
        					//convert the string to an integer
        					long id = -1;
        					try {
        						id = Long.parseLong(selectedImgIds[i]);
        					}
        					catch (NumberFormatException e){
        						conn.rollback();
        						conn.setAutoCommit(true);
        						throw new SQLException("Img id was not Long");
        						//return "An unexpected error occurred. Please try again";
        					}
        					//if the id is one of the list of all ids and 
        					//it is not already in the list of image ids to be added, add it to the list
        					if(allImgIds.contains(id) && !imgIds.contains(id)) {
        						imgIds.add(id);
        					}
        					
        				}
        			}
        			
        			//if there are ids in the list, add them to the db
        			if(imgIds.size() > 0) {
        				StringBuilder imageVersionInsert = new StringBuilder("");
        				imageVersionInsert.append(MySQLUpdate.ADD_VERSION_IMAGE_1);
        				for(int i=0;i<imgIds.size();i++) {
        					imageVersionInsert.append(MySQLUpdate.ADD_VERSION_IMAGE_2);
            				if(i != imgIds.size()-1) {
            					imageVersionInsert.append(", ");
            				}
        				}
        				//tie the model images to the version of the model
            			//dParQ.setQuerySQL(imageVersionInsert.toString());
            			//dParQ.setPrepStat(conn);
            			prepStat = conn.prepareStatement(imageVersionInsert.toString());//dParQ.getPrepStat(conn);
            			//set the sql params
            			int numParams = 3;
            			for(int i=0;i<imgIds.size();i++) {
            				prepStat.setInt((i*numParams)+1, modelOid);
            				prepStat.setInt((i*numParams)+2, versionNo);
            				prepStat.setLong((i*numParams)+3, imgIds.get(i));
            			}
            			prepStat.executeUpdate();
            			//TODO - common code - create new method?
            			if(imgs != null) {
            				//main directory for files associated witgh this model
            				String modelDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX+idString;
            				FileUtility.createDirectory(modelDir);
            				FileUtility.createDirectory(modelDir+"/images/");
            				FileUtility.createDirectory(modelDir+"/images/"+Constants.THUMBNAILS_URL_SUFFIX);
                			//add each new image to the file system
                			for(int i=0;i<imgs.size();i++) {
                				DataFile df = imgs.get(i);
                				FileUtility.uploadFile(modelDir+"/images/"+df.getDataFileFileName(), df.getDataFile());
                				BufferedImage img = ImageIO.read(new File(modelDir+"/images/"+df.getDataFileFileName()));
                				int [] dimensions = ImageUtility.getTargetDimensions(img, Constants.THUMBNAIL_DIMENSION);
                				img = ImageUtility.resize(img, dimensions[0], dimensions[1], true);
                				ImageUtility.writeImageToFile(img, "png", modelDir+"/images/"+Constants.THUMBNAILS_URL_SUFFIX, df.getDataFileFileName());
                			}
            			}
        			}
        			/*else {
        				conn.rollback();
        				conn.setAutoCommit(true);
        				//return "An unexpected error occurred. Please try again.";
        				throw new SQLException();
        			}*/
        			//conn.commit();
        			
        			//add the supplementary files for this version
        			//stmt.execute(MySQLQuery.XLOCK_SUPPL_FILE);
        			/*resSet = stmt.executeQuery(MySQLQuery.MAX_SUP_FILE_OID);
        			oid = 1;
        			if(resSet.first()) {
        				oid = resSet.getInt(1) +1;
        			}*/
        			
        			//list of all data file ids for this model version
        			ArrayList<Long> supFileIds = new ArrayList<>();
        			
        			List<DataFile> supFiles = submission.getSupplementaryFiles();
        			
        			//if the user has chosen to upload new data files
        			if(supFiles != null && supFiles.size() > 0){
        				StringBuilder supFileInsert = new StringBuilder("");
        				supFileInsert.append(MySQLUpdate.ADD_SUP_FILE_1);
        				for(int i=0;i<supFiles.size();i++) {
        					supFileInsert.append(MySQLUpdate.ADD_SUP_FILE_2);
        					if(i != supFiles.size()-1) {
        						supFileInsert.append(", ");
        					}
        				}
        				
        				//dParQ.setQuerySQL(supFileInsert.toString());
        				//dParQ.setPrepStat(conn);
        				prepStat = conn.prepareStatement(supFileInsert.toString());// dParQ.getPrepStat(conn);
        				
        				int numParams = 5;
        				for(int i=0;i<supFiles.size();i++) {
        					
                                                long supId = IdGenerator.getNextFileId();
        					//add the id of this image to the list of all image ids for this version
                                                supFileIds.add(supId);
        					
        					DataFile df = supFiles.get(i);
        					
        					//set the name of the file
        					df.setDataFileFileName(String.valueOf(supId)+df.getDataFileFileName());
        					supFiles.set(i, df);
        					
        					//set sql params
        					prepStat.setLong((i*numParams)+1, supId);
        					prepStat.setString((i*numParams)+2, df.getDescription());
        					prepStat.setString((i*numParams)+3, Constants.DB_ID_PREFIX+idString+"/data/");
        					prepStat.setString((i*numParams)+4, df.getDataFileFileName());
        					prepStat.setInt((i*numParams)+5, modelOid);
        				}
        				prepStat.executeUpdate();
        			}
        			
        			/*execute query to associate new and preselected data files with this version*/ 
        			
        			//first get ALL the data file ids for this model
        			parQ = MySQLQuery.getParamQuery("MODEL_SUPP_FILES");
        			
        			prepStat = parQ.getPrepStat(conn);
        			prepStat.setString(1, submission.getAccession());
        			
        			resSet = prepStat.executeQuery();
        			
        			ArrayList<Long> allSupFileIds = new ArrayList<>();
        			while(resSet.next()) {
        				allSupFileIds.add(resSet.getLong(1));
        			}
        			
        			//get the list of user-selected files already associated with the model
        			String [] selectedSupFileIds = submission.getSupFileIds();
        			//if the user has selected data files to accocitae with this version
        			if(selectedSupFileIds != null && selectedSupFileIds.length > 0) {
        				//for each selected file id, check that it exists  
        				//in the db and it is associated with this particular model
        				for(int i=0;i<selectedSupFileIds.length;i++){
        					//convert the string to an integer
        					long id = -1;
        					try {
        						id = Long.parseLong(selectedSupFileIds[i]);
        					}
        					catch (NumberFormatException e){
        						conn.rollback();
        						conn.setAutoCommit(true);
        						//return "An unexpected error occurred. Please try again";
        						throw new SQLException("Sup file id was not long");
        					}
        					//if the id is one of the list of all ids and 
        					//it is not already in the list of image ids to be added, add it to the list
        					if(allSupFileIds.contains(id) && !supFileIds.contains(id)) {
        						supFileIds.add(id);
        					}
        					
        				}
        			}
        			
        			//if there are ids in the list, add them to the db
        			if(supFileIds.size() > 0) {
        				StringBuilder supFileVersionInsert = new StringBuilder("");
        				supFileVersionInsert.append(MySQLUpdate.ADD_VERSION_SUP_FILE_1);
        				for(int i=0;i<supFileIds.size();i++) {
        					supFileVersionInsert.append(MySQLUpdate.ADD_VERSION_SUP_FILE_2);
            				if(i != supFileIds.size()-1) {
            					supFileVersionInsert.append(", ");
            				}
        				}
        				//tie the model images to the version of the model
            			//dParQ.setQuerySQL(supFileVersionInsert.toString());
            			//dParQ.setPrepStat(conn);
            			prepStat = conn.prepareStatement(supFileVersionInsert.toString());//dParQ.getPrepStat(conn);
            			//set the sql params
            			int numParams = 3;
            			for(int i=0;i<supFileIds.size();i++) {
            				prepStat.setInt((i*numParams)+1, modelOid);
            				prepStat.setInt((i*numParams)+2, versionNo);
            				prepStat.setLong((i*numParams)+3, supFileIds.get(i));
            			}
            			prepStat.executeUpdate();
            			
            			if(supFiles != null) {
            				
            				//main directory for files associated witgh this model
            				String modelDir = Constants.UPLOAD_DATA_DIR+Constants.DB_ID_PREFIX+idString;
            				FileUtility.createDirectory(modelDir);
            				
            				//directory used to store the data files
                			String dataDir = modelDir+"/data";
                			FileUtility.createDirectory(dataDir);
            				for(int i=0;i<supFiles.size();i++) {
            					DataFile df = supFiles.get(i);
            					FileUtility.uploadFile(dataDir+"/"+df.getDataFileFileName(), df.getDataFile());
            				}
            			}
        			}
    			}
    			else {
    				//the format couldn't be found return error
    				conn.rollback();
    				conn.setAutoCommit(true);
    				//return "An unexpected error occurred. Please try again";
    				throw new SQLException();
    			}
        	//}
        	//else {
        		//couldn't find any versions of this model so return error message
        		//conn.rollback();
				//conn.setAutoCommit(true);
        		//return "An unexpected error occurred. Please try again";
				//throw new SQLException();
        	//}
        	//commit the transaction
			conn.commit();
                        //stmt.execute(MySQLQuery.XUNLOCK);
			conn.setAutoCommit(true);
			return null;
        	
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			return "An unexpected error occurred. Please try again";
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		} finally {
			closeResultSet(resSet);
			closeStatement(stmt);
			closePreparedStatement(prepStat);
			closeConnection(conn);
		}
	}

	
        protected void addAttributesNames(Collection<String> names,Connection conn) throws SQLException {
            
            if (names == null || names.isEmpty()) return;
            
            
            PreparedStatement prepStat = null;
            Statement stmt = null;
            ResultSet resSet = null;

            try {
            
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                stmt.execute(MySQLQuery.XLOCK_ATTRIBUTE_NAME);

                for (String attName : names) {
                        if (attName == null || attName.isEmpty()) continue;
                        //check the attribtue name is in the db
                        ParamQuery parQ = MySQLQuery.getParamQuery("ATTRIBUTE_NAME");
                        prepStat = parQ.getPrepStat(conn);
                        prepStat.setString(1, attName);
                        resSet = prepStat.executeQuery();

                        //if the name isn't in the db, put it in
                        if(!resSet.first()) {
                                parQ = MySQLUpdate.getParamQuery("ADD_ATTRIBUTE_NAME");
                                
                                prepStat = parQ.getPrepStat(conn);
                                prepStat.setString(1, attName);
                                prepStat.executeUpdate();
                        }

                }
		conn.commit();
                stmt.execute(MySQLQuery.XUNLOCK);
                conn.setAutoCommit(true);
            } finally {
                closeResultSet(resSet);
                closeStatement(stmt);
                closePreparedStatement(prepStat);
            }
            
        }

    @Override
    public void updatePublication(Publication publication, String accession) {
        Connection conn = getConnection();
        
        try {

                conn.setAutoCommit(false);
                
                long modelId = getModelId(accession,conn);
                
                ParamQuery parQ = MySQLUpdate.getParamQuery(MySQLUpdate.UPDATE_MODEL_PUBLICATION);
                

                PreparedStatement prepStat = parQ.getPrepStat(conn); 
                try {

                    setPublication(publication, prepStat, 0);
                    prepStat.setLong(13, modelId);
                    prepStat.setLong(14, publication.getPubOID());
                    
                    int updated = prepStat.executeUpdate();
                    if (updated != 1) {
                        conn.rollback();
                        throw new SQLException("Could not update publication, wrong result count="+updated);
                    }
                    
                    conn.commit();
                    conn.setAutoCommit(true);
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
    public void removePublication(Publication publication, String accession) {
        Connection conn = getConnection();
        
        try {

                conn.setAutoCommit(false);
                
                long modelId = getModelId(accession,conn);
                
                ParamQuery parQ = MySQLUpdate.getParamQuery(MySQLUpdate.DELETE_MODEL_PUBLICATION);
                

                PreparedStatement prepStat = parQ.getPrepStat(conn); 
                try {

                    prepStat.setLong(1, modelId);
                    prepStat.setLong(2, publication.getPubOID());
                    
                    int updated = prepStat.executeUpdate();
                    if (updated != 1) {
                        conn.rollback();
                        throw new SQLException("Could not remove publication, wrong result count="+updated);
                    }
                    
                    conn.commit();
                    conn.setAutoCommit(true);
                } finally {
                    closePreparedStatement(prepStat);
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Problem removing publications: "+e.getMessage(),e);
        }
        finally {
            closeConnection(conn);
        }
    }
    

    
    @Override
    public void insertPublication(Publication publication, String accession) {
        Connection conn = getConnection();
        
        try {

                conn.setAutoCommit(false);
                
                long modelId = getModelId(accession,conn);
                
                ParamQuery parQ = MySQLUpdate.getParamQuery(MySQLUpdate.INSERT_MODEL_PUBLICATION);
                

                PreparedStatement prepStat = parQ.getPrepStat(conn); 
                try {

                    setPublication(publication, prepStat, 0);
                    prepStat.setLong(13, modelId);
                    
                    int updated = prepStat.executeUpdate();
                    if (updated != 1) {
                        conn.rollback();
                        throw new SQLException("Could not insert publication, wrong result count="+updated);
                    }
                    
                    conn.commit();
                    conn.setAutoCommit(true);
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
    
    protected void setPublication(Publication pub,PreparedStatement stat,int ix) throws SQLException {
        
        stat.setString(ix+1, pub.getAuthors());
        stat.setString(ix+2, pub.getPeriodicalName());
        stat.setString(ix+3, pub.getYear());
        stat.setString(ix+4, pub.getTitle());
        stat.setString(ix+5, pub.getAbstract());
        stat.setString(ix+6, pub.getSecondaryAuthors());
        stat.setString(ix+7, pub.getSecondaryTitle());
        stat.setString(ix+8, pub.getPublisher());
        stat.setString(ix+9, pub.getPages());
        stat.setString(ix+10, pub.getIsbn());
        stat.setString(ix+11, pub.getUrl());
        stat.setString(ix+12, pub.getReferenceType());
        
    }
    

    private long getModelId(String accession, Connection conn) throws SQLException {
        
                ParamQuery parQ = MySQLUpdate.getParamQuery(MySQLUpdate.GET_MODEL_ID);
                

                PreparedStatement prepStat = parQ.getPrepStat(conn); 
                try {

                    prepStat.setString(1, accession);
                    ResultSet resSet = prepStat.executeQuery();
                    try {
                        if(!resSet.first()) throw new SQLException("Missing model: +accession");
                        return resSet.getLong(1);
                        
                    } finally {
                        closeResultSet(resSet);
                    }                                
                } finally {
                    closePreparedStatement(prepStat);
                }
    }

	

	
}
