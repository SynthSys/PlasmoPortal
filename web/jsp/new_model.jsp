<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
  <h3>Submit a New Model</h3>
  <s:if test="formats == null">
    The are currently no model formats in the portal. Please <a href="<s:url namespace="/new" action="format" />">add a new model format</a> before adding new models. 
  </s:if>
  <s:else>
    <s:if test="#session.modelSubmission == null">
    <s:form method="post" theme="simple" action="submit_model" enctype="multipart/form-data">
    <s:actionerror/>
    <s:if test="!fieldErrors['validXML'].isEmpty">
      <span class="errorMessage">
        The file you have submitted is not valid. 
        Please correct the following errors before submitting the file:
      </span>
      <div>
      <ul>
      <s:iterator value="fieldErrors['validXML']">
        <li><span class="errorMessage"><s:property/></span></li>
      </s:iterator>
      </ul>
      </div>
    </s:if>
      <p>Model format: <s:select name="format" list="formats" /> <%--<a href="<s:url namespace="/new" action="format" />">Add a New Model Format</a>--%></p>
      <p>Upload Model File: <s:file name="model" size="55"/><s:submit value="Upload" /></p>
    </s:form>
    </s:if>
  
  
  <s:else>
  <s:actionmessage/>
  <div id="submissionBox">
    <script type="text/javascript">
