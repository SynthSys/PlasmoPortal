package uk.ac.ed.plasmo.utility.table;

public class HeaderItem {
	
	private String title;
	private boolean sortable;
	private int total;
	
	public HeaderItem(String title, boolean sortable) {
		this.title = title;
		this.sortable = sortable;
		//this.total = total;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isSortable() {
		return sortable;
	}
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
