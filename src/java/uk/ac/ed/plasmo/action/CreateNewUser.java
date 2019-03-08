package uk.ac.ed.plasmo.action;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.struts2.interceptor.ServletRequestAware;

import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.Constants;
import uk.ac.ed.plasmo.utility.MailUtility;
import uk.ac.ed.plasmo.assembler.UserAssembler;

import com.opensymphony.xwork2.ActionSupport;

public class CreateNewUser extends ActionSupport implements ServletRequestAware {
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	private String recaptcha_response_field;
	private String recaptcha_challenge_field;

	private HttpServletRequest req;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getRecaptcha_response_field() {
		return recaptcha_response_field;
	}
	public void setRecaptcha_response_field(String recaptcha_response_field) {
		this.recaptcha_response_field = recaptcha_response_field;
	}
	public String getRecaptcha_challenge_field() {
		return recaptcha_challenge_field;
	}
	public void setRecaptcha_challenge_field(String recaptcha_challenge_field) {
		this.recaptcha_challenge_field = recaptcha_challenge_field;
	}
	
        @Override
        @SuppressWarnings("unchecked")
	public String execute() {

		UserAssembler assembler = new UserAssembler();
		
		HashMap<Boolean, Object> result = assembler.createNewUser(user);
		
		if(result == null) {
			addActionError("An unexpected error occurred. Please try again");
			return INPUT;
		}
		
		if(result.containsKey(true)) {
			String uuid = (String) result.get(true);
			
			StringBuilder content = new StringBuilder("Dear "+ user.getGiven() + ", \r\n\r\n");
			
			content.append("Thank you for registering with PlaSMo. To confirm your email address and activate your " +
					"account please click the following link:\r\n\r\n");
			
			content.append(Constants.PROJECT_BASE_URL+"plasmo/login/activate.shtml?activationId="+ uuid +"\r\n\r\n");
			
			content.append("PlaSMo - Plant Systems Biology Modelling\r\n\r\n");
			
			content.append(Constants.PROJECT_BASE_URL);
			
			String [] to = {Constants.ADMIN_EMAIL};
			
			try {
				MailUtility.send(Constants.ADMIN_EMAIL, to, null, "Confirmation of Request for New User Account Creation in the PlaSMo portal", content.toString(), null);
				
			} catch (Exception e) {
				e.printStackTrace(System.out);
				throw new RuntimeException();
			}
			
			
			return SUCCESS;
		}
		else {
			ArrayList<String> errors = (ArrayList<String>) result.get(false);
			for(int i=0; i <  errors.size(); i++) {
				addActionError(errors.get(i));
			}
			return INPUT;
		}
		
		
	}
	
        @Override
	public void validate() {
		
		String remoteAddress = req.getRemoteAddr();
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		
		reCaptcha.setPrivateKey(Constants.RECAPTCHA_PRIVATE_KEY);
		
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddress, recaptcha_challenge_field, recaptcha_response_field);
		
		if(!reCaptchaResponse.isValid()) {
			System.out.println("invalid recaptcha");
			addFieldError("captcha", "Incorrect entry. Please try again.");
		}
		//else {
		//	System.out.println("Valid recaptcha");
		//}
	}
	
	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.req = req;
	}
	
}
