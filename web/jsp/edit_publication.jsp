<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3>Edit Publication in: <s:property value="model.name" /></h3>


  <s:actionmessage/>
  
    
      <div class="panel">
      <div>&nbsp;</div>
        <p>Please enter the details of reference publication associated with the model</p>
        
        <s:iterator var="err" value="getFieldErrors().values()">
            <s:property value="#err" /><br/>
        </s:iterator>

    	<script type="text/javascript">
            
            function submitAction(act_name) {
               document.mainForm.action = act_name;
               document.mainForm.submit();                      
            }
    	</script>
            
      	<div id="publicationsInputsContainer">
            <s:form method="post" theme="simple" action="submit_publication_edit" name="mainForm">
            <s:hidden name="publication.referenceType" />
            <s:hidden name="publication.pubOID" />
            <s:hidden name="accession" />
            
		<s:if test="publication.referenceType == 'Journal Article'">
			<table>
				<tbody>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.authors" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Authors:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="publication.authors" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.year" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Year:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="publication.year" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.title" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Title:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="publication.title" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param
								value="publication.periodicalName" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Journal:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="publication.periodicalName"
							size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.url" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>URL:</td>
						<td><s:textfield
							name="publication.url" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.abstract" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Abstract:</td>
						<td><s:textarea
							name="publication.abstract" cols="70"
							rows="5" /></td>
					</tr>
				</tbody>
			</table>
			
		</s:if> 
		<s:elseif test="publication.referenceType == 'Book'">
			<table>
				<tbody>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.title" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Book Title:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="publication.title" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.year" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Year:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="publication.year" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.authors" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Book Authors:</td>
						<td><s:textfield
							name="publication.authors" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.secondaryTitle" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Chapter Title:</td>
						<td><s:textfield
							name="publication.secondaryTitle"
							size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.secondaryAuthors" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Chapter Authors:</td>
						<td><s:textfield
							name="publication.secondaryAuthors"
							size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.pages" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Pages:</td>
						<td><s:textfield
							name="publication.pages" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.publisher" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Publisher:</td>
						<td><s:textfield
							name="publication.publisher" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.isbn" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>ISBN:</td>
						<td><s:textfield
							name="publication.isbn" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="publication.url" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>URL:</td>
						<td><s:textfield
							name="publication.url" size="70" /></td>
					</tr>
				</tbody>
			</table>
			
		</s:elseif>
            
                <div><s:submit value="Save Changes" />
                    <button value="Remove" name="Remove" onclick="JavaScript:submitAction('RemovePublication.shtml');" >Remove</button>
                </div>
            </s:form>
    	</div>
      </div>

  
      <div>
        <a href="<s:url namespace="/edit" action="publications"><s:param name="accession" value="%{accession}" /></s:url>">Return</a>   
      </div>
<s:include value="includes/footer.jsp" />
