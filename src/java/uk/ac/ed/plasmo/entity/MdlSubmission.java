package uk.ac.ed.plasmo.entity;

public class MdlSubmission {
	
	private String accession, name, description;
	
	private boolean _private;
	
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isPrivate() {
		return _private;
	}
	public void setPrivate(boolean _private) {
		this._private = _private;
	}
}
