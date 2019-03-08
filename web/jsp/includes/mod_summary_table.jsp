<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<table width="100%" cellspacing="2" cellpadding="2">
  <s:iterator value="table.data" var="row" status="status">
    
    <tr>
      <td class="rowData">
        <p class="wide_margin">
          <a class="databold" href="<s:url namespace="/models" action="model"><s:param name="accession" value="#row[0]" /></s:url>"><s:property value="#row[1]"/></a> 
        </p>
        <p class="wide_margin"><span class="itemLabel">Format:</span> <s:property value="#row[2]"/></p>
        <p class="wide_margin"><span class="itemLabel">Contact/Model Admin:</span> <a href="mailto:<s:property value="#row[5]"/>"><s:property value="#row[4]"/></a> </p>
        <p class="wide_margin"><span class="itemLabel">Submission Date:</span> <s:property value="#row[6]"/></p>
        <s:if test="#row[9] == 0"><p class="wide_margin"><span class="itemLabel">Access:</span> Restricted <img src="${pageContext.request.contextPath}/images/padlock.gif" width="18px" height="18px" /></p></s:if>
        <div class="desc">
          <s:if test="#row[10] == null || #row[10] == ''"><span class="ghosttext">No description</span></s:if>
          <s:else><s:property escapeHtml="false" value="#row[10]"/></s:else>
        </div>
      </td>
      <td class="rowData tenth"><p class="nowrap wide_margin"><a target="_new" href="<s:url namespace="/models" action="download"><s:param name="accession" value="#row[0]" /></s:url>">Download Model</a></p></td>
    </tr>
  </s:iterator>
</table>