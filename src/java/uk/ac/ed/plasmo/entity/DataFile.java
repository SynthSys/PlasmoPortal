package uk.ac.ed.plasmo.entity;

import java.io.File;

public class DataFile {
	
	private String id;
	private File dataFile;
	private String dataFileFileName, dataFileContentType;
	private String description;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void setDataFile(File dataFile){
		this.dataFile = dataFile;
	}
	
	public File getDataFile(){
		return dataFile;
	}
	
	public void setDataFileFileName(String dataFileFileName){
		this.dataFileFileName = dataFileFileName;
	}
	
	public String getDataFileFileName(){
		return dataFileFileName;
	}
	
	public void setDataFileContentType(String dataFileContentType){
		this.dataFileContentType = dataFileContentType;
	}
	
	public String getDataFileContentType() {
		return dataFileContentType;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
}