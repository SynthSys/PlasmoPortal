<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3>Forgot Password</h3>
<div class="panels">
<s:form theme="simple" action="reset_password_request" namespace="/login">
<s:actionmessage/>
<s:actionerror/>


<h4>Anti-spam test</h4>
<div class="panel">
<s:include value="includes/recaptcha.jsp" />
</div>


<h4>Email</h4>
<div class="panel">
  <s:fielderror>
    <s:param value="%{'email'}" />
  </s:fielderror>
  <p>Enter your registered email address: <s:textfield name="email" size="50" /> <s:submit /></p>


</div>


</s:form>
</div>
<s:include value="includes/footer.jsp" />