jQuery().ready(function() {
	jQuery(".panels").toggleGroup({
		header: "h4",
		openTabs: "all"/*,
		toggleIcons: {
			closedIcon : "../images/plus.png",
			openIcon: "../images/minus.png",
			iconClass:"icon",
			holderClass: "icon_holder"
		}*/
	});

	jQuery('#submissionDescription').ckeditor(
    	function() {},
    	{
    		toolbar: 'Basic',
    		height:180, 
    		width:550,
    		uiColor: '#CBF5CB',
    		resize_enabled: false
    	} 
    );
	
	jQuery().publicationInput({
		entriesIdx: <s:if test="submission.publications != null"><s:property value="submission.publications.size()" /></s:if><s:else>0</s:else>
	});
	jQuery().dataFileInput({
		entriesIdx: <s:property value="submission.supplementaryFiles.size()" />
	});
	jQuery().imageFileInput({
		entriesIdx: <s:property value="submission.images.size()" />
	});
	jQuery().newAttributeInput({
		entriesIdx: <s:if test="submission.attributes != null"><s:property value="submission.attributes.size()" /></s:if><s:else>0</s:else>
	});
	jQuery('#unpublished').click(function(e) {
		jQuery('#publicationsInputsContainer').toggle();
	});

	if(jQuery('#unpublished').is(':checked')) {
		jQuery('#publicationsInputsContainer').css('display','none');
	}
	else {
		jQuery('#publicationsInputsContainer').css('display','block');
	}
			
	jQuery(".toolTip").tooltip({
		showURL: false,
		showBody: "::"
	});

	
	if(<s:property value="submission.private"/>) {
		jQuery('#userGroupSelect').css("display","block");
	}
	else {
		jQuery('#userGroupSelect').css("display","none");
	}	
		
	jQuery('#private').click(
    	function() {
    		$('#userGroupSelect').slideToggle('normal');
    		if(!this.checked) {
    			$('#userGroupSelect').find('input:checkbox').each(function() {
        			$(this).attr('checked', false);
        		});
    		}
    	}
    );
	
});
</script>
    <p>Please fill in the additional fields about the model under each tab. It is mandatory to fill in fields marked "<span class="errorMessage">*</span>".</p>
    <%-- <ul class="tabs">
      <li class="off"><a>Model Summary</a></li>
      <li class="off"><a>Publications</a></li>
    </ul>--%>
    <s:form theme="simple" action="submit_model_data" method="post" enctype="multipart/form-data">
    <div class="panels">
    <h4>Overview</h4>
    <div class="panel">
    
      <s:include value="includes/edit_model_core.jsp"></s:include>
      
      <div class="subcontainer">
      <span style="font-weight:bold">Add Images:</span>
      <span> 
        Please upload any images you want to associate with the uploaded model.
      </span>
      <s:iterator value="submission.images" status="stat">
        <div id="<s:property value="#stat.index" />_imageFileInputContainer" class="subcontainer">
          <div id="<s:property value="#stat.index" />_imageFileInputFieldsContainer" >
            <s:fielderror>
              <s:param value="%{'submission.images['+#stat.index+'].dataFile'}" />
              <s:param value="%{'submission.images['+#stat.index+'].description'}"/>
            </s:fielderror>
            <p>Image/Screen Shot of the Model:
            <img src="../images/tip.gif" class="toolTip" title="Image/Screen shot of the Model::Optional. Upload any images associated with the model. E.g. graphical representation of the model/submodels within the model, simulation results" /> 
            <s:file name="%{'submission.images['+#stat.index+'].dataFile'}" size="55" /></p>
            <p>Description of screen shot:
            <img src="../images/tip.gif" class="toolTip" title="Description of Screen Shot::A description of the image to be submitted" /> 
            <s:textfield name="%{'submission.images['+#stat.index+'].description'}" size="70" /></p>
          </div>
        </div>
      </s:iterator>
      
      <div id="imgButtonContainer">
        <s:submit id="addImg" value="Add Additional Image/Screen shot" type="button" />
        <s:submit id="removeImg" value="Remove Last Image/Screen shot" type="button" />
      </div>
      </div>
      <div class="subcontainer">
      <span style="font-weight:bold">Add Supplementary Data Files:</span>
      <span>
        Please upload any data files to accompany the uploaded model.  
      </span>
      <s:iterator value="submission.supplementaryFiles" status="stat">
      <div id="<s:property value="#stat.index" />_dataFileInputContainer" class="subcontainer">
        <div id="<s:property value="#stat.index" />_dataFileInputFieldsContainer" >
          <s:fielderror>
            <s:param value="%{'submission.supplementaryFiles['+#stat.index+'].dataFile'}" />
            <s:param value="%{'submission.supplementaryFiles['+#stat.index+'].description'}"/>
          </s:fielderror>
          <p>Supplementary Data File:
          <img src="../images/tip.gif" class="toolTip" title="Supplementary Data File::Optional. Associated data files may contain values of quantitative attributes contained in the model. These values may be used as driving variables to illustrate model usage" /> 
          <s:file name="%{'submission.supplementaryFiles['+#stat.index+'].dataFile'}" size="55" /></p>
          <p>Description of data file :
          <img src="../images/tip.gif" class="toolTip" title="Description of Data File::A description of the file to be submitted" /> 
          <s:textfield name="%{'submission.supplementaryFiles['+#stat.index+'].description'}" size="70" /></p>
        </div>
      </div>
    </s:iterator>
      <div id="dfButtonContainer">
        <s:submit id="addDF" value="Add Additional Data File" type="button" />
        <s:submit id="removeDF" value="Remove Last Data File Entry" type="button" />
      </div>
      </div>
    </div>
    <h4>Publications</h4>
      <div class="panel">
      <div>&nbsp;</div>
        <p>Please enter the details of any reference publications associated with the model OR check the box below if there are no publications associated with the model.</p>
        
        <s:fielderror>
      	  <s:param>allPublications</s:param>
      	</s:fielderror>
      	<div id="publicationsInputsContainer">
      	<s:iterator value="submission.publications" status="stat">
		<div id="<s:property value="#stat.index" />_publicationInputContainer" class="subcontainer">

		<div id="<s:property value="#stat.index" />_inputFieldsContainer">
		<s:if test="referenceType == 'Journal Article'">
			<table>
				<tbody>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].authors'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Authors:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].authors'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].year'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Year:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].year'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].title'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Title:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].title'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param
								value="%{'submission.publications['+#stat.index+'].periodicalName'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Journal:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].periodicalName'}"
							size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].url'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>URL:</td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].url'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].abstract'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Abstract:</td>
						<td><s:textarea
							name="%{'submission.publications['+#stat.index+'].abstract'}" cols="70"
							rows="5" /></td>
					</tr>
				</tbody>
			</table>
			<s:hidden name="%{'submission.publications['+#stat.index+'].referenceType'}" />
		</s:if> 
		<s:elseif test="referenceType == 'Book'">
			<table>
				<tbody>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].title'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Book Title:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].title'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].year'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Year:<span class="errorMessage">*</span></td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].year'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].authors'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Book Authors:</td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].authors'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].secondaryTitle'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Chapter Title:</td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].secondaryTitle'}"
							size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].secondaryAuthors'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Chapter Authors:</td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].secondaryAuthors'}"
							size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].pages'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Pages:</td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].pages'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].publisher'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>Publisher:</td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].publisher'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].isbn'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>ISBN:</td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].isbn'}" size="70" /></td>
					</tr>
					<tr>
						<td colspan="2"><s:fielderror>
							<s:param value="%{'submission.publications['+#stat.index+'].url'}" />
						</s:fielderror></td>
					</tr>
					<tr>
						<td>URL:</td>
						<td><s:textfield
							name="%{'submission.publications['+#stat.index+'].url'}" size="70" /></td>
					</tr>
				</tbody>
			</table>
			<s:hidden name="%{'submission.publications['+#stat.index+'].referenceType'}" />
		</s:elseif>
		</div>
		</div>
	    </s:iterator>
	    <div id="pubButtonContainer">
      	  <s:submit id="addJournalRef" value="Add Journal Reference" type="button"/>
      	  <s:submit id="addBookRef" value="Add Textbook Reference" type="button" />
      	  <s:submit id="removePub" value="Remove Last Publication Entry" type="button" />
    	</div>
    	<p>OR</p>
    	</div>
    	<p><s:checkbox name="submission.unpublished" id="unpublished" /> There are no publications associated with the model</p>
      </div>
    <div style="padding-top:2px"><s:submit/></div>
    </div>
    </s:form>
  </div>
  </s:else>
  </s:else>
<s:include value="includes/footer.jsp" />