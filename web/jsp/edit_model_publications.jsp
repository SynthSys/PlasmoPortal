<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3>Edit Publications in <s:property value="submission.name" /></h3>


  <s:actionmessage/>
  
    <s:if test="submission.publications != null && submission.publications.size() > 0">
    <s:iterator value="submission.publications" status="status">
      <s:if test="referenceType == 'Journal Article'">
        <table class="subcontainer" width="100%" cellspacing="6" cellpadding="0">
          <tr>
            <td class="item itemLabel">Journal</td>
            <td class="item itemContent"><s:property value="periodicalName" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Title</td>
            <td class="item itemContent"><s:property value="title" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Year</td>
            <td class="item itemContent"><s:property value="year" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Authors</td>
            <td class="item itemContent"><s:property value="authors" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Abstract</td>
            <td class="item itemContent"><s:property value="abstract" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">URL</td>
            <td class="item itemContent"><s:a class="data" href="%{href}" ><s:property value="url" /></s:a>&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Operations</td>
            <td class="item itemContent">
                <s:url var="act" namespace="/edit" action="publication"><s:param name="accession" value="submission.accession" /><s:param name="pubOID" value="pubOID" /></s:url>
                <s:a href="%{act}">Edit entry</s:a>
            </td>
          </tr>
        </table>
      </s:if>
      <s:elseif test="referenceType == 'Book'">
        <table class="subcontainer" width="100%">
          <tr>
            <td class="item itemLabel">Book Title</td>
            <td class="item itemContent"><s:property value="title" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Year</td>
            <td class="item itemContent"><s:property value="year" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Book Authors</td>
            <td class="item itemContent"><s:property value="authors" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Chapter Title</td>
            <td class="item itemContent"><s:property value="secondaryTitle" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Chapter Authors</td>
            <td class="item itemContent"><s:property value="secondaryAuthors" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Pages</td>
            <td class="item itemContent"><s:property value="pages" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">URL</td>
            <td class="item itemContent"><s:a class="data" href="%{href}" ><s:property value="url" /></s:a>&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">ISBN</td>
            <td class="item itemContent"><s:property value="isbn" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Publisher</td>
            <td class="item itemContent"><s:property value="publisher" />&nbsp;</td>
          </tr>
          <tr>
            <td class="item itemLabel">Operations</td>
            <td class="item itemContent">
                <s:url var="act" namespace="/edit" action="publication"><s:param name="accession" value="submission.accession" /><s:param name="pubOID" value="pubOID" /></s:url>
                <s:a href="%{act}">Edit entry</s:a>
            </td>
          </tr>
        </table>
      </s:elseif>
    </s:iterator>
    </s:if>
    <s:else>
      <span>There are no publications associated with the model.</span>
    </s:else>  
      <div style="margin-top: 0.5em;">
        <a href="<s:url namespace="/edit" action="publication"><s:param name="accession" value="%{accession}" /><s:param name="refType" value="'Journal Article'" /></s:url>">Add new article</a>;    
        <a href="<s:url namespace="/edit" action="publication"><s:param name="accession" value="%{accession}" /><s:param name="refType" value="'Book'" /></s:url>">Add new book</a>;    
        <a href="<s:url namespace="/edit" action="import_publication"><s:param name="accession" value="%{accession}" /></s:url>">Import PubMed record</a>   
      </div>
      <div style="margin-top: 2em;">
        <a href="<s:url namespace="/models" action="model"><s:param name="accession" value="%{accession}" /></s:url>">Return</a>   
      </div>
<s:include value="includes/footer.jsp" />