package uk.ac.ed.plasmo.entity;
import java.util.List;

import uk.ac.ed.plasmo.assembler.UserAssembler;
import uk.ac.ed.plasmo.persistence.RefGroup;

public class ModelSubmission extends MdlSubmission {
	
	private String tempId;
	private List<DataFile> supplementaryFiles;
	private List<DataFile> images;
	private List<RefGroup> groups;
	private int [] accessGroupIds;
	private List<Publication> publications;
	private List<Attribute> attributes;
	private boolean unpublished;
	
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public List<DataFile> getSupplementaryFiles() {
		return supplementaryFiles;
	}
	public void setSupplementaryFiles(List<DataFile> supplementaryFiles) {
		this.supplementaryFiles = supplementaryFiles;
	}
	public List<DataFile> getImages() {
		return images;
	}
	public void setImages(List<DataFile> images) {
		this.images = images;
	}
	public List<Publication> getPublications() {
		return publications;
	}
	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public boolean isUnpublished() {
		return unpublished;
	}
	public void setUnpublished(boolean unpublished) {
		this.unpublished = unpublished;
	}
	public int[] getAccessGroupIds() {
		return accessGroupIds;
	}
	public void setAccessGroupIds(int[] accessGroupIds) {
		this.accessGroupIds = accessGroupIds;
	}
	public List<RefGroup> getGroups() {
		UserAssembler assembler = new UserAssembler();
		groups = assembler.getUserGroups(); 
		for(RefGroup g : groups) {
			if(accessGroupIds!= null && accessGroupIds.length > 0) {
				for(int gId : accessGroupIds) {
					if(gId == g.getGrpOid()) {
						g.setSelected(true);
						break;
					}
				}
			}
		}
		return groups;
	}
	
	public void setGroups(List<RefGroup> groups) {
		this.groups = groups;
	}
}
