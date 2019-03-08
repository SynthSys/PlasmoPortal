package uk.ac.ed.plasmo.assembler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.ModifyItemDAO;
import uk.ac.ed.plasmo.db.MySQLQueryItemDAOImpl;
import uk.ac.ed.plasmo.db.NewItemDAO;
import uk.ac.ed.plasmo.db.QueryItemDAO;
import uk.ac.ed.plasmo.db.RemoveItemDAO;
import uk.ac.ed.plasmo.entity.GroupAttributes;
import uk.ac.ed.plasmo.entity.ModelData;
import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.entity.VersionSubmission;
import uk.ac.ed.plasmo.entity.WSModelSubmission;
import uk.ac.ed.plasmo.utility.Constants;
import uk.ac.ed.plasmo.xml.XSLTTransformer;

/**
 * The {@code ModelAssembler} class is used to initialise data access object 
 * and call methods used to retrieve and submit model data stored in the database.
 * @author ctindal
 *
 */
public class ModelAssembler {
	
	/**
	 * initialise the DAO to add a model to the database temporarily
	 * @param model
	 * @param format
	 * @return
	 */
	public String addInterimModel(File model, String format) {
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		return newItemDAO.addInterimModel(model, format);
	}
	
	
	/**
	 * add a new model (submitted via web service interface) to the database
	 * @param submission
	 * @param user
	 * @return
	 */
	public String addNewModel(WSModelSubmission submission, User user) {
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		return newItemDAO.addNewModel(submission, user);
	}
	
	/**
	 * initialize the database update object used to add model meta data to the database to accompany
	 * a specific model already stored in an interim table in the database
	 * @param submission the model submission
	 * @param user the submitter of the model
	 * @return the accession id of the newly submitted model
	 */
	public String completeModelSubmission(ModelSubmission submission, User user) {
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		String accession = newItemDAO.completeModelSubmission(submission, user);
		return accession;
	}
	
	/**
	 * initialise the DAO to retrieve a model as an {@code InputStream}
	 * @param accession
	 * @param version
	 * @return
	 */
	public InputStream getModelAsInputStream(String accession, String version,DOWNLOAD_TYPE type) {
	
            try {
		//QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		//return queryItemDAO.getModelAsInputStream(accession, version);
                QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
                int versionId = queryItemDAO.getVersionId(accession,version);
                //System.out.println("V: "+versionId);
                long formatId = queryItemDAO.getFormatId(accession, versionId);
                //System.out.println("F: "+formatId);
                String modelTXT = queryItemDAO.getModelAsString(accession, versionId);
                //System.out.println("M: "+modelTXT.substring(0,50));
                if (type == null) type = DOWNLOAD_TYPE.ANNOTATED;
                
                String model;
                switch (type) {
                    case ANNOTATED: model = annotateModel(modelTXT,formatId,accession,versionId); break;
                    case SIMPLIFIED: model = simplifyModel(modelTXT,formatId); break;
                    case ORIGINAL: model = modelTXT;break;
                    default:throw new IllegalArgumentException("Unknown download option: "+type);
                }
                
                return new ByteArrayInputStream(model.getBytes(StandardCharsets.UTF_8));
            } catch (MySQLQueryItemDAOImpl.MissingModel e) {
                e.printStackTrace();
                return null;
            }
	}
	
