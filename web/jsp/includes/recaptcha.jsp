<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<p>Enter the words you see in the box below in order to verify you are a human visitor and to prevent automated spam attacks</p>
<s:fielderror>
  <s:param value="%{'captcha'}" />
</s:fielderror>
<script>
    var RecaptchaOptions = {
	    theme : 'clean'
	};
</script>
<script type="text/javascript" src="http://api.recaptcha.net/challenge?k=<s:property value="#application.RECAPTCHA_PUBLIC_KEY" />">
</script>
<noscript>
  <iframe src="http://api.recaptcha.net/noscript?k=<s:property value="#application.RECAPTCHA_PUBLIC_KEY" />" height="300" width="500" frameborder="0"></iframe>
  <br/>
  <textarea name="recaptcha_challenge_field" rows="3" cols="40"></textarea>
  <input type="hidden" name="recaptcha_response_field" value="manual_challenge" />
</noscript>