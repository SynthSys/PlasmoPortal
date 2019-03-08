package uk.ac.ed.plasmo.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import uk.ac.ed.plasmo.assembler.UserAssembler;
import uk.ac.ed.plasmo.entity.User;

import com.opensymphony.xwork2.ActionSupport;
/**
 * <p>Action class that interacts with the data access layer in order to
 * retrieve a user's details using a unique id emailed to them when they
 * are unable to remember their passwords. If the user details are retrieved using this id, 
 * the user's details are stored as part of their current session and they are logged in to the application.</p> 
 * @author ctindal
 *
 */
public class ResetPassword extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private Map<String, Object> sessionMap;

        String msg;
        
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String execute() {
		
		UserAssembler assembler = new UserAssembler();
		
		User user = assembler.getUser(id);
		
		if(user == null) {
                    addActionError("Invalid password reset id");
                    setMsg("Invalid password reset id");
			return INPUT;
		}
		else {
			sessionMap.put("user", user);
			return SUCCESS;
		}
	}
	
	@Override
	public void setSession(Map<String, Object> map) {
		this.sessionMap = map;
	}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
