package uk.ac.ed.plasmo.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.db.DataConstants;
import uk.ac.ed.plasmo.entity.Attribute;
import uk.ac.ed.plasmo.entity.DataFile;
import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.ImageUtility;
/**
 * <p>Action class that interacts with the data access layer in order to
 * complete the submission process of a new model by providing meta data
 * about a model to the DAO for submission to the database.</p>
 * <p>A user must be logged on in order to submit new models/meta data to the portal</p> 
 * @author ctindal
 *
 */
public class UploadModelMetadata extends CreateNewModelOperations {
	
	private static final long serialVersionUID = 1L;

	private ModelSubmission submission; //representation of all model data forming part of a submission
	
        String accession;
        
	private int tabIndex = 0;
	public ModelSubmission getSubmission() {
		return submission;
	}
	public void setSubmission(ModelSubmission submission) {
		this.submission = submission;
	}
	public int getTabIndex() {
		return tabIndex;
	}
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
	
	public String execute() {
		
		//get any submission data already stored in session and remove the model from session once retrieved
		Map<String, Object> session = ActionContext.getContext().getSession();
		ModelSubmission sessionModel = (ModelSubmission) session.get("modelSubmission");
		session.remove("modelSubmission");
		
		User user = (User) session.get("user");
		
		//no model or user in session means error in prior submission stage 
		if(sessionModel == null || user == null){
			return INPUT;
		}
		
		//set the user-submitted values
		submission.setTempId(sessionModel.getTempId());
		//submission.setPublications(publications);
		//modelSubmission.setImages(images);
		//modelSubmission.setSupplementaryFiles(supplementaryFiles);
		
		ModelAssembler assembler = new ModelAssembler();
		
		accession = assembler.completeModelSubmission(submission, user);
		
		if(accession == null) {
			addActionError("A problem occurred in the model submission process. Please try again.");
			return INPUT;
		}
		
		submission.setAccession(accession);
		
		addActionMessage("The model was succefully uploaded.");
		setMsg("The model was succefully uploaded.");
		return SUCCESS;
	}

    public String getAccession() {
        return accession;
    }
        
        
	
	public void validate() {
		if(submission != null) {
			
			if(!submission.isUnpublished()) {
				if(submission.getPublications() == null || submission.getPublications().size() == 0) {
					//publications = new ArrayList<Publication>();
					submission.setPublications(new ArrayList<Publication>());
					addFieldError("allPublications","Please provide details of at least one publication reference.");
				}
				else {
					this.validatePublications();
				}
			}
			if(submission.getSupplementaryFiles() != null && submission.getSupplementaryFiles().size() > 0){
				this.validateSupplementaryFiles();
			}
			else {
				submission.setSupplementaryFiles(new ArrayList<DataFile>());
			}
			if(submission.getAttributes() != null && submission.getAttributes().size() > 0) {
				this.validateAttributes();
			}
			if(submission.getImages() != null && submission.getImages().size() > 0){
				this.validateImages();
			}
			else {
				submission.setImages(new ArrayList<DataFile>());
				//addFieldError("images[0].dataFile","Please provide an appropriate image to upload.");
				//addFieldError("images[0].description", "Please enter a description of the image you have tried to upload.");
				tabIndex = 0;
			}
			
			if(!submission.isPrivate()) {
				submission.setAccessGroupIds(null);
			}
			
		}
		else {
			removeInterimModel();
			Map<String, Object> session = ActionContext.getContext().getSession();
			session.remove("modelSubmission");
			addActionError("An unexpected error occurred. Please resubmit your model.");
		}
	}
	
	private void validateImages() {
		List<DataFile> images = submission.getImages();
		for(int i=0;i<images.size();i++){
			DataFile dFile = (DataFile)images.get(i);
			if(dFile != null) {
				if(dFile.getDataFile() == null && dFile.getDescription() != null && !dFile.getDescription().equals("")) {
					addFieldError("submission.images["+i+"].dataFile", "Please provide a file to accompany the corresponding description you have entered.");
					tabIndex = 0;
					if(dFile.getDescription().length() > DataConstants.FILE_SHORT_DESCRIPTION) {
						addFieldError("submission.images["+i+"].description", "File description exceeds the maximum character length of " +DataConstants.FILE_SHORT_DESCRIPTION);
						tabIndex = 0;
					}
				}
				if((dFile.getDescription() == null || dFile.getDescription().equals("")) && dFile.getDataFile()!=null) {
					addFieldError("submission.images["+i+"].description", "Please enter a description of the file you have tried to upload.");
					if(dFile.getDataFileFileName().length() > DataConstants.FILE_FILENAME) {
						addFieldError("submission.images["+i+"].dataFile", "File name exceeds the maximum character length of "+DataConstants.FILE_FILENAME);
						tabIndex = 0;
					}
				}
			}
		}
		submission.setImages(images);
	}
	
