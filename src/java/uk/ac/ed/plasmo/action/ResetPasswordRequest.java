package uk.ac.ed.plasmo.action;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.struts2.interceptor.ServletRequestAware;

import uk.ac.ed.plasmo.assembler.UserAssembler;
import uk.ac.ed.plasmo.entity.User;
import uk.ac.ed.plasmo.utility.Constants;
import uk.ac.ed.plasmo.utility.MailUtility;

import com.opensymphony.xwork2.ActionSupport;
/**
 * <p>Action class used to send an email to user with instructions
 * on how to reset their password if they cannot remember it.</p>
 * <p>The user must enter their email address and correctly enter captcha
 * text to prevent spam.</p>
 * <p>This action class interacts with the data access layer in order to
 * retrieve an auto-generated unique id associated with a user 
 * (using the user's email address to identify the user). This id is 
 * sent as part of a link in an email to the user allowing them to login using
 * the unique id and ultimately change their password once logged in. </p> 
 * @author ctindal
 *
 */
public class ResetPasswordRequest extends ActionSupport implements ServletRequestAware {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String recaptcha_response_field;
	private String recaptcha_challenge_field;
	private HttpServletRequest req;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	
	public String execute() {
		
		UserAssembler assembler = new UserAssembler();
		
		User user = assembler.getUserPasswordResetId(email);
		
		if(user == null) {
			addActionError("There are no users in the database with the specified email address.");
			return INPUT;
		}
		else {
			addActionMessage("You have been sent an email with instructions on how to reset your password.");
			
			StringBuilder content = new StringBuilder("Dear "+ user.getGiven() + " "+ user.getFamily() + ", \r\n\r\n");
			
			content.append("A request has been submitted to reset your password in the PlaSMo portal.\r\n\r\n");
			
			content.append("Your username is: " +user.getUserName()+"\r\n\r\n");
			
			content.append("Please click on the link below so you can reset your password:\r\n\r\n");
			
			content.append(Constants.PROJECT_BASE_URL+"plasmo/login/reset_password.shtml?id="+user.getUuid()+"\r\n\r\n");
			
			content.append("PlaSMo - Plant Systems Biology Modelling\r\n\r\n");
			
			content.append(Constants.PROJECT_BASE_URL);
			
			String [] to = {user.getEmail()};
			
			try {
				MailUtility.send(Constants.ADMIN_EMAIL, to, null, "Reset Password request in the PlaSMo portal", content.toString(), null);
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			
			return SUCCESS;	
		}
	}
	
	public void validate() {
		String remoteAddress = req.getRemoteAddr();
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		
		reCaptcha.setPrivateKey(Constants.RECAPTCHA_PRIVATE_KEY);
		
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddress, recaptcha_challenge_field, recaptcha_response_field);
		
		if(!reCaptchaResponse.isValid()) {
			addFieldError("captcha", "Incorrect entry. Please try again.");
		}
	}
	
	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.req = req;
	}
	

}
