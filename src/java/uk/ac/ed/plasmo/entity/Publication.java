package uk.ac.ed.plasmo.entity;

public class Publication {
	
        private long pubOID;
	private String referenceType;
	private String periodicalName;
	private String title;
	private String authors;
	private String secondaryTitle;
	private String secondaryAuthors;
	private String _abstract;
	private String url;
	private String pages;
	private String publisher;
	private String isbn;
	private String year;
	
	public String getReferenceType() {
		return referenceType;
	}
	
	public void setReferenceType(String value){
		this.referenceType = value;
	}
	
	public String getPeriodicalName() {
		return periodicalName;
	}
	
	public void setPeriodicalName(String value){
		this.periodicalName = value;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String value){
		this.title = value;
	}
	
	public String getAuthors() {
		return authors;
	}
	
	public void setAuthors(String value){
		this.authors = value;
	}
	
	public String getSecondaryTitle() {
		return secondaryTitle;
	}
	
	public void setSecondaryTitle(String value){
		this.secondaryTitle = value;
	}
	
	public String getSecondaryAuthors() {
		return secondaryAuthors;
	}
	
	public void setSecondaryAuthors(String value){
		this.secondaryAuthors = value;
	}
	
	public String getAbstract() {
		return _abstract;
	}
	
	public void setAbstract(String value){
		this._abstract = value;
	}
	
	public String getHref() {
            if (url == null) return null;
            if (url.startsWith("http")) return url;
            else return "http://"+url;            
	}
        
        public String getUrl() {
            return url;
        }
	
	public void setUrl(String value){
		this.url = value;
	}
	
	public String getPages() {
		return pages;
	}
	
	public void setPages(String value){
		this.pages = value;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String value){
		this.publisher = value;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String value){
		this.isbn = value;
	}
	
	public String getYear(){
		return year;
	}
	
	public void setYear(String value){
		this.year = value;
	}

    public long getPubOID() {
        return pubOID;
    }

    public void setPubOID(long pubOID) {
        this.pubOID = pubOID;
    }
        
        
}