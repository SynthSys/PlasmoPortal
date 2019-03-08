package uk.ac.ed.plasmo.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import uk.ac.ed.plasmo.assembler.ModelFormatAssembler;
import uk.ac.ed.plasmo.db.DataConstants;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.xml.SchemaValidator;

/**
 * adds a new user-defined model format to the database. The user can specify an optional 
 * XML schema used to validate models of this format 
 * @author ctindal
 *
 */
public class AddModelFormat extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private String format, description;
	private String schemaContentType, schemaFileName;
	private File schema;
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSchemaContentType() {
		return schemaContentType;
	}
	public void setSchemaContentType(String schemaContentType) {
		this.schemaContentType = schemaContentType;
	}
	public String getSchemaFileName() {
		return schemaFileName;
	}
	public void setSchemaFileName(String schemaFileName) {
		this.schemaFileName = schemaFileName;
	}
	public File getSchema() {
		return schema;
	}
	public void setSchema(File schema) {
		this.schema = schema;
	}
	
	public String execute() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("user");
		
		//no model or user in session means error in prior submission stage 
		if(user == null){
			return INPUT;
		}
		
		ModelFormatAssembler assembler = new ModelFormatAssembler();
		String message = assembler.addNewModelFormat(format, description, schema, schemaFileName, user);
		
		if(message != null){
			addFieldError("format",message);
			return INPUT;
		}
		else {
			return SUCCESS;
		}
	}
	
	public void validate() {
		if(schema != null) {
			//check that the schema is valid
			ArrayList<String> errorList = (ArrayList<String>) SchemaValidator.validateSchema(schema);
			//if the schema has errors, notify user
			if(errorList.size() != 0) {
				for(int i=0;i<errorList.size();i++){
					addFieldError("validSchema", errorList.get(i));
				}
			}
		}
		
		if(description != null) {
			if(description.length() > DataConstants.FILE_DESCRIPTION) {
				addFieldError("description", "Descripton exceeds maximum character value of " +DataConstants.FILE_DESCRIPTION);
			}
		}
		
	}
	

}
