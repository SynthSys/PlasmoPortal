package uk.ac.ed.plasmo.entity;

public class VsnSubmission {
	
	private String accession, comments, format, version, modelAsString;
	
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getModelAsString() {
		return modelAsString;
	}
	public void setModelAsString(String modelAsString) {
		this.modelAsString = modelAsString;
	}

}
