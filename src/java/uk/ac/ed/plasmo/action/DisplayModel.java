package uk.ac.ed.plasmo.action;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.dispatcher.HttpParameters;

import uk.ac.ed.plasmo.assembler.ModelAssembler;
import uk.ac.ed.plasmo.entity.GroupAttributes;
import uk.ac.ed.plasmo.entity.ModelData;
import uk.ac.ed.plasmo.utility.Constants;

/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve data about a specific model (using it's accession id) so it can be displayed to the
 * user</p>
 * <p>If the model is flagged as private in the database, the user must be logged in and 
 * have sufficient privileges to access the model.</p> 
 * @author ctindal
 *
 */
public class DisplayModel extends DisplayModelOperations{
	
	private static final long serialVersionUID = 1L;
	
	
	private String version;
	private ModelData modelData;

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public ModelData getModelData() {
		return modelData;
	}
	public void setModelData(ModelData modelData) {
		this.modelData = modelData;
	}
	
	public String execute() {
		
            
		String result = this.checkAuthorisation();
		if(!result.equals(SUCCESS)) {
			return result;
		}
		
		ModelAssembler assembler = new ModelAssembler();
		modelData = assembler.getModelData(getAccession(), version, getUser());
		
		if(modelData == null) {
			return INPUT;
		}
		else {
			
			//Check if the client has updated the permissions
			HttpParameters parameters = null;
			if(ActionContext.getContext() != null){
				parameters = ActionContext.getContext().getParameters();
				if(parameters.containsKey("updategrouppermission")) //checks if the button was pressed
				{
					List<GroupAttributes> grpAttributes = modelData.getGroupAttributes();
					for(String key : parameters.keySet()){
						/*Object o = parameters.get(key);
						if(o instanceof String[] == false)
							continue;
						String[] res = (String[])o;
						String value = res[0];*/
                                                String value = parameters.get(key).getValue();
						//check if there is anything different
						for(GroupAttributes grpAttribute : grpAttributes){
							if(grpAttribute.getName().compareTo(key) != 0)
								continue;
							boolean dirty= false;
							//Check the current permission
							if(value.compareTo("access") == 0){
								if(grpAttribute.getAccess()!=true){
									grpAttribute.setAccess(true);
									dirty=true;
								}
							}
							else if(value.compareTo("edit") == 0){
								if(grpAttribute.getEdit() != true){
									grpAttribute.setEdit(true);
									dirty=true;
								}
							}
							else if(value.compareTo("noaccess") == 0){
								if(grpAttribute.getEdit() == true || grpAttribute.getAccess()==true){
									grpAttribute.setEdit(false);
									grpAttribute.setAccess(false);
									dirty=true;
								}
							}
							if(dirty == true){
								ModelAssembler modelAssembler = new ModelAssembler();
								modelAssembler.updateGroupPermissions(modelData.getAccession(), grpAttribute);
							}
						}
					}
				}	
			}
			
                        /*
                        Disabled by TZ as the URL http://www.simileweb.com/models/plasmo/
                        no longer exists. And I don't know where it migrated.
                        
			try {
				
				String simileWebURL = Constants.SIMILEWEB_PLASMO_URL+modelData.getAccession()+"/";
				
				URL simileWeb = new URL(simileWebURL);
				
				HttpURLConnection urlConn = (HttpURLConnection) simileWeb.openConnection();
				
				int responseCode = urlConn.getResponseCode();
				
				if(responseCode == 200) { 
					modelData.setSimileWebURL(simileWebURL);
				}
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			
			version = modelData.getVersion();
			return SUCCESS;
		}
	}

}
