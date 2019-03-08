/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.plasmo.action;

import ed.robust.pubmed.client.JournalArticleRecord;
import ed.robust.pubmed.client.PubMedClient;
import ed.robust.pubmed.client.PubMedError;
import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.assembler.PublicationAssembler;
import uk.ac.ed.plasmo.entity.ModelSubmission;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.utility.Constants;

/**
 *
 * @author tzielins
 */
public class ImportPublication extends EditModelOperations {
    private static final long serialVersionUID = 1L;
    
    protected ModelSubmission model;
    long pubMedId;

    public void setPubMedId(long pubMedId) {
        this.pubMedId = pubMedId;
    }

    public ModelSubmission getModel() {
        return model;
    }

    
    
    @Override
    public String execute()  {
                
        if (getAccession() == null || getAccession().isEmpty()) return NO_MODEL;
        
        String result = this.checkAuthorisation();
        if(!result.equals(SUCCESS)) {
                return result;
        }
        
        model = getModelData(getAccession());
        
        if (pubMedId <= 0) return INPUT;
        
        try {
        JournalArticleRecord record = getPubMedRecord(pubMedId);
        
        Publication pub = convertToPublicatoin(record);
        

        PublicationAssembler assembler = new PublicationAssembler();
        assembler.savePublication(pub,getAccession());
        
        return SUCCESS;
        } catch (PubMedError e) {
            System.out.println("PubMed Error: "+e.getMessage());
            addActionError("PubMed error: "+e.getMessage());
            return INPUT;
        }
    }

    protected Publication convertToPublicatoin(JournalArticleRecord record) {
        
        Publication pub = new Publication();
        pub.setReferenceType("Journal Article");
        pub.setTitle(record.getTitle());
        pub.setPeriodicalName(record.getJournal());
        pub.setYear(""+record.getYear());
        StringBuilder authors = new StringBuilder();
        if (!record.getAuthors().isEmpty()) authors.append(record.getAuthors().get(0));
        if (record.getAuthors().size() > 1)
            for (String author : record.getAuthors().subList(1, record.getAuthors().size())) authors.append(", ").append(author);
        pub.setAuthors(authors.toString());
        String url = null;
        if (record.getDOI() != null) {
            url = "doi.org/"+record.getDOI();
        } else {
            url = "http://www.ncbi.nlm.nih.gov/pubmed/"+record.getPubMedId();
        }
        pub.setUrl(url);
        return pub;
    }

    protected JournalArticleRecord getPubMedRecord(long pubMedId) throws PubMedError {
        
        PubMedClient client = new PubMedClient(Constants.TOOL_EMAIL, Constants.TOOL_NAME);
        return client.getJournalArticle(pubMedId);
    }
    
    protected ModelSubmission getModelData(String accession) {
        ModelAssembler assembler = new ModelAssembler();
        return assembler.getModelDataForEditModelDisplay(accession);
    }
    
    
    
}