	private void validateSupplementaryFiles() {
		List<DataFile> supplementaryFiles = submission.getSupplementaryFiles();
		for(int i=0;i<supplementaryFiles.size();i++){
			DataFile dFile = (DataFile)supplementaryFiles.get(i);
			if(dFile != null){
				if(dFile.getDataFile() == null && dFile.getDescription() != null && !dFile.getDescription().equals("")) {
					addFieldError("submission.supplementaryFiles["+i+"].dataFile", "Please provide a file to accompany the corresponding data file description you have entered.");
					tabIndex = 0;
					if(dFile.getDescription().length() > DataConstants.FILE_SHORT_DESCRIPTION) {
						addFieldError("submission.supplementaryFiles["+i+"].description", "File description exceeds the maximum character length of " +DataConstants.FILE_SHORT_DESCRIPTION);
						tabIndex = 0;
					}
				}
				if((dFile.getDescription() == null || dFile.getDescription().equals("")) && dFile.getDataFile()!=null) {
					addFieldError("submission.supplementaryFiles["+i+"].description", "Please enter a description of the data file you have tried to upload.");
					if(dFile.getDataFileFileName().length() > DataConstants.FILE_FILENAME) {
						addFieldError("submission.supplementaryFiles["+i+"].dataFile", "File name exceeds the maximum character length of "+DataConstants.FILE_FILENAME);
						tabIndex = 0;
					}
				}
			}
		}
		submission.setSupplementaryFiles(supplementaryFiles);
	}
	
	private void validateAttributes() {
		List<Attribute> attributes = submission.getAttributes();
		for(int i=0;i<attributes.size();i++){
			Attribute att = attributes.get(i);
			if(att != null) {
				if((att.getName() == null || att.getName().trim().equals("")) && (att.getValue() == null || att.getValue().trim().equals(""))) {
					attributes.remove(i);
					i--;
				}
				else {
					if(att.getName() == null || att.getName().trim().equals("")) {
						addFieldError("submission.attributes["+i+"].name","Please enter an appropriate name to identify the attribute value you have entered or remove this attribute entry from the submission.");
						tabIndex = 0;
					}
					else {
						if(att.getName().length() > DataConstants.ATTRIBUTE_NAME) {
							addFieldError("submission.attributes["+i+"].name","Attribute name exceeds the maximum character length of "+DataConstants.ATTRIBUTE_NAME);
							tabIndex = 0;
						}
					}
					if(att.getValue() == null || att.getValue().trim().equals("")){
						addFieldError("submission.attributes["+i+"].value","Please enter an appropriate value to accompany the attribute name you have entered or remove this attribute entry from the submission.");
						tabIndex = 0;
					}
					/*else {
						if(att.getValue().length() > DataConstants.ATTRIBUTE_VALUE) {
							addFieldError("submission.attributes["+i+"].value","Attribute value exceeds the maximum character length of "+DataConstants.ATTRIBUTE_VALUE);
							tabIndex = 0;
						}
					}*/
				}
			}
			else {
				attributes.remove(i);
				i--;
			}
		}
		submission.setAttributes(attributes);
	} 
	
