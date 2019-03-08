<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />

<s:set var="eurl" value="url" scope="request" />


<c:catch var="exception">
<c:import url="${eurl}" />
</c:catch>

<c:if test="${exception != null}">
  The page you requested could not be found.
</c:if>


<s:include value="includes/footer.jsp" />