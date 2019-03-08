<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />

<s:actionerror />
<div>
<s:form action="process_login" theme="simple">
<p class="errorMessage"><s:property value="#parameters.errorMsg"/></p>
<s:fielderror />
<div>User name: <s:textfield name="userName" /></div>
<div>Password: <s:password name="password" /></div>
<div><s:submit value="Login" /></div>
</s:form>
</div>
<div>
  <%-- <p>Need an account? <a href="<s:url namespace="/login" action="new_user" />">Register here!</a></p>--%>
  <p><a href="<s:url namespace="/login" action="forgot_password" />">Forgot your password?</a></p>
</div>


<s:include value="includes/footer.jsp" />