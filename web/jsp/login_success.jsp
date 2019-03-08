<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />

<s:actionerror />
<s:actionmessage/>
<div>
<p>
  You are logged on as 
  <s:property value="#session.user.given"/> 
  <s:property value="#session.user.family"/>, 
  user name 
  <s:property value="#session.user.userName"/>.
</p>
</div>

<s:include value="includes/footer.jsp" />