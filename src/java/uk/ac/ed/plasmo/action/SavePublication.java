/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.plasmo.action;

import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.List;
import uk.ac.ed.plasmo.assembler.PublicationAssembler;
import uk.ac.ed.plasmo.db.DataConstants;
import uk.ac.ed.plasmo.db.MySQLQueryItemDAOImpl;
import uk.ac.ed.plasmo.entity.Publication;

/**
 *
 * @author tzielins
 */
public class SavePublication extends EditPublication {

    @Override
    public Publication getPublication() {
        if (publication == null) publication = new Publication();
        return publication;
    }

    
    @Override
    public String execute() {
        
        String result = this.checkAuthorisation();
        if(!result.equals(SUCCESS)) {
                return result;
        };
        
        try {
        model = getModelData(getAccession());

        if (publication.getPubOID() > 0) {
            Publication previous = getPublication(getAccession(),publication.getPubOID());
            if (previous == null) return NO_MODEL;
        }
        
            PublicationAssembler assembler = new PublicationAssembler();
            assembler.savePublication(publication,getAccession());
            
        } catch (MySQLQueryItemDAOImpl.MissingModel | MySQLQueryItemDAOImpl.MissingPublication e) {
            return NO_MODEL;
        }
        
        return SUCCESS;
        
        
    }

    @Override
    public void validate() {
        validatePublication(publication);
    }
    
    protected void validatePublication(Publication publ) {
        
            
				if(publ.getReferenceType() != null && publ.getReferenceType().equals("Journal Article")){
					if(publ.getPeriodicalName() == null || publ.getPeriodicalName().equals("")){
						addFieldError("publication.periodicalName", "Please provide a journal name for this publication");
					}
					else {
						if(publ.getPeriodicalName().length() > DataConstants.PUBLICATION_PERIODICAL_NAME) {
							addFieldError("publication.periodicalName", "The journal name exceeds the maximum character length of " + DataConstants.PUBLICATION_PERIODICAL_NAME);
						}
					}
					if(publ.getTitle() == null || publ.getTitle().equals("")){
						addFieldError("publication.title", "Please provide the title of this publication");
					}
					else {
						if(publ.getTitle().length() > DataConstants.PUBLICATION_TITLE) {
							addFieldError("publication.title", "The title exceeds the maximum character length of " + DataConstants.PUBLICATION_TITLE);
						}
					}
					if(publ.getAuthors() == null || publ.getAuthors().equals("")){
						addFieldError("publication.authors","Please provide the authors of this publication");
					}
					else {
						if(publ.getAuthors().length() > DataConstants.PUBLICATION_AUTHORS) {
							addFieldError("publication.authors","Authors exceeds the maximum character length of " + DataConstants.PUBLICATION_AUTHORS);
						}
					}
					if(publ.getYear() != null && !publ.getYear().equals("")){
						try {
							Integer.parseInt(publ.getYear());
						}
						catch(NumberFormatException e){
							addFieldError("publication.year", "Please provide a valid publication year for this publication");
						}
					}
					else {
						addFieldError("publication.year", "Please provide the publication year of this publication");
					}
					if(publ.getUrl() != null && publ.getUrl().length() > DataConstants.PUBLICATION_URL) {
						addFieldError("publication.url", "URL exceeds the maximum character length of "+DataConstants.PUBLICATION_URL);
					}
					/*if(publ.getAbstract() != null && publ.getAbstract().length() > DataConstants.PUBLICATION_ABSTRACT){
						addFieldError("publication.abstract", "Abstract exeeds the maximum character length of "+ DataConstants.PUBLICATION_ABSTRACT);
					}*/
				}
				else if(publ.getReferenceType() != null && publ.getReferenceType().equals("Book")){
					if(publ.getTitle() == null || publ.getTitle().equals("")){
						addFieldError("publication.title", "Please provide the title of this publication");
					}
					else {
						if(publ.getTitle().length() > DataConstants.PUBLICATION_TITLE) {
							addFieldError("publication.title", "The title exceeds the maximum character length of " + DataConstants.PUBLICATION_TITLE);
						}
					}
					if(publ.getYear() != null && !publ.getYear().equals("")){
						try {
							Integer.parseInt(publ.getYear());
						}
						catch(NumberFormatException e){
							addFieldError("publication.year", "Please provide a valid publication year for this publication");
						}
					}
					else {
						addFieldError("publication.year", "Please provide the publication year of this publication");
					}
					if(publ.getAuthors() != null && publ.getAuthors().length() > DataConstants.PUBLICATION_AUTHORS) {
						addFieldError("publication.authors","Authors exceeds the maximum character length of " + DataConstants.PUBLICATION_AUTHORS);
					}
					if(publ.getSecondaryTitle() != null && publ.getSecondaryTitle().length() > DataConstants.PUBLICATION_TITLE) {
						addFieldError("publication.secondaryTitle", "The title exceeds the maximum character length of " + DataConstants.PUBLICATION_TITLE);
					}
					if(publ.getSecondaryAuthors() != null && publ.getSecondaryAuthors().length() > DataConstants.PUBLICATION_AUTHORS) {
						addFieldError("publication.secondaryAuthors","Authors exceeds the maximum character length of " + DataConstants.PUBLICATION_AUTHORS);
					}
					if(publ.getPages() != null && publ.getPages().length() > DataConstants.PUBLICATION_PAGES) {
						addFieldError("publication.pages","Pages exceeds the maximum character length of " + DataConstants.PUBLICATION_PAGES);
					}
					if(publ.getPublisher() != null && publ.getPublisher().length() > DataConstants.PUBLICATION_PUBLISHER) {
						addFieldError("publication.publisher","Publisher exceeds the maximum character length of " + DataConstants.PUBLICATION_PUBLISHER);
					}
					if(publ.getIsbn() != null && publ.getIsbn().length() > DataConstants.PUBLICATION_ISBN) {
						addFieldError("publication.isbn","ISBN exceeds the maximum character length of " + DataConstants.PUBLICATION_ISBN);
					}
					if(publ.getUrl() != null && publ.getUrl().length() > DataConstants.PUBLICATION_URL) {
						addFieldError("publication.url", "URL exceeds the maximum character length of "+DataConstants.PUBLICATION_URL);
					}
				}
				
	}
    
    
    
}
