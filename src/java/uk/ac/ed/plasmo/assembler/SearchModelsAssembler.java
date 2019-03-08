package uk.ac.ed.plasmo.assembler;

import java.util.ArrayList;
import java.util.Map;

import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.QueryItemDAO;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.table.TableAssembler;

/**
 * The {@code BrowseModelsAssembler} class is used to initialise data access object 
 * and call methods used to build tabular data of all models whose attributes
 * matching a user-defined search term. This search term is passed to the class inside the
 * {@code Map} object called {@code params} which can contain other relevant query parameters. 
 * @author ctindal
 *
 */
public class SearchModelsAssembler extends TableAssembler {
	
	private User user; //the user making the query
	private String queryParam; //the search term
	private String type; //the type of attribute to search on (not yet implemented)
	
	public SearchModelsAssembler(Map<String, Object> params) {
		super(params);
                //System.out.println("SeachModelsAssembler created");

	}
	
	public SearchModelsAssembler() {}

	@Override
	public void setParams() {
		user = (User) getParam("user");
		queryParam = (String) getParam("query");
		type = (String) getParam("type");
	}

	@Override
	public ArrayList<String[]> retrieveData(int column, boolean ascending,
			int offset, int num) {
		
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		
		ArrayList<String[]> models = queryItemDAO.searchModels(queryParam, column, ascending, offset, num, user);

		return models;
	}

	@Override
	public int retrieveNumberOfRows() {
		
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		
		int numberOfRows = queryItemDAO.getTotalModelsFromSearch(user, queryParam);
		
		return numberOfRows;
	}

}
