<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />

<span class="errorMessage"><s:property value="#parameters.msg"/></span>
<s:if test="#session.user != null"><h3>Models Submitted by <s:property value="#session.user.userName"/></h3></s:if>
<s:if test="table != null && table.numRows > 0">

<table class="defBg nowrap" width="100%" border="0">
  <tr>
    <td>Page <s:property value="pgNo" /> of <s:property value="noPgs" /></td>
    <s:if test="perPageOptions != null && perPageOptions.length > 0">
    <td>
      <s:form method="post" theme="simple" action="all_rpp">
      <table>
        <tr>
          <td>Display</td>
          <td><s:select onchange="submit()" name="rpp" list="perPageOptions" /></td>
          <td>entries per page</td>
        </tr>
      </table>
      </s:form>
    </td>
    </s:if>
    <s:if test="noPgs > 1">
    <td class="pgNav">
      <a href="<s:url action="user"><s:param name="pgNo" value="1" /></s:url>"><img src="/plasmo/images/first.gif" alt="first" title="First Page" /></a>
      <a href="<s:url action="user"><s:param name="pgNo" value="%{pgNo-1}" /></s:url>"><img src="/plasmo/images/previous.gif" title="Previous Page" alt="previous" /></a>
      <a href="<s:url action="user"><s:param name="pgNo" value="%{pgNo+1}" /></s:url>"><img src="/plasmo/images/next.gif" alt="next" title="Next Page" /></a>
      <a href="<s:url action="user"><s:param name="pgNo" value="%{noPgs}" /></s:url>"><img src="/plasmo/images/last.gif" alt="last" title="Last Page" /></a>
    </td>
    
    <td>
      <s:form method="post" theme="simple" action="all">
      Go to page:
      <s:textfield size="3" name="pgNo" /> 
      <s:submit value="Go" />
      </s:form>
    </td>
    </s:if>
    
  </tr>
</table>
<s:include value="includes/mod_summary_table.jsp" />
</s:if>
<s:else><s:property value="noDataMessage" /></s:else>
<s:include value="includes/footer.jsp" />