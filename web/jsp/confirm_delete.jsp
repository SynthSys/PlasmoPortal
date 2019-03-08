<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<span class="errorMessage"><s:property value="#parameters.msg"/></span>
<span class="errorMessage"><s:property value="msg"/></span>

    <h2>Confirm model removal</h2>
    <table>
        <tr><td>Model: </td><td><s:property value="modelName"/></td></tr>
        <tr><td>Description:</td><td><s:property value="modelDesc" escapeHtml="false"/></td></tr>
    </table>
        <s:form theme="simple" action="model" namespace="/delete">
            <s:hidden name="confirmed" value="true"/>
            <s:hidden name="accession"/>        
            <s:submit value="confirm"/>
        </s:form>
    
    

<s:include value="includes/footer.jsp" />