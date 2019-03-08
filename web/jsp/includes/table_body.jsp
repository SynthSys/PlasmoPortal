<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<table class="defBg" width="100%" border="0">
  <%-- <tr>
    <td>Page <s:property value="pgNo" /> of <s:property value="noPgs" /></td>
    <s:if test="noPgs > 1">
    <td class="pgNav">
      <a href="<s:url action="display_models"><s:param name="pgNo" value="1" /></s:url>"><img src="/plasmo/images/first.gif" alt="first" title="First Page" /></a>
      <a href="<s:url action="display_models"><s:param name="pgNo" value="%{pgNo-1}" /></s:url>"><img src="/plasmo/images/previous.gif" title="Previous Page" alt="previous" /></a>
      <a href="<s:url action="display_models"><s:param name="pgNo" value="%{pgNo+1}" /></s:url>"><img src="/plasmo/images/next.gif" alt="next" title="Next Page" /></a>
      <a href="<s:url action="display_models"><s:param name="pgNo" value="%{noPgs}" /></s:url>"><img src="/plasmo/images/last.gif" alt="last" title="Last Page" /></a>
    </td>
    </s:if>
      <td>
      Display X entries per page
    </td>
  </tr>--%>
</table>

<table width="100%" border="0">
  <tr>
    <s:iterator value="table.headers">
      <td class="colHead"><s:property value="title"/></td>
    </s:iterator>
  </tr>
  

  <%-- <tr>
    <td class="colHead">PlaSMo ID</td>
    <td class="colHead">Name</td>
    <td class="colHead">Format</td>
    <td class="colHead">Submission Date</td>
    <td class="colHead">Submitted By</td>
  </tr>
  <s:iterator value="modelsMetadata" status="status">
  <tr class="<s:if test="#status.odd == true ">odd</s:if><s:else>even</s:else>">
    <td class="colData">
      <s:if test="format.startsWith('SBML')"><a class="data" href="<s:url action="model_sbml"><s:param name="modelId" value="%{id}" /></s:url>"><s:property value="id" /></a></s:if>
      <s:else><a class="data" href="<s:url action="model_simv3"><s:param name="modelId" value="%{id}" /></s:url>"><s:property value="id" /></a></s:else>
    </td>
    <td class="colData"><s:property value="modelName" /></td>
    <td class="colData"><s:property value="format" /></td>
    <td class="colData"><s:property value="submissionDate" /></td>
    <td class="colData"><a class="data" href="mailto:<s:property value="submitter.email" />" ><s:property value="submitter.given" /> <s:property value="submitter.family" /></a></td>
    
  </tr>
  </s:iterator>--%>
</table>