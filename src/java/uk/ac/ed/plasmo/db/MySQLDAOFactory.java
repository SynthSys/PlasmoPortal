package uk.ac.ed.plasmo.db;

/**
 * Class resonsible for initialisation of all DAOs
 * @author ctindal
 *
 */
public final class MySQLDAOFactory {
	
	public MySQLDAOFactory() {
		
	}
	
	public static SecurityDAO getSecurityDAO(){
		return new MySQLSecurityDAOImpl();
	}
	
	public static NewItemDAO getNewItemDAO() {
		return new MySQLNewItemDAOImpl();
	}
	
	public static RemoveItemDAO getRemoveItemDAO() {
		return new MySQLRemoveItemDAOImpl();
	}
	
	public static QueryItemDAO getQueryItemDAO() {
		return new MySQLQueryItemDAOImpl();
	}
	
	public static ModifyItemDAO getModifyItemDAO() {
		return new MySQLModifyItemDAOImpl();
	}
}
