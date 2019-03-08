package uk.ac.ed.plasmo.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import uk.ac.ed.plasmo.assembler.SecurityAssembler;
import uk.ac.ed.plasmo.entity.User;

import com.opensymphony.xwork2.ActionSupport;
/**
 * <p>Action class that interacts with the data access layer in order to
 * authenticate a user when they try to login to the portal. If successful, 
 * the user's details are stored as part of their current session</p> 
 * @author ctindal
 *
 */
public class LoginAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	
	private User user;
	
	private Map<String, Object> sessionMap;
	
	public String execute() {
		
		SecurityAssembler assembler = new SecurityAssembler();
		
		user = assembler.getUser(userName, password);
		
		if(user == null) {
			addActionError("Invalid user name or password. Please try again.");
			return INPUT;
		}
		else {
			sessionMap.put("user", user);
			return SUCCESS;
		}
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
	}

	@Override
	public void setSession(Map<String, Object> map) {
		this.sessionMap = map;
	}

}
