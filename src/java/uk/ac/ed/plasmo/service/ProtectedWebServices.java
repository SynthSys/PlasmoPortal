package uk.ac.ed.plasmo.service;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import org.apache.axis2.context.MessageContext;
//import org.apache.rampart.RampartMessageData;
import org.xml.sax.InputSource;

import uk.ac.ed.plasmo.action.TableView;
import uk.ac.ed.plasmo.assembler.BrowseModelsAssembler;
import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.assembler.ModelFormatAssembler;
import uk.ac.ed.plasmo.assembler.SearchModelsAssembler;
import uk.ac.ed.plasmo.assembler.SecurityAssembler;
import uk.ac.ed.plasmo.entity.ModelData;
import uk.ac.ed.plasmo.entity.ModelSummary;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.entity.VersionSubmission;
import uk.ac.ed.plasmo.entity.VsnSubmission;
import uk.ac.ed.plasmo.entity.WSModelSubmission;
import uk.ac.ed.plasmo.exception.InvalidModelException;
import uk.ac.ed.plasmo.exception.InvalidSubmissionException;
import uk.ac.ed.plasmo.exception.InvalidUserException;
import uk.ac.ed.plasmo.utility.table.Table;
import uk.ac.ed.plasmo.utility.table.TableAssembler;
//import uk.ac.ed.plasmo.xml.XMLProcessor;
import uk.ac.ed.plasmo.xml.XMLValidator;

/**
 * class containing a set of methods used by axis2 web service engine to submit models
 * and related meta data to the database.
 * @author ctindal
 *
 */
public class ProtectedWebServices {
	
	/**
	 * get the submitter's username
	 * @return
	 */
	private String getUserNameFromRampart() {
		
                throw new UnsupportedOperationException("Commented off during libraries update, and axis removal"); 
		/*MessageContext msgContext = MessageContext.getCurrentMessageContext();
		
		String userName = (String) msgContext.getProperty(RampartMessageData.USERNAME);
		
		return userName;
                */ 
	}
	
	/**
	 * checks if a submitted model is valid
	 * @param model
	 * @param modelFormat
	 */
	private void validateModel(String model, String modelFormat) {
            
            
		//check that the user has uploaded a valid model
		if(model != null && !model.trim().equals("")) {
                    
                            
                        ModelFormatAssembler assembler = new ModelFormatAssembler();
                        String schemaLocation = assembler.getSchemaLocation(modelFormat);
                        File schema = new File(schemaLocation);
                        XMLValidator validator = new XMLValidator();
                        try {
                            validator.validateXML(model, schema);
                        } catch (XMLValidator.NotValidSchema | XMLValidator.NotValidXML e) {
                            throw new InvalidSubmissionException("The model you have uploaded is not a valid xml file ("+e.getMessage()+") Please try again.");
                        }

                        /*
			XMLProcessor processor = new XMLProcessor();
			
			InputSource source = new InputSource(new StringReader(model));
			processor.processXMLInputSource(source);
			
			//make sure the file uploaded contains well-formed xml
			ArrayList<String> errorList = (ArrayList<String>) processor.getErrorList();
			//if the xml is well-formed
			if(errorList.size() == 0) {
				String nsURI = processor.getNsURI();
				
				//get the location on the file system of the schema file for the model
				ModelFormatAssembler assembler = new ModelFormatAssembler();
				String schemaLocation = assembler.getSchemaLocation(modelFormat);
				
				//if schema location is null, error
				if(schemaLocation == null) {
					throw new InvalidSubmissionException("The model format \""+modelFormat+"\" does not exist in the portal. Please try again.");
				}
				//if a schema exists, validate the model against the schema - display errors to user
				else if(!schemaLocation.equals("")) {
					InputSource sourceII = new InputSource(new StringReader(model));
					XMLValidator modelValidator = new XMLValidator();
					errorList = (ArrayList<String>) modelValidator.validateXMLInputSource(sourceII, schemaLocation, nsURI);
					
					if(errorList.size() > 0) {
						throw new InvalidSubmissionException("The model you have uploaded is not a valid "+modelFormat+" file. Please try again.");
					}
				}
			}
			else {
				throw new InvalidSubmissionException("The model you have uploaded is not a valid xml file. Please try again.");
			} */
		}
		//else error
		else {
			throw new InvalidSubmissionException("Please provide a model to be entered in the database before progressing");
		}
                
	}
	