	private void validatePublications() {
		List<Publication> publications = submission.getPublications();
		for(int i=0;i<publications.size();i++) {
			Publication publ = (Publication)publications.get(i);
			if(publ == null) {
				publications.remove(i);
				addFieldError("allPublications","An unexpected error occurred. Please check your submission details and try again.");
			}
			else {
				if(publ.getReferenceType() != null && publ.getReferenceType().equals("Journal Article")){
					if(publ.getPeriodicalName() == null || publ.getPeriodicalName().equals("")){
						addFieldError("submission.publications["+i+"].periodicalName", "Please provide a journal name for this publication");
					}
					else {
						if(publ.getPeriodicalName().length() > DataConstants.PUBLICATION_PERIODICAL_NAME) {
							addFieldError("submission.publications["+i+"].periodicalName", "The journal name exceeds the maximum character length of " + DataConstants.PUBLICATION_PERIODICAL_NAME);
						}
					}
					if(publ.getTitle() == null || publ.getTitle().equals("")){
						addFieldError("submission.publications["+i+"].title", "Please provide the title of this publication");
					}
					else {
						if(publ.getTitle().length() > DataConstants.PUBLICATION_TITLE) {
							addFieldError("submission.publications["+i+"].title", "The title exceeds the maximum character length of " + DataConstants.PUBLICATION_TITLE);
						}
					}
					if(publ.getAuthors() == null || publ.getAuthors().equals("")){
						addFieldError("submission.publications["+i+"].authors","Please provide the authors of this publication");
					}
					else {
						if(publ.getAuthors().length() > DataConstants.PUBLICATION_AUTHORS) {
							addFieldError("submission.publications["+i+"].authors","Authors exceeds the maximum character length of " + DataConstants.PUBLICATION_AUTHORS);
						}
					}
					if(publ.getYear() != null && !publ.getYear().equals("")){
						try {
							Integer.parseInt(publ.getYear());
						}
						catch(NumberFormatException e){
							addFieldError("submission.publications["+i+"].year", "Please provide a valid publication year for this publication");
						}
					}
					else {
						addFieldError("submission.publications["+i+"].year", "Please provide the publication year of this publication");
					}
					if(publ.getUrl() != null && publ.getUrl().length() > DataConstants.PUBLICATION_URL) {
						addFieldError("submission.publications["+i+"].url", "URL exceeds the maximum character length of "+DataConstants.PUBLICATION_URL);
					}
					/*if(publ.getAbstract() != null && publ.getAbstract().length() > DataConstants.PUBLICATION_ABSTRACT){
						addFieldError("submission.publications["+i+"].abstract", "Abstract exeeds the maximum character length of "+ DataConstants.PUBLICATION_ABSTRACT);
					}*/
				}
				else if(publ.getReferenceType() != null && publ.getReferenceType().equals("Book")){
					if(publ.getTitle() == null || publ.getTitle().equals("")){
						addFieldError("submission.publications["+i+"].title", "Please provide the title of this publication");
					}
					else {
						if(publ.getTitle().length() > DataConstants.PUBLICATION_TITLE) {
							addFieldError("submission.publications["+i+"].title", "The title exceeds the maximum character length of " + DataConstants.PUBLICATION_TITLE);
						}
					}
					if(publ.getYear() != null && !publ.getYear().equals("")){
						try {
							Integer.parseInt(publ.getYear());
						}
						catch(NumberFormatException e){
							addFieldError("submission.publications["+i+"].year", "Please provide a valid publication year for this publication");
						}
					}
					else {
						addFieldError("submission.publications["+i+"].year", "Please provide the publication year of this publication");
					}
					if(publ.getAuthors() != null && publ.getAuthors().length() > DataConstants.PUBLICATION_AUTHORS) {
						addFieldError("submission.publications["+i+"].authors","Authors exceeds the maximum character length of " + DataConstants.PUBLICATION_AUTHORS);
					}
					if(publ.getSecondaryTitle() != null && publ.getSecondaryTitle().length() > DataConstants.PUBLICATION_TITLE) {
						addFieldError("submission.publications["+i+"].secondaryTitle", "The title exceeds the maximum character length of " + DataConstants.PUBLICATION_TITLE);
					}
					if(publ.getSecondaryAuthors() != null && publ.getSecondaryAuthors().length() > DataConstants.PUBLICATION_AUTHORS) {
						addFieldError("submission.publications["+i+"].secondaryAuthors","Authors exceeds the maximum character length of " + DataConstants.PUBLICATION_AUTHORS);
					}
					if(publ.getPages() != null && publ.getPages().length() > DataConstants.PUBLICATION_PAGES) {
						addFieldError("submission.publications["+i+"].pages","Pages exceeds the maximum character length of " + DataConstants.PUBLICATION_PAGES);
					}
					if(publ.getPublisher() != null && publ.getPublisher().length() > DataConstants.PUBLICATION_PUBLISHER) {
						addFieldError("submission.publications["+i+"].publisher","Publisher exceeds the maximum character length of " + DataConstants.PUBLICATION_PUBLISHER);
					}
					if(publ.getIsbn() != null && publ.getIsbn().length() > DataConstants.PUBLICATION_ISBN) {
						addFieldError("submission.publications["+i+"].isbn","ISBN exceeds the maximum character length of " + DataConstants.PUBLICATION_ISBN);
					}
					if(publ.getUrl() != null && publ.getUrl().length() > DataConstants.PUBLICATION_URL) {
						addFieldError("submission.publications["+i+"].url", "URL exceeds the maximum character length of "+DataConstants.PUBLICATION_URL);
					}
				}
				else {
					publications.remove(i);
					addFieldError("allPublications","An unexpected error occurred. Please check your submission details and try again.");
				}
			}
		}
		submission.setPublications(publications);
	}

}
