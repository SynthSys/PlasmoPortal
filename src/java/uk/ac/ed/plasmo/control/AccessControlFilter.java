package uk.ac.ed.plasmo.control;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * The {@code AccessControlFilter} class is responsible for controlling access to
 * specified urls within the web application. See the <b>web.xml</b> file
 * of this application to see which directories the filter controls access to. The class
 * controls access to specified urls by checking to see if a user is logged in to the application
 * (i.e. it checks to see if a user's credentials can be found in the current session).
 * @author ctindal
 *
 */
public class AccessControlFilter implements Filter {
	
	private String loginPage; //the page the user is taken to if not logged in
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;
		
		if(!isAuthenticated(httpReq)) {
			String forwardURI = getForwardURI(httpReq);
			
			//Forward to the login page and stop further processing
			httpResp.sendRedirect(httpReq.getContextPath()+forwardURI);
			return;
		}
		
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		loginPage = "/login/user_login.shtml";
		
	}
	
	/**
	 * checks to see if the user is logged in to the application
	 * @param request
	 * @return
	 */
	private boolean isAuthenticated(HttpServletRequest request) {
		boolean isAuthenticated = false;
		
		HttpSession session = request.getSession();
		if(session.getAttribute("user") != null) {
			isAuthenticated = true;
		}
		
		return isAuthenticated;
	}
	
	/**
	 * processes the url the user has to be redirected to if 
	 * not logged in
	 * @param request
	 * @return
	 */
	private String getForwardURI(HttpServletRequest request){
		StringBuilder uri = new StringBuilder(loginPage);
		uri.append("?errorMsg=Please+log+in+first.");
		return uri.toString();
	}

}
