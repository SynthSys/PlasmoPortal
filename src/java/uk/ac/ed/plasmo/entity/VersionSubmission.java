package uk.ac.ed.plasmo.entity;

import java.io.File;
import java.util.List;

public class VersionSubmission extends VsnSubmission {
	
	private String name;
	private String [] supFileIds, imageIds;
	private List<DataFile> images, supplementaryFiles;
	private File model;
	private String modelFileName, modelContentType;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String [] getImageIds() {
		return imageIds;
	}
	public void setImageIds(String [] imageIds) {
		this.imageIds = imageIds;
	}
	public String [] getSupFileIds() {
		return supFileIds;
	}
	public void setSupFileIds(String [] supFileIds) {
		this.supFileIds = supFileIds;
	}
	public List<DataFile> getImages() {
		return images;
	}
	public void setImages(List<DataFile> images) {
		this.images = images;
	}
	public List<DataFile> getSupplementaryFiles() {
		return supplementaryFiles;
	}
	public void setSupplementaryFiles(List<DataFile> supplementaryFiles) {
		this.supplementaryFiles = supplementaryFiles;
	}
	public File getModel() {
		return model;
	}
	public void setModel(File model) {
		this.model = model;
	}
	public String getModelFileName() {
		return modelFileName;
	}
	public void setModelFileName(String modelFileName) {
		this.modelFileName = modelFileName;
	}
	public String getModelContentType() {
		return modelContentType;
	}
	public void setModelContentType(String modelContentType) {
		this.modelContentType = modelContentType;
	}
}
