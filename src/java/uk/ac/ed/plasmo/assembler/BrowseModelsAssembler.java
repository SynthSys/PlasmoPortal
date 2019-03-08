package uk.ac.ed.plasmo.assembler;

import java.util.ArrayList;
import java.util.Map;

import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.QueryItemDAO;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.table.TableAssembler;
/**
 * The {@code BrowseModelsAssembler} class is used to initialise data access object 
 * and call methods used to build tabular data of all models available to a 
 * specific user in the database or if the user is null, all public models.
 * @author ctindal
 *
 */
public class BrowseModelsAssembler extends TableAssembler {
	
	private User user; //the user accessing the data
	
	public BrowseModelsAssembler() {}
	
	public BrowseModelsAssembler(Map<String, Object> params) {
		super(params);
                //System.out.println("Browse models tb asembler");
	}
	
	@Override
	public void setParams() {
		user = (User) getParam("user");
	}

	@Override
	public ArrayList<String []> retrieveData(int column, boolean ascending, int offset,
			int num) {
		
            //System.out.println("BrowseModelsAssembler retrieve");
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		
		ArrayList<String[]> models = queryItemDAO.browseModels(column, ascending, offset, num, user);
		
		return models;
	}

	@Override
	public int retrieveNumberOfRows() {
		
            //System.out.println("BrowseModelsAssembler numbers of rows");
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		
		int numberOfRows = queryItemDAO.getTotalNumberOfModels(user);
		
		return numberOfRows;
	}

	

}