	/**
	 * retrieve model xml as a string 
	 * @param accession
	 * @param version
	 * @return the model as a string literal
	 */
	public String getModelAsString(String accession, int versionId) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelAsString(accession, versionId);
	}
        
        /**
	 * retrieve model xml as a string 
	 * @param accession
	 * @param version if null most recent version will be obtained
	 * @return the model as a string literal
         */
	public String getModelAsString(String accession, String version) {
            QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
            int versionId = queryItemDAO.getVersionId(accession,version);
            return queryItemDAO.getModelAsString(accession, versionId);
	}
        
	
	/**
	 * initialise the DAO to retrieve all meta data about a model
	 * @param accession the accession no. of the model
	 * @param version the version no. of the model
	 * @return
	 */
	public ModelData getModelData(String accession, String version, User user) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		
		ModelData data = queryItemDAO.getModelData(accession, version, user);
		if(data != null) {
			data.setComments(queryItemDAO.getModelComments(accession));
		}
		
		return data;
	}
	
	/**
	 * initiales the DAO to remove a temporarily stored model from the database
	 * @param tempId the temporary id of the model
	 */
	public void deleteInterimModel(String tempId) {
		RemoveItemDAO removeItemDAO = MySQLDAOFactory.getRemoveItemDAO();
		removeItemDAO.deleteInterimModel(tempId);
	}
	
	/*public String getModelNameFromAccession(String accession) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelNameFromAccession(accession);
	}*/
	
	
	public ModelData getModelDataForNewModelVersionDisplay(String accession) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelDataForNewVersionDisplay(accession);
	}
	
	/**
	 * inititialise the DAO used to add a new version of a model to the database
	 * @param submission object containing all new data to be submitted to the database
	 * @param user the user submitting the data
	 * @return
	 */
	public String addNewModelVersion(VersionSubmission submission, User user) {
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		return newItemDAO.addNewModelVersion(submission, user);
	}
	
	/**
	 * initialise the DAO used to flag the model as deleted in the database
	 * @param accession
	 * @param delete
	 */
	public void setModelIsDeleted(String accession, boolean delete) {
		ModifyItemDAO modifyItemDAO = MySQLDAOFactory.getModifyItemDAO();
		modifyItemDAO.setModelIsDeleted(accession, delete);
	}
	
	/**
	 * initialise the DAO used to delete a model from the database 
	 * @param accession
	 */
	public void deleteModel(String accession) {
		RemoveItemDAO removeItemDAO = MySQLDAOFactory.getRemoveItemDAO();
		removeItemDAO.deleteModel(accession);
	}
	
	/**
	 * initialise the DAO used to get model version data for display to the suer
	 * @param accession the accession no. of the model
	 * @param version the version no. of the model
	 * @return
	 */
	public VersionSubmission getModelDataForEditModelVersionDisplay(String accession, String version) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelDataForEditVersionDisplay(accession, version);
	}
	
	/**
	 * initialise the DAO used to submit changes/updates to a specific version of a
	 * specific model
	 * @param submission
	 * @param user
	 * @return
	 */
	public String editModelVersion(VersionSubmission submission, User user) {
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		return newItemDAO.editModelVersion(submission, user);
	}
	
        
        public ModelSubmission getModelDataForEditPublicationsDisplay(String accession) {
            QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
            ModelSubmission model = getModelDataForEditModelDisplay(accession,queryItemDAO);
            setPublications(model,queryItemDAO);
            return model;
        }
        
	/**
	 * initialise the database query object to get meta data for a specific model to
	 * be displayed when editing the model
	 * @param accession the accession id of the model
	 * @return
	 */
	public ModelSubmission getModelDataForEditModelDisplay(String accession) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelDataForEditModelDisplay(accession);
	}
        
	protected ModelSubmission getModelDataForEditModelDisplay(String accession,QueryItemDAO queryItemDAO) {
		return queryItemDAO.getModelDataForEditModelDisplay(accession);
	}
        
	
	/**
	 * initialise the DAO used to submit changes/updates to model meta data
	 * @param submission
	 * @return
	 */
	public String editModel(ModelSubmission submission) {
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		return newItemDAO.editModel(submission);
	}
	
	/**
	 * initialise the DAO used to submit user comments about a model to the database
	 * @param accession
	 * @param comment
	 * @param user
	 */
	public void addModelComment(String accession, String comment, User user) {
		NewItemDAO newItemDAO = MySQLDAOFactory.getNewItemDAO();
		newItemDAO.addModelComment(accession, comment, user);
	}
	
	/**
	 * initialise the DAO used to retrieve a list of all user comments about
	 * a specific model
	 * @param accession the accession no. of the model
	 * @return
	 */
	public ArrayList<String []> getModelComments(String accession) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelComments(accession);
	}
	
	/**
	 * initialise the DAO used to get the ids of all public models in the
	 * database
	 * @return
	 */
	public ArrayList<String> getAllModelIds() {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getAllModelIds();
	}
	
	/**
	 * get the name of a model using the model's accession no.
	 * @param accession the id of the model
	 * @return the name of the model
	 */
	public String getModelName(String accession) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelName(accession);
	}
	
	/**
	 * initialise the database query object used to get the ids of all models with a specific name
	 * @param name the specified model name
	 * @return list of model ids whose name matches the one specified
	 */
	public ArrayList<String> getModelsIdsByName(String name) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelIdsByName(name);
	}
	
	/**
	 * initialise the database query object used to find ids of models with a specific format
	 * @param format the format of the model
	 * @return a list of model ids whose format is the same as the one specified
	 */
	public ArrayList<String> getModelIdsByFormat(String format) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getModelIdsByFormat(format);
	}
	
	/**
	 * initialise the database access object used to update the owner of the selected model
	 * @param supervisor
	 * @param newOwner
	 * @param accession
	 * @return
	 */
	public String updateModelOwnership(User supervisor, User newOwner, String accession) {
		ModifyItemDAO modifyItemDAO = MySQLDAOFactory.getModifyItemDAO();
		return modifyItemDAO.updateModelOwnership(supervisor, newOwner, accession);
	}
	
	/*public VersionSubmission getDataForNewModelVersionDisplay(String accession) {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		return queryItemDAO.getDataForNewModelVersionDisplay(accession);
	}*/
	
	public void updateGroupPermissions(String accession,GroupAttributes grpAttribute){
		ModifyItemDAO modifyItemDAO = MySQLDAOFactory.getModifyItemDAO();
		modifyItemDAO.updateGroupPermissions(accession, grpAttribute);
	}

    public int hideModel(String accession) {
		RemoveItemDAO removeItemDAO = MySQLDAOFactory.getRemoveItemDAO();
		return removeItemDAO.hideModel(accession);
    }


    /**
     * Inserts Plasmo ids into model annotations
     * @param modelTXT model to be customized
     * @param formatId model format (id as in the db)
     * @param accession model id (used to insert the value into the model description)
     * @param version model version (used to insert the value into the model description)
     * @return 
     */
    protected String annotateModel(String modelTXT, long formatId,String accession,int version) {

        String templatePath = getAnnotationTemplatePath(formatId);
        //System.out.println("Template path: "+templatePath);
        if (templatePath == null) return modelTXT;
        File template = new File(templatePath);
        if (!template.exists() || !template.canRead()) return modelTXT;
        try {
            Map<String,String> params = new HashMap<>();
            params.put(Constants.XSLT_ACC_PARAM,accession);
            params.put(Constants.XSLT_VER_PARAM,""+version);
            return XSLTTransformer.transform(modelTXT, template,params);
        } catch (XSLTTransformer.XMLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Could not annotate model: "+ex.getMessage());
        }
    }
    
    /**
     * Removes annotations from sbml files
     * @param modelTXT model to be customized
     * @param formatId model format (id as in the db)
     * @return 
     */
    protected String simplifyModel(String modelTXT, long formatId) {

        String templatePath = getSimplificationTemplatePath(formatId);
        //System.out.println("Template path: "+templatePath);
        if (templatePath == null) return modelTXT;
        File template = new File(templatePath);
        if (!template.exists() || !template.canRead()) return modelTXT;
        try {
            Map<String,String> params = new HashMap<>();
            return XSLTTransformer.transform(modelTXT, template,params);
        } catch (XSLTTransformer.XMLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Could not simplified model: "+ex.getMessage());
        }
    }
    

    protected String getAnnotationTemplatePath(long formatId) {
        return Constants.UPLOAD_XSLT_DIR+formatId+File.separator+Constants.ANNOTATIONS_TEMPLATE;//"annotation.xslt";
    }
    
    protected String getSimplificationTemplatePath(long formatId) {
        return Constants.UPLOAD_XSLT_DIR+formatId+File.separator+Constants.SIMPLIFICATION_TEMPLATE;
    }
    

    protected void setPublications(ModelSubmission model,QueryItemDAO queryItemDAO) {
        List<Publication> publications = queryItemDAO.getModelPublications(model.getAccession());
        model.setPublications(publications);
    }


}
