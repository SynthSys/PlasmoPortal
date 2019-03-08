<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />

<s:actionerror />
<div>

    <h2>DB2 to MySQL Models migration</h2>
    <p><s:property value="msg"/></p>
    <p>DB2 Models: <s:property value="db2Size"/></p>
    <p>MySQL Models: <s:property value="mysqlSize"/></p>
    <p>Migrated: <s:property value="migrated"/></p>
</div>


<s:include value="includes/footer.jsp" />