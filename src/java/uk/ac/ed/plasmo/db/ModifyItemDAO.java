package uk.ac.ed.plasmo.db;

import uk.ac.ed.plasmo.entity.GroupAttributes;
import uk.ac.ed.plasmo.entity.Publication;
import uk.ac.ed.plasmo.entity.User;

/**
 * DAO responsible for making any modifications/updates to database data 
 * @author ctindal
 *
 */
public interface ModifyItemDAO {
	
	/**
	 * sets the status of a model in the database to deleted
	 * @param accession
	 * @param delete
	 */
	public void setModelIsDeleted(String accession, boolean delete);
	
	/**
	 * updates the id of a model owner
	 * @param supervisor
	 * @param newOwner
	 * @param accession
	 * @return
	 */
	public String updateModelOwnership(User supervisor, User newOwner, String accession);
	
	public void updateGroupPermissions(String accession, GroupAttributes grpAttribute);


}
