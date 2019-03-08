package uk.ac.ed.plasmo.entity;

import java.util.List;

public class ModelData {
	
	private String name;
	private String description;
	private String submissionDate;
	private String accession;
	private String format;
	private String version;
	private int maxVersion;
	private List<Integer> versions;
	private String versionComment;
	private User submitter;
	private User owner;
	private List<User> creators;
	private List<Publication> publications;
	private List<String []> images;
	private List<String []> supplementaryFiles;
	private List<Attribute> attributes;
	private List<String []> transformOptions;
	private List<String []> comments;
	private List<GroupAttributes> groupAttributes;
	private boolean _private;
	private String simileWebURL;

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
	public String getSubmissionDate() {
		return submissionDate;
	}
	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}
	public String getAccession() {
		return accession;
	}
	public void setAccession(String accession) {
		this.accession = accession;
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
	public int getMaxVersion() {
		return maxVersion;
	}
	public void setMaxVersion(int maxVersion) {
		this.maxVersion = maxVersion;
	}
	public List<Integer> getVersions() {
		return versions;
	}
	public void setVersions(List<Integer> versions) {
		this.versions = versions;
	}
	public String getVersionComment() {
		return versionComment;
	}
	public void setVersionComment(String versionComment) {
		this.versionComment = versionComment;
	}
	public User getSubmitter() {
		return submitter;
	}
	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public List<User> getCreators() {
		return creators;
	}
	public void setCreators(List<User> creators) {
		this.creators = creators;
	}
	public List<Publication> getPublications() {
		return publications;
	}
	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}
	public List<String []> getImages() {
		return images;
	}
	public void setImages(List<String []> images) {
		this.images = images;
	}
	public List<String []> getSupplementaryFiles() {
		return supplementaryFiles;
	}
	public void setSupplementaryFiles(List<String []> supplementaryFiles) {
		this.supplementaryFiles = supplementaryFiles;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public List<String[]> getTransformOptions() {
		return transformOptions;
	}
	public void setTransformOptions(List<String[]> transformOptions) {
		this.transformOptions = transformOptions;
	}
	public List<String[]> getComments() {
		return comments;
	}
	public void setComments(List<String[]> comments) {
		this.comments = comments;
	}
	public boolean isPrivate() {
		return _private;
	}
	public void setPrivate(boolean _private) {
		this._private = _private;
	}
	public String getSimileWebURL() {
		return simileWebURL;
	}
	public void setSimileWebURL(String simileWebURL) {
		this.simileWebURL = simileWebURL;
	}
	public void setGroupAttributes(List<GroupAttributes> groupAttributes) {
		this.groupAttributes = groupAttributes;
	}
	public List<GroupAttributes> getGroupAttributes() {
		return this.groupAttributes;
	}

}
