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

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;


/**
 * Filter that ensures that redirect bug in Struts is not exploited. It checks the query string for the presence
 * of redirect key word or action phrase.
 * @author tzielins
 *
 */
public class RedirectControlFilter implements Filter {

	protected Logger accessLog;

	public RedirectControlFilter()
	{
		super();
		accessLog = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public void destroy() {

	}

        


	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;

                 if (isRedirectQuery(httpReq)) {
                    httpResp.sendError(403,"Direct action calling is not permitted");
                    //httpResp
                    accessLog.error("Ignoring direct action calling: "+httpReq.getRequestURL().toString()+":"+httpReq.getQueryString()+", from: "+httpReq.getRemoteAddr());
                    return;
                }
                chain.doFilter(request, response);

	}



	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}




    private boolean isRedirectQuery(HttpServletRequest httpReq) {
        
        String url = httpReq.getQueryString();
        if (url == null) return false;
        url = url.toLowerCase();
        //System.out.println("Q:" +url);
        if (url.contains("redirect")) return true;
        //if (url.contains("redirectaction")) return true;
        if (url.contains("action:")) return true;
        if (url.contains("action%3A")) return true;
        if (url.contains("?action")) return true;
        if (url.contains("xwork2")) return true;
        if (url.contains("processbuild")) return true;
        if (url.contains("java.")) return true;
        return false;
    }


}
