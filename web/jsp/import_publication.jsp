<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3>Import Publication into: <s:property value="model.name" /></h3>


  <s:actionmessage/>
  
    
      <div class="panel">
      <div>&nbsp;</div>
        <p>Please enter the PubMedID of publication associated with the model</p>
        
        <s:iterator var="err" value="getActionErrors()">
            <s:property value="#err" /><br/>
        </s:iterator>

            
      	<div id="publicationsInputsContainer">
            <s:form method="post" theme="simple" action="import_publication" name="mainForm">
            <s:hidden name="accession" />
            
			<table>
				<tbody>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="pubMedId" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>PubMedId:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="pubMedId" size="50" /></td>
					</tr>
				</tbody>
			</table>
			
            
                <div><s:submit value="Import" />
                </div>
            </s:form>
    	</div>
      </div>

  
      <div>
        <a href="<s:url namespace="/edit" action="publications"><s:param name="accession" value="%{accession}" /></s:url>">Return</a>   
      </div>
<s:include value="includes/footer.jsp" />