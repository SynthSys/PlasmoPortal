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
 * Filter that ensures that direct calling of jsp files is not permitted. Filitering is based on the request url and its end of name in 
 * the form of .jsp
 * @author tzielins
 *
 */
public class JspControlFilter implements Filter {

	protected Logger accessLog;

	public JspControlFilter()
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

                if (isDirectJSP(httpReq)) {
                    httpResp.sendError(403,"Direct access to files is not permitted");
                    //httpResp
                    accessLog.error("Ignoring direct access to jps: "+httpReq.getRequestURL().toString()+", from: "+httpReq.getRemoteAddr());
                    return;
                }
                chain.doFilter(request, response);

	}



	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}




    private boolean isDirectJSP(HttpServletRequest httpReq) {
        
        String url = httpReq.getRequestURL().toString();
        if (url == null) return false;
        //System.out.println("R:" +url);
        if (url.endsWith(".jsp")) return true;
        return false;
    }


}
