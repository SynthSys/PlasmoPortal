package uk.ac.ed.plasmo.entity;

public class WSModelSubmission extends MdlSubmission {
	
	private String modelAsString;
	private String format;

	public String getModelAsString() {
		return modelAsString;
	}
	public void setModelAsString(String modelAsString) {
		this.modelAsString = modelAsString;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
}
