<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<span class="errorMessage"><s:property value="#parameters.msg"/></span>
<span class="errorMessage"><s:property value="msg"/></span>

    <h2 style="color:red;">Unauthorised action</h2>
    

<s:include value="includes/footer.jsp" />