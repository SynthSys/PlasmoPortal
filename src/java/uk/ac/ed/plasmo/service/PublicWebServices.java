package uk.ac.ed.plasmo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ed.plasmo.action.TableView;
import uk.ac.ed.plasmo.assembler.BrowseModelsAssembler;
import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.assembler.ModelFormatAssembler;
import uk.ac.ed.plasmo.assembler.SearchModelsAssembler;
import uk.ac.ed.plasmo.assembler.SecurityAssembler;
import uk.ac.ed.plasmo.entity.ModelData;
import uk.ac.ed.plasmo.entity.ModelSummary;
import uk.ac.ed.plasmo.exception.InvalidModelException;
import uk.ac.ed.plasmo.exception.InvalidUserException;
import uk.ac.ed.plasmo.utility.table.Table;
import uk.ac.ed.plasmo.utility.table.TableAssembler;

/**
 * class allowing a user to retrieve data for all publicly available models in the 
 * database via a web service. 
 * @author ctindal
 *
 */
public class PublicWebServices {
	
	/**
	 * get a list of ids of all public models in the database
	 * @return
	 */
	public String [] getPublicModelsIds() {
		
		ModelAssembler assembler = new ModelAssembler();
		
		ArrayList<String> idList = assembler.getAllModelIds();
		
		String [] ids = new String [idList.size()];
		
		for(int i=0;i<idList.size();i++) {
			ids[i] = idList.get(i);
		}
		
		return ids;
	}
	
	/**
	 * retrieve a public model using the model's unique id
	 * @param accession the id of the model to be retrieved
	 * @return
	 */
	public String getPublicModelById(String accession) {
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		Boolean authorised = secAssembler.isAuthorisedToAccessModel(null, accession);
		
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
	
	/**
	 * get the name of a model with a specific id
	 * @param id the id of the model
	 * @return
	 */
	public String getModelNameById(String accession) {
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		Boolean authorised = secAssembler.isAuthorisedToAccessModel(null, accession);
		
		if(authorised == null) {
			throw new InvalidModelException("Model with accession "+accession+" does not exist in the database.");
		}
		if(!authorised) {
			throw new InvalidUserException("You are not authorised to access model with accession "+accession);
		}
		
		ModelAssembler assembler = new ModelAssembler();
		
		String modelName = assembler.getModelName(accession);
		
		return modelName;
	}
	
	/**
	 * get all the public model ids matching a specific model name
	 * @param name the name of the model
	 * @return
	 */
	public String [] getPublicModelIdsByName(String name) {
		
		ModelAssembler assembler = new ModelAssembler();
		
		ArrayList<String> idList = assembler.getModelsIdsByName(name);
		
		if(idList == null) {
			throw new InvalidModelException("There are no models with name \""+name+"\" in the database.");
		}
		
		String [] ids = new String [idList.size()];
		
		for(int i=0;i<idList.size();i++) {
			ids[i] = idList.get(i);
		}
		
		return ids;
	}
	
	/**
	 * get all the model ids where the model is in a specific format
	 * @param format the format of the model
	 * @return
	 */
	public String [] getPublicModelIdsByFormat(String format) {
		ModelAssembler assembler = new ModelAssembler();
		
		ArrayList<String> idList = assembler.getModelIdsByFormat(format);
		
		String [] ids = new String [idList.size()];
		
		for(int i=0;i<idList.size();i++) {
			ids[i] = idList.get(i);
		}
		
		return ids;
	}
	
	/**
	 * get a list of all model formats in the database
	 * @return
	 */
	public String [] getModelFormats() {
		ModelFormatAssembler assembler = new ModelFormatAssembler();
		ArrayList<String> formatList = (ArrayList<String>) assembler.getModelFormats();
		
		String [] formats = new String [formatList.size()];
		
		for(int i=0;i<formatList.size();i++) {
			formats[i] = formatList.get(i);
		}
		
		return formats;
	}
	
	/**
	 * get a list of model summaries for all public models in the 
	 * database with attributes matching a specific search term. The search is carried out against
	 * the name of the model, the model format, the user name of the model owner, the name of the 
	 * model owner and any of the model's descriptive attributes. If the search term used is '*' then the
	 * program will search for all public models stored in the database. 
	 * The set of returned data is ordered by model name.
	 * @param offset the start point from which the first model is retrieved. E.g. if the offset is 
	 * 1 and numModels is 5, only the 1st 5 models in the database will be returned. If the offset is 2 and numModels
	 * is 5, the 2nd group of 5 models will be returned (models 1-5 will be skipped).
	 * @param numModels the number of models to be retrieved. Returned lists are limited to the following sizes: 5, 10, 20, 50, 100
	 * @return a list of model summaries
	 */
	public ModelSummary [] getSummaryOfAllPublicModelsSearch(int offset, int numModels, String searchTerm) {
		
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
		
		Map<String, Object> qParams = new HashMap<String, Object>();
		qParams.put("query", searchTerm);
		
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
	 * get a list of model summaries for all public models in the database. The set of returned data is ordered
	 * by model name.
	 * @param offset the start point from which the first model is retrieved. E.g. if the offset is 
	 * 1 and numModels is 5, only the 1st 5 models in the database will be returned. If the offset is 2 and numModels
	 * is 5, the 2nd group of 5 models will be returned (models 1-5 will be skipped).
	 * @param numModels the number of models to be retrieved. Returned lists are limited to the following sizes: 5, 10, 20, 50, 100
	 * @return a list of model summaries
	 */
	public ModelSummary [] getSummaryOfAllPublicModels(int offset, int numModels) {
		
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
		
		TableAssembler assembler = new BrowseModelsAssembler();
		
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
	 * get a summary of a model with a specific accession id
	 * @param id the id of the model 
	 * @return a summary of the model
	 */
	public ModelSummary getModelSummaryById(String accession) {
		
		SecurityAssembler secAssembler = new SecurityAssembler();
		Boolean authorised = secAssembler.isAuthorisedToAccessModel(null, accession);
		
		if(authorised == null) {
			throw new InvalidModelException("Model with accession "+accession+" does not exist in the database.");
		}
		if(!authorised) {
			throw new InvalidUserException("You are not authorised to access model with accession "+accession);
		}
		
		
		ModelAssembler assembler = new ModelAssembler();
		ModelData data = assembler.getModelData(accession, null,null);
		
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
	
}
