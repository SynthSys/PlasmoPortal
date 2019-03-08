package uk.ac.ed.plasmo.interceptor;

import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Custom interceptor used to prevent browsers from caching password protected
 * information. The interceptor tells the browser not to cache any of the data
 * displayed. See struts.xml file to find out which pages/packages the interceptor
 * is applied to.
 * @author ctindal
 *
 */
public class CacheHeadersInterceptor extends AbstractInterceptor implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		final ActionContext context = invocation.getInvocationContext();
		
		final HttpServletResponse response = (HttpServletResponse) context.get(StrutsStatics.HTTP_RESPONSE);
		
		if(response != null) {
			response.setHeader("Cache-control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "-1");
		}
		
		return invocation.invoke();
	}

}
