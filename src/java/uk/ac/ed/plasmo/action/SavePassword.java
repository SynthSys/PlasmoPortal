package uk.ac.ed.plasmo.action;

import java.util.Map;

import uk.ac.ed.plasmo.assembler.UserAssembler;
import uk.ac.ed.plasmo.entity.User;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action class that interacts with the data access layer in order to
 * save a user's change of password to the database
 * @author ctindal
 *
 */
public class SavePassword extends ActionSupport {

	private static final long serialVersionUID = 1L;
	String msg;
        
	private User user;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String execute() {
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		User sessionUser = (User) session.get("user");
		
		if(sessionUser == null) {
			return INPUT;
		}
		
		
		user.setUserName(sessionUser.getUserName());
		
		UserAssembler assembler = new UserAssembler();
		
		String savePasswordMsg = assembler.savePassword(user);
		
		if(savePasswordMsg == null) {
                    setMsg("password successfully changed");
			return SUCCESS;
		}
		else {
			addActionError(savePasswordMsg);
			return INPUT;
		}

	}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
