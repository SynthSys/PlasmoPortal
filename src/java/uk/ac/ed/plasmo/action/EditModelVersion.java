package uk.ac.ed.plasmo.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import java.io.File;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.assembler.ModelFormatAssembler;
import uk.ac.ed.plasmo.db.DataConstants;
import uk.ac.ed.plasmo.entity.DataFile;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.entity.VersionSubmission;
import uk.ac.ed.plasmo.utility.ImageUtility;
import uk.ac.ed.plasmo.xml.XMLValidator;
/**
 * <p>Action class that interacts with the data access layer in order to submit
 * modified meta data about a specific model</p>
 * <p>The user must be logged in and be the owner of the model to proceed.</p> 
 * @author ctindal
 *
 */
public class EditModelVersion extends EditModelVersionDisplay {
	
	private static final long serialVersionUID = 1L;
	
	//protected static final String NO_CHANGE = "no_change";
	
        @Override
	public String execute() {
		String result = this.checkAuthorisation();
		if(!result.equals(SUCCESS)) {
			return result;
		}
		
		
		
		VersionSubmission submission = this.getSubmission();
		
		if(submission != null) {
			if(submission.getModel() != null) {
                                
                            ModelFormatAssembler assembler = new ModelFormatAssembler();
                            String schemaLocation = assembler.getSchemaLocation(submission.getFormat());
                            File schema = new File(schemaLocation);
                            XMLValidator validator = new XMLValidator();
                            try {
                                validator.validateXMLfile(submission.getModel(), schema);
                            } catch (XMLValidator.NotValidSchema | XMLValidator.NotValidXML e) {
                                addFieldError("validXML", "Wrong format: "+e.getMessage());
                            }
                            /*
				XMLProcessor processor = new XMLProcessor();

				processor.processXMLFile(submission.getModel().getAbsolutePath());

				//make sure the file uploaded contains well-formed xml
				ArrayList<String> errorList = (ArrayList<String>) processor.getErrorList();
				//if the xml is well-formed
				if(errorList.isEmpty()) {
					String nsURI = processor.getNsURI();

					//get the location on the file system of the schema file for the model
					ModelFormatAssembler assembler = new ModelFormatAssembler();
					String schemaLocation = assembler.getSchemaLocation(submission.getFormat());

					//if schema location is null, error
					if(schemaLocation == null) {
						addFieldError("validXML", "The model format \""+submission.getFormat()+"\" does not exist. Please try again.");
					}
					//if a schema exists, validate the model against the schema - display errors to user
					else if(!schemaLocation.equals("")) {
						XMLValidator modelValidator = new XMLValidator();
						errorList = (ArrayList<String>) modelValidator.validateXMLfile(submission.getModel().getAbsolutePath(), schemaLocation, nsURI);

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
			
			//validate image file submissions
			List<DataFile> images = submission.getImages();
			if(images != null && images.size() > 0){
				for(int i=0;i<images.size();i++) {
					DataFile dFile = images.get(i);
					if(dFile == null) {
						images.remove(i);
						i--;
					}
					else {
						if(dFile.getDataFile() == null && dFile.getDescription() != null && !dFile.getDescription().trim().equals("")) {
							addFieldError("submission.images["+i+"].dataFile", "Please provide an image file to accompany the corresponding description you have entered.");
							if(dFile.getDescription().length() > DataConstants.FILE_SHORT_DESCRIPTION) {
								addFieldError("submission.images["+i+"].description", "File description exceeds the maximum character length of " +DataConstants.FILE_SHORT_DESCRIPTION);
							}
						}
						if((dFile.getDescription() == null || dFile.getDescription().equals("")) && dFile.getDataFile()!=null) {
							addFieldError("submission.images["+i+"].description", "Please enter a description of the image you have tried to upload.");
						}
						if((dFile.getDescription() == null || dFile.getDescription().trim().equals("")) && dFile.getDataFile() == null) {
							images.remove(i);
							i--;
						}
						if(dFile.getDataFile() != null) {
							if(ImageUtility.convertFileToImage(dFile.getDataFile()) == null) {
								addFieldError("submission.images["+i+"].dataFile","Unable to process image file.");
							}
						}
					}
				}
			}
			submission.setImages(images);
			
			//validate supplementary file submissions
			List<DataFile> supFiles = submission.getSupplementaryFiles();
			if(supFiles != null && supFiles.size() > 0){
				for(int i=0;i<supFiles.size();i++) {
					DataFile dFile = supFiles.get(i);
					if(dFile == null) {
						supFiles.remove(i);
						i--;
					}
					else {
						if(dFile.getDataFile() == null && dFile.getDescription() != null && !dFile.getDescription().trim().equals("")) {
							addFieldError("submission.supplementaryFiles["+i+"].dataFile", "Please provide a file to accompany the corresponding description you have entered.");
							if(dFile.getDescription().length() > DataConstants.FILE_SHORT_DESCRIPTION) {
								addFieldError("submission.supplementaryFiles["+i+"].description", "File description exceeds the maximum character length of " +DataConstants.FILE_SHORT_DESCRIPTION);
							}
						}
						if((dFile.getDescription() == null || dFile.getDescription().equals("")) && dFile.getDataFile()!=null) {
							addFieldError("submission.supplementaryFiles["+i+"].description", "Please enter a description of the file you have tried to upload.");
						}
						if((dFile.getDescription() == null || dFile.getDescription().trim().equals("")) && dFile.getDataFile() == null) {
							supFiles.remove(i);
							i--;
						}
					}
				}
			}
			submission.setSupplementaryFiles(supFiles);
			
			
			/*if(submission.getComments() != null && submission.getComments().length() > DataConstants.VERSION_COMMENT) {
				addFieldError("submission.comments", "Comments exceeds the maximum character length of " + DataConstants.VERSION_COMMENT);
			}*/
			
			/*if(submission.getModel() == null 
					&& (submission.getImages() == null || submission.getImages().size() < 1) 
					&& (submission.getSupplementaryFiles() == null || submission.getSupplementaryFiles().size() < 1)
					&& (submission.getComments() == null || submission.getComments().trim().equals(""))) {
				return NO_CHANGE;
			}*/
		}
		else {
			addActionError("An unexpected error occurred. Please try again.");
		}
		
		if(getFieldErrors().size() > 0 || getActionErrors().size() > 0) {
			setVersionDisplayData();
			submission.setName(getSubmission().getName());
			this.setSubmission(submission);
			return INPUT;
		}
		else {
			
			Map<String, Object> session = ActionContext.getContext().getSession();
			user = (User) session.get("user");
			
			submission.setAccession(getAccession());
			submission.setVersion(getVersion());
			ModelAssembler assembler = new ModelAssembler();
			result = assembler.editModelVersion(submission, user);
			
			if(result == null) {
                            setMsg("version "+getVersion()+" successfully modified");
				return SUCCESS;
			}
			else {
				addActionError(result);
				return INPUT;
			}
		}
		
	}

}