	/**
	 * retrieve any model (public or restricted access provided the user has access privileges)
	 * using the model's unique id
	 * @param accession the id of the model to be retrieved 
	 * @return
	 */
	public String getModelById(String accession) {
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		
		//get the name of the user adding the model
		User user = secAssembler.getUser(getUserNameFromRampart());
		
		
		Boolean authorised = secAssembler.isAuthorisedToAccessModel(user, accession);
		if(authorised == null) {
			throw new InvalidModelException("Model with accession "+accession+" does not exist in the database.");
		}
		if(!authorised) {
			throw new InvalidUserException("You are not authorised to access model with accession "+accession);
		}
		
		ModelAssembler assembler = new ModelAssembler();
		
                
		String model = assembler.getModelAsString(accession, null);
		
		return model;
	}
	
	/*public String [] getAccessibleModelsIds() {
		return null;
	}*/
	
	/**
	 * get a list of model summaries for all models in the database including private models 
	 * that the user has access privileges for and whose attributes also match the specified search term.
	 * The search is carried out against
	 * the name of the model, the model format, the user name of the model owner, the name of the 
	 * model owner and any of the model's descriptive attributes. If the search term used is '*' then the
	 * program will search for all public models stored in the database although for performance issues, it
	 * is recommended that {@link getSummaryOfAllAccessibleModels} is used in place of '*' when searching all
	 * models. The set of returned data is ordered by model name.
	 * @param offset offset the start point from which the first model is retrieved. E.g. if the 
	 * offset is 1 and numModels is 5, only the 1st 5 models in the database will be returned. 
	 * If the offset is 2 and numModels is 5, the 2nd group of 5 models will be returned (models 1-5 will be skipped).
	 * @param numModels the number of models to be retrieved. Returned 
	 * lists are limited to the following sizes: 5, 10, 20, 50, 100
	 * @param searchTerm the string used to search for specific models
	 * @return an array of {@link ModelSummary} objects
	 */
	public ModelSummary [] getSummaryOfAllAccessibleModelsSearch(int offset, int numModels, String searchTerm) {
		
		TableView tableView = new TableView();

		try {
			tableView.setRpp(numModels);
			tableView.setPgNo(offset);
		}
		catch(NumberFormatException e) {
			tableView.setRpp(TableView.DEFAULT_ROWS_PER_PAGE);
			tableView.setPgNo(1);
		}
				
		boolean validOption = false;
		int index = 0;
		while(index < TableView.DEFAULT_PER_PAGE_OPTIONS.length && !validOption) {
			if(tableView.getRpp() == TableView.DEFAULT_PER_PAGE_OPTIONS[index]) {
				validOption = true;
			}
			else {
				index++;
			}
		}

		//if the supplied value isn't valid, use the default
		if(!validOption) {
			tableView.setRpp(TableView.DEFAULT_ROWS_PER_PAGE);			
		}
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		//get the name of the user adding the model
		User user = secAssembler.getUser(getUserNameFromRampart());
		
		Map<String, Object> qParams = new HashMap<String, Object>();
		qParams.put("query", searchTerm);
		qParams.put("user", user);
		
		TableAssembler assembler = new SearchModelsAssembler(qParams);
		
		Table table = new Table(assembler, null);

		tableView.setTable(table);
				
		tableView.setValues();

		table = tableView.getTable();
				
		ArrayList<String []> data = table.getData();
		
		if(data == null || data.size() == 0) {
			return null;
		}
		
		ModelSummary [] modelSummaries = new ModelSummary [data.size()];
		
		for(int i=0;i<data.size();i++) {
			
			String [] dataRow = data.get(i);
			
			ModelSummary summary = new ModelSummary();
			summary.setId(dataRow[0]);
			summary.setName(dataRow[1]);
			summary.setFormat(dataRow[2]);
			summary.setLatestVersion(dataRow[3]);
			summary.setSubmitterName(dataRow[7]+" "+dataRow[8]);
			
			modelSummaries[i] = summary;
			
		}
		return modelSummaries;
	}
	
