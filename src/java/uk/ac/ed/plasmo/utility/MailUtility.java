package uk.ac.ed.plasmo.utility;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Utility class used to send email
 * @author ctindal
 *
 */
public final class MailUtility {
	
	/**
	 * send an email
	 * @param from who the email is from
	 * @param to list of who the email is to
	 * @param cc list who the email is to be cc'd to 
	 * @param subject the subject of the email
	 * @param content the content of the email
	 * @param attachments any file attachments to be sent with the email
	 * @throws Exception
	 */
	public static void send(String from, String [] to, String [] cc, String subject, String content, File [] attachments) throws Exception {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", Constants.MAIL_SERVER);
		Session session = Session.getDefaultInstance(props);
		Message message = new MimeMessage(session);
			
			try {
				message.setFrom(new InternetAddress(from));
				for(int i=0;i<to.length;i++){
					message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to[i]));
				}
				if(cc != null && cc.length > 0) {
					for(int i=0;i<cc.length;i++){
						message.addRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cc[i]));
					}
				}
				message.setSubject(subject);
				
				Multipart mp = new MimeMultipart();
				MimeBodyPart mimeTextContent = new MimeBodyPart();
				mimeTextContent.setText(content);
				mp.addBodyPart(mimeTextContent);
			    
			    if(attachments != null && attachments.length >0){
			    	for(int i=0;i<attachments.length;i++){
			    		File attachment = attachments[i];
			    		MimeBodyPart mbp = new MimeBodyPart();
			    		FileDataSource fds = new FileDataSource(attachment);
			    		mbp.setDataHandler(new DataHandler(fds));
			    		mbp.setFileName(fds.getName());
			    		mp.addBodyPart(mbp);
			    	}
			    }
			    
			    message.setContent(mp);
			    
			    Transport.send(message);
			    
			} catch (MessagingException e) {
				e.printStackTrace();
				throw e;
			}	
	}
}
