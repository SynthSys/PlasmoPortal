package uk.ac.ed.plasmo.assembler;

import java.util.ArrayList;

import uk.ac.ed.plasmo.db.MySQLDAOFactory;
import uk.ac.ed.plasmo.db.QueryItemDAO;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.table.TableAssembler;

/**
 * <p>The {@code UserModelsAssembler} class is used to initialise data access object 
 * and call methods used to build tabular data of all models owned by a 
 * specific user in the database.</p>
 * <p>**NB: This class and any functionality associated with it are not
 * yet completed. Further development is required.</p>
 * @author ctindal
 *
 */
public class UserModelsAssembler extends TableAssembler {
	
	private User user;
	
	public UserModelsAssembler() {}
	
	public UserModelsAssembler(User user) {
		this.user = user;
	}
	
	@Override
	public ArrayList<String[]> retrieveData(int column, boolean ascending,
			int offset, int num) {
		
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		
		ArrayList<String[]> models = queryItemDAO.browseUsersModels(column, ascending, offset, num, user);
		
		return models;
	}

	@Override
	public int retrieveNumberOfRows() {
		QueryItemDAO queryItemDAO = MySQLDAOFactory.getQueryItemDAO();
		int numberOfRows = queryItemDAO.getTotalNumberOfUserModels(user);
		return numberOfRows;
	}

}
