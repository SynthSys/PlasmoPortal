package uk.ac.ed.plasmo.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import uk.ac.ed.plasmo.assembler.UserAssembler;
import uk.ac.ed.plasmo.entity.User;

import com.opensymphony.xwork2.ActionSupport;

public class ActivateUserAccount extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	
	private String activationId;
	private User user;
	
	private Map<String, Object> sessionMap;
	
	public String getActivationId() {
		return activationId;
	}
	public void setActivationId(String activationId) {
		this.activationId = activationId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public void setSession(Map<String, Object> map) {
		this.sessionMap = map;
	}
	
	public String execute() {
		UserAssembler assembler = new UserAssembler();
		
		user = assembler.activateUser(activationId);
		
		if(user == null) {
			addActionError("Invalid activation id.");
			return INPUT;
		}
		else {
			addActionMessage("Account activation successful!");
			sessionMap.put("user", user);
			return SUCCESS;
		}
	}

}