	/**
	 * get a list of model summaries for all accessible models in the database. 
	 * The set of returned data is ordered by model name.
	 * @param offset the start point from which the first model is retrieved. E.g. if the offset is 
	 * 1 and numModels is 5, only the 1st 5 models in the database will be returned. If the offset is 2 and numModels
	 * is 5, the 2nd group of 5 models will be returned (models 1-5 will be skipped).
	 * @param numModels the number of models to be retrieved. Returned lists are limited to the following sizes: 5, 10, 20, 50, 100
	 * @return a list of model summaries
	 */
	public ModelSummary [] getSummaryOfAllAccessibleModels(int offset, int numModels) {
		
		TableView tableView = new TableView();

		try {
			tableView.setRpp(numModels);
			tableView.setPgNo(offset);
		}
		catch(NumberFormatException e) {
			tableView.setRpp(TableView.DEFAULT_ROWS_PER_PAGE);
			tableView.setPgNo(1);
		}
				
		boolean validOption = false;
		int index = 0;
		while(index < TableView.DEFAULT_PER_PAGE_OPTIONS.length && !validOption) {
			if(tableView.getRpp() == TableView.DEFAULT_PER_PAGE_OPTIONS[index]) {
				validOption = true;
			}
			else {
				index++;
			}
		}

		//if the supplied value isn't valid, use the default
		if(!validOption) {
			tableView.setRpp(TableView.DEFAULT_ROWS_PER_PAGE);			
		}
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		//get the name of the user adding the model
		User user = secAssembler.getUser(getUserNameFromRampart());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		
		TableAssembler assembler = new BrowseModelsAssembler(params);
		
		Table table = new Table(assembler, null);

		tableView.setTable(table);
				
		tableView.setValues();

		table = tableView.getTable();
				
		ArrayList<String []> data = table.getData();
		
		ModelSummary [] modelSummaries = new ModelSummary [data.size()];
		
		for(int i=0;i<data.size();i++) {
			
			String [] dataRow = data.get(i);
			
			ModelSummary summary = new ModelSummary();
			summary.setId(dataRow[0]);
			summary.setName(dataRow[1]);
			summary.setFormat(dataRow[2]);
			summary.setLatestVersion(dataRow[3]);
			summary.setSubmitterName(dataRow[7]+" "+dataRow[8]);
			
			modelSummaries[i] = summary;
			
		}
		return modelSummaries;
	}
	
	/**
	 * get a summary of a model with a specific accession id provided the user is
	 * authorised to access the model
	 * @param id the id of the model 
	 * @return a summary of the model
	 */
	public ModelSummary getModelSummaryById(String accession) {
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		//get the name of the user adding the model
		User user = secAssembler.getUser(getUserNameFromRampart());
		
		Boolean authorised = secAssembler.isAuthorisedToAccessModel(user, accession);
		
		if(authorised == null) {
			throw new InvalidModelException("Model with accession "+accession+" does not exist in the database.");
		}
		if(!authorised) {
			throw new InvalidUserException("You are not authorised to access model with accession "+accession);
		}
		ModelAssembler assembler = new ModelAssembler();
		ModelData data = assembler.getModelData(accession, null,user);
		
		if(data != null) {
			ModelSummary summary = new ModelSummary();
			summary.setId(data.getAccession());
			summary.setName(data.getName());
			summary.setSubmitterName(data.getSubmitter().getGiven() +" "+ data.getSubmitter().getFamily());
			summary.setLatestVersion(String.valueOf(data.getMaxVersion()));
			summary.setFormat(data.getFormat());
			return summary;
		}
		else {
			return null;
		}
	}
	
