<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    

<table width="100%" cellspacing="6" cellpadding="0">
<s:iterator value="modelData.comments" var="item" status="stat">
  <tr>
    <td class="item">
      <p><s:property value="#stat.index + 1" />. Comment by <a href="mailto:<s:property value="#item[4]" />"><s:property value="#item[3]" /></a> on <s:property value="#item[1]" /></p>
      <div class="desc"><s:property escapeHtml="false" value="#item[0]" /></div>
    </td>
  </tr>
</s:iterator>
</table>