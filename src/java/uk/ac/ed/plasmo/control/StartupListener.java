package uk.ac.ed.plasmo.control;

import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
/**
 * performs activities required when the application is first started
 * (e.g. get the connection pool data required to connect to the database)
 * @author ctindal
 *
 */
public class StartupListener implements ServletContextListener {
	
	private static ServletContext servletContext;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
		
		try {
                    Context iniContext = new InitialContext();
                    Context envContext = (Context)iniContext.lookup("java:/comp/env");
                    DataSource dataSource = (DataSource)envContext.lookup("jdbc/plasmo");

                    servletContext.setAttribute("DBPOOL", dataSource);
		} catch (NamingException e) {
                    System.out.println("Could not get Data Source: "+e.getMessage());
                    e.printStackTrace();
                    throw new IllegalStateException("Missing data source info");
		}
		
		//set the recaptcha public key in application scope so jsp pages can access
		String recaptchaPublicKey = ResourceBundle.getBundle("configuration").getString("RECAPTCHA_PUBLIC_KEY");
		servletContext.setAttribute("RECAPTCHA_PUBLIC_KEY", recaptchaPublicKey);
	}
	
	public static ServletContext getServletContext() {
		return servletContext;
	}

}
