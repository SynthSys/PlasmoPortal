package uk.ac.ed.plasmo.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.assembler.ModelFormatAssembler;
import uk.ac.ed.plasmo.entity.DataFile;
import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.entity.User;
//import uk.ac.ed.plasmo.xml.XMLProcessor;
import uk.ac.ed.plasmo.xml.XMLValidator;

import com.opensymphony.xwork2.ActionContext;
/**
 * <p>Action class that interacts with the data access layer in order to
 * add a new model to the database</p>
 * <p>The model is submitted via this class as a file which is then passed
 * to the DAO for upload to a temporary location in the database.</p>
 * <p>A user must be logged on in order to submit new models/meta data to the portal.</p>
 * @author ctindal
 *
 */
public class UploadModel extends CreateNewModelOperations {
	
	private static final long serialVersionUID = 1L;
	
	private File model;
	private String modelFileName, modelContentType;
	
	private String format;
	private ModelSubmission submission;
	
	public File getModel() {
		return model;
	}
	public void setModel(File model) {
		this.model = model;
	}
	public String getModelFileName() {
		return modelFileName;
	}
	public void setModelFileName(String modelFileName) {
		this.modelFileName = modelFileName;
	}
	public String getModelContentType() {
		return modelContentType;
	}
	public void setModelContentType(String modelContentType) {
		this.modelContentType = modelContentType;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public ModelSubmission getSubmission() {
		return submission;
	}
	public void setSubmission(ModelSubmission submission) {
		this.submission = submission;
	}
	
        @Override
	public String execute() {
		
                //System.out.println("Starting upload");
                //if (true) throw new IllegalArgumentException("Are we here at all???");
		ModelAssembler assembler = new ModelAssembler();
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		
		User user = (User) session.get("user");
		
		//no model or user in session means error in prior submission stage 
		if(user == null){
			return INPUT;
		}
		
		String id = assembler.addInterimModel(model, format);
		if (id == null) {
                    addActionError("Could not save temporaly the model");
                    return INPUT;
                }
		//if no metadata, create new modelSubmission object
		//TODO parse the model file (SIMILE and SBML) to obtain meta data
		if(submission == null) {
			submission = new ModelSubmission();
		}
		
		submission.setTempId(id);
		
		if(submission.getPublications() == null){
			submission.setPublications(new ArrayList<Publication>());
		}
		
		DataFile df = new DataFile();
		ArrayList<DataFile> files = new ArrayList<DataFile>();
		files.add(df);
		submission.setImages(files);
		submission.setSupplementaryFiles(files);
		
		session.put("modelSubmission", submission);
		addActionMessage("The file "+ modelFileName.toUpperCase() +" has been sucessfully uploaded. Please complete the submission process:");
		
            //System.out.println("Executed upload");
		return SUCCESS;
	}
	
        @Override
	public void validate() {
		
            //System.out.println("Starting validation");
            
		//if the user was midway through submitting a model (which is stored in session) and decided 
		//to start again, and the submission has to be removed from session and the model removed from the db
		this.removeInterimModel();
		
                //System.out.println("Validating");
		//check that the user has uploaded a file
		if(model != null) {
                    
                            
                        ModelFormatAssembler assembler = new ModelFormatAssembler();
                        String schemaLocation = assembler.getSchemaLocation(format);
                        File schema = new File(schemaLocation);
                        XMLValidator validator = new XMLValidator();
                        try {
                            validator.validateXMLfile(model, schema);
                        } catch (XMLValidator.NotValidSchema | XMLValidator.NotValidXML e) {
                            addFieldError("validXML", "Wrong format: "+e.getMessage());
                        }
                    
                        /*
			XMLProcessor processor = new XMLProcessor();
			
			processor.processXMLFile(model.getAbsolutePath());
			
			//make sure the file uploaded contains well-formed xml
			List<String> errorList = processor.getErrorList();
                        //if (errorList.isEmpty()) System.out.println("Proper xml");
			//if the xml is well-formed
			if(errorList.isEmpty()) {
				String nsURI = processor.getNsURI();
				
				//get the location on the file system of the schema file for the model
				ModelFormatAssembler assembler = new ModelFormatAssembler();
				String schemaLocation = assembler.getSchemaLocation(format);
				
				//if schema location is null, error
				if(schemaLocation == null) {
					addFieldError("validXML", "The model format \""+format+"\" does not exist. Please try again.");
				}
				//if a schema exists, validate the model against the schema - display errors to user
				else if(!schemaLocation.equals("")) {
					XMLValidator modelValidator = new XMLValidator();
					errorList = modelValidator.validateXMLfile(model.getAbsolutePath(), schemaLocation, nsURI);
					
                                        //System.out.println("Gots "+errorList.size()+" errors after xml validator");
					for(int i=0;i<errorList.size();i++){
						addFieldError("validXML", errorList.get(i));
					}
				}
			}
			else {
				for(int i=0;i<errorList.size();i++){
					addFieldError("validXML", errorList.get(i));
				}
			}
                        */
		}
		//else error
		else {
			addActionError("Please upload a model before progressing");
		}
                //System.out.println("Finished validation");
	}
}
