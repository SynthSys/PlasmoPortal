package uk.ac.ed.plasmo.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
/**
 * <p>Action class that removes a user's details from the current session, thus logging
 * them out of the portal.</p> 
 * @author ctindal
 *
 */
public class LogoutAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionMap;
	
	public String execute() {
		sessionMap.remove("user");
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> map) {
		this.sessionMap = map;
	}

}