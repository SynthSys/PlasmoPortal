package uk.ac.ed.plasmo.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import uk.ac.ed.plasmo.utility.Constants;

import com.opensymphony.xwork2.ActionSupport;
/**
 * <p>Action class that displays static content to the user</p> 
 * @author ctindal
 *
 */
public class DisplayStaticPage extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String url;
	
	public String execute() {
		
		try {

			if (name != null && !name.endsWith(".html")) {
				name = name + ".html";
			}
			else {
				name = "home.html";
			}
			
			url = Constants.PROJECT_BASE_URL + URLDecoder.decode(name, "UTF-8");
                        //System.out.println("Static: "+url.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return SUCCESS;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

}