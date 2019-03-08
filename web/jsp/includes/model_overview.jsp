<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <table width="100%" cellspacing="6" cellpadding="0">
    <tr>
        <td class="item itemLabel">Version</td>
        <td class="item itemContent">
          <table cellpadding="0" cellspacing="0" width="100%">
            <tr>
              <td><s:property value="modelData.version" /><span class="ghosttext"> of <s:property value="modelData.maxVersion" /></span></td>
              <s:if test="modelData.versions.size() > 1">
              <td>
                <s:form theme="simple" namespace="/models" action="model">
                  <span class="itemLabel">View version:</span>
                  <s:select onchange="submit()" name="version" list="modelData.versions" />
                  <s:hidden name="accession" value="%{modelData.accession}" />
                </s:form>
              </td>
              </s:if>
            </tr>
          </table>
        
        </td>
      </tr>
      <s:if test="modelData.versionComment != null && modelData.versionComment != ''">
        <tr>
        <td class="item itemLabel">Version Comments</td>
        <td class="item itemContent"><s:property escapeHtml="false" value="modelData.versionComment" />&nbsp;</td>
      </tr>
      </s:if>
      <tr>
        <td class="item itemLabel">Model Format</td>
        <td class="item itemContent"><s:property value="modelData.format" /></td>
      </tr>
      <tr>
        <td class="item itemLabel">Description</td>
        <td class="item itemContent"><s:property escapeHtml="false" value="modelData.description" />&nbsp;</td>
      </tr>
      <s:iterator value="modelData.attributes">
      <tr>
        <td class="item itemLabel"><s:property value="name" /></td>
        <td class="item itemContent"><s:property escapeHtml="false" value="value" /></td>
      </tr>
      </s:iterator>
      <tr>
        <td class="item itemLabel">Contact/Model Admin</td>
        <td class="item itemContent">
          <s:property value="modelData.owner.given" /> <s:property value="modelData.owner.family" />,
          <s:property value="modelData.owner.organisation" />,
          <a class="data" href="mailto:<s:property value="modelData.owner.email" />"><s:property value="modelData.owner.email" /></a>
          </td>
      </tr>
      <tr>
        <td class="item itemLabel">Submitted By</td>
        <td class="item itemContent">
          <s:property value="modelData.submitter.given" /> <s:property value="modelData.submitter.family" />,
          <s:property value="modelData.submitter.organisation" />,
          <a class="data" href="mailto:<s:property value="modelData.submitter.email" />"><s:property value="modelData.submitter.email" /></a>
          </td>
      </tr>
      <tr>
        <td class="item itemLabel">Submission Date</td>
        <td class="item itemContent"><s:property value="modelData.submissionDate" /></td>
      </tr>
      <tr>
        <td class="item itemLabel">Images</td>
        <td class="item itemContent">
          <table width="100%">
          <s:iterator value="modelData.images" var="item">
            <tr>
              <td class="thumbnail-content">
                <a class="data" href="<s:property value="#item[0]" />" target="new"><img src="<s:property value="#item[1]" />" alt="thumbnail" /></a>
                <%-- <a class="data" href="<s:property value="dataFileFileName" />" target="new"><s:property value="description" /> (image opens in new window)</a>--%>
              </td>
              <td class="text-top"><s:property value="#item[2]"/></td>
            </tr>
          </s:iterator>
          </table>
        </td>
      </tr>
      <tr>
        <td class="item itemLabel">Supplementary Data Files</td>
        <td class="item itemContent">
          <table>
          <s:iterator value="modelData.supplementaryFiles" var="item">
            <tr>
              <td>
                <a class="data" href="<s:property value="#item[0]" />" target="new"><s:property value="#item[1]" /></a>
              </td>
            </tr>
          </s:iterator>
          </table>
        </td>
      </tr>
      <tr>
        <td class="item itemLabel">Model Files</td>
        <td class="item itemContent">
            <a target="_new" href="<s:url action="download"><s:param name="accession" value="modelData.accession" /><s:param name="version" value="modelData.version" /><s:param name="type">ORIGINAL</s:param></s:url>">original file</a>, 
            <a target="_new" href="<s:url action="download"><s:param name="accession" value="modelData.accession" /><s:param name="version" value="modelData.version" /><s:param name="type">SIMPLIFIED</s:param></s:url>">simplified file</a>
            <small>(use simplified if your software cannot read the file, e.g. Sloppy Cell)</small>
        </td>
      </tr>
      <%--<s:if test="(#session.user.oid != null && #session.user.oid == modelData.owner.oid && #session.user.supervisesUsers.size() > 1) || (#session.user.oid != null && #session.user.oid == modelData.owner.supervisor.oid)" >--%>
      <s:if test="(#session.user.oid != null && #session.user.oid == modelData.owner.oid ) || (#session.user.oid != null && #session.user.oid == modelData.owner.supervisor.oid)" >
      <tr>
        <td class="item itemLabel">Permission</td>
        <td class="item itemContent">
        <s:form theme="simple" namespace="/models" action="model"  method="post">
        <s:hidden name="accession" value="%{modelData.accession}" />
        <s:hidden name="updategrouppermission" value="updategrouppermission" />
         <table>
         	<tr>
         		<th align="left">Group</th><th align="center">Read</th><th align="center">Read/Write</th><th align="center">No access</th>
         	</tr>
            <s:iterator value="modelData.groupAttributes" var="item">
            <tr>
              <td>
                <s:property value="#item.name" />
              </td>
              <td align="center"><input type="radio" name="<s:property value="#item.name"/>" value="access" <s:if test="#item.access == true"> checked="<s:property value="#item.access"/>" </s:if> /></td>
              <td align="center"><input type="radio" name="<s:property value="#item.name"/>" value="edit" <s:if test="#item.edit == true"> checked="<s:property value="#item.edit"/>" </s:if>/></td>
              <td align="center"><input type="radio" name="<s:property value="#item.name"/>" value="noaccess" <s:if test="#item.access == false && #item.edit == false"> checked="<s:property value="#item.access"/>" </s:if>/></td>
            </tr>
          </s:iterator>
          <tr>
          <td colspan="4"><s:submit value="Update Model Permission"/></td>
          </tr>
          </table>
          </s:form>
        </td>
      </tr>
      </s:if>
    </table>