	/**
	 * add a new model to the database
	 * @param submission submission object containing all details of the model to be 
	 * added to the database
	 * @return the accession id of the newly submitted model 
	 */
	public String addNewModel(WSModelSubmission submission) {
		
		if(submission == null) {
			throw new InvalidSubmissionException("Please provide a valid model submission.");
		}
		
		if(submission.getName() == null || submission.getName().trim().equals("")) {
			throw new InvalidSubmissionException("Please provide a valid name for the model");
		}
		
		this.validateModel(submission.getModelAsString(), submission.getFormat());
		
		//get the name of the user adding the model
		//String userName = this.getUserNameFromRampart();
		User user = new User();
		user.setUserName(getUserNameFromRampart());
		
		ModelAssembler assembler = new ModelAssembler();
		
		String accession = assembler.addNewModel(submission, user);
		
		if(accession == null) {
			throw new InvalidSubmissionException("An unexpected error has occurred. Please check your submission and try again.");
		}
		
		return accession;
	}
	
	/**
	 * add a new version of a specific model to the database
	 * @param submission object containing model version (as a string) and relevant meta data to
	 * be submitted to the database with this version of the model
	 * @return a message indicating the new model version was successfully uploaded.
	 */
	public String addNewModelVersion(VsnSubmission submission) {
		
		if(submission == null) {
			throw new InvalidSubmissionException("Please supply the data required to submit a new model version.");
		}
		
		if(submission.getAccession() == null || submission.getAccession().trim().equals("")) {
			throw new InvalidSubmissionException("Please specify the accession no. for the model.");
		}
		
		User user = new User();
		user.setUserName(getUserNameFromRampart());
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		Boolean authorised = secAssembler.isAuthorisedToEditModel(user, submission.getAccession());
		
		if(authorised == null) {
			throw new InvalidSubmissionException("Model with accession "+submission.getAccession()+" does not exist in the database.");
		}
		else {
			if(authorised) {
				this.validateModel(submission.getModelAsString(), submission.getFormat());
				ModelAssembler assembler = new ModelAssembler();
				
				VersionSubmission vSub =  new VersionSubmission();
				vSub.setAccession(submission.getAccession());
				vSub.setModelAsString(submission.getModelAsString());
				vSub.setFormat(submission.getFormat());
				vSub.setComments(submission.getComments());
				String result = assembler.addNewModelVersion(vSub, user);
				
				if(result == null) {
					return "A new version of "+vSub.getAccession()+" has been succesfully uploaded.";
				}
				else {
					throw new InvalidSubmissionException("An unexpected error occured");
				}
				
			}
			else {
				throw new InvalidUserException("User "+user.getUserName()+" is not authorised to edit the model with accession "+submission.getAccession());
			}
		}
	}
	
	/**
	 * overwrite a model version already in the database with another model
	 * submission object containing model version (as a string) and relevant meta data such as the
	 * unique id of the model and the specific version number to be replaced to be submitted to the 
	 * database
	 * @return a message indicating changes have been successfully implemented
	 */
	public String replaceModel(VsnSubmission submission) {
		
		if(submission == null) {
			throw new InvalidSubmissionException("Please supply the data required to submit a new model version.");
		}
		
		if(submission.getAccession() == null || submission.getAccession().trim().equals("")) {
			throw new InvalidSubmissionException("Please specify the accession no. for the model.");
		}
		
		User user = new User();
		user.setUserName(getUserNameFromRampart());
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		Boolean authorised = secAssembler.isAuthorisedToEditModel(user, submission.getAccession());
		
		if(authorised == null) {
			throw new InvalidSubmissionException("Model with accession "+submission.getAccession()+" does not exist in the database.");
		}
		else {
			if(authorised) {
				this.validateModel(submission.getModelAsString(), submission.getFormat());
				ModelAssembler assembler = new ModelAssembler();
				
				VersionSubmission vSub =  new VersionSubmission();
				vSub.setAccession(submission.getAccession());
				vSub.setModelAsString(submission.getModelAsString());
				vSub.setFormat(submission.getFormat());
				vSub.setComments(submission.getComments());
				vSub.setVersion(submission.getVersion());
				
				String result = assembler.editModelVersion(vSub, user);
				
				if(result == null) {
					return "Version "+vSub.getVersion()+" of "+vSub.getAccession()+" has been succesfully updated.";
				}
				else {
					throw new RuntimeException("An unexpected error occured");
				}
			}
			else {
				throw new InvalidUserException("User "+user.getUserName()+" is not authorised to edit the model with accession "+submission.getAccession());
			}
		}
	}
	

}
