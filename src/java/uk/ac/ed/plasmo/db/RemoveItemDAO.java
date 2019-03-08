package uk.ac.ed.plasmo.db;

/**
 * DAO responsible for removing database data 
 * @author ctindal
 *
 */
public interface RemoveItemDAO {
	
	/**
	 * deletes an xml model from it's temporary location in the database
	 * @param oid the temporary id of the model
	 */
	public void deleteInterimModel(String oid);
	
	/**
	 * deletes a model and all associated files/meta data from the database and file system
	 * @param accession the accession id of the model
	 * @return
	 */
	public String deleteModel(String accession);

        /**
         * Hides a model marking is as deleted.
         * @param accession the accession id of the model
         * @return number of affected rows (1) on succes
         */
        public int hideModel(String accession);
}
