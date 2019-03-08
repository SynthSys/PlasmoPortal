package uk.ac.ed.plasmo.entity;

/**
 * Class for storing group access information
 * pertaining to a model
 * @author martin
 *
 */
public class GroupAttributes {
	private String name;
	private Boolean access;
	private Boolean edit;
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setAccess(Boolean access) {
		this.access = access;
	}
	public Boolean getAccess() {
		return access;
	}
	public void setEdit(Boolean edit) {
		this.edit = edit;
	}
	public Boolean getEdit() {
		return edit;
	}

}
