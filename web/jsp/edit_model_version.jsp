<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3><s:property value="submission.name" /> : Edit Version <s:property value="version" /></h3>
<div class="panels">
  <s:if test="formats == null">
    The are currently no model formats in the portal. Please add a new model format before adding new models. 
  </s:if>
  <s:else>
    
    <script type="text/javascript">
      jQuery().ready(function() {
          
		  jQuery().dataFileInput({
			  entriesIdx: <s:if test="submission.supplementaryFiles != null"><s:property value="submission.supplementaryFiles.size()" /></s:if><s:else>0</s:else>,
			  minEntries: 0
		  });
		  jQuery().imageFileInput({
			  entriesIdx: <s:if test="submission.images != null"><s:property value="submission.images.size()" /></s:if><s:else>0</s:else>,
			  minEntries: 0
		  });
		  jQuery(".toolTip").tooltip({
			  showURL: false,
			  showBody: "::"
		  });
		  jQuery('#submissionComments').ckeditor(
			  function() {},
			  {
				  toolbar: 'Basic',
				  height:180, 
				  width:550,
				  uiColor: '#CBF5CB',
				  resize_enabled: false
			  } 
		  );
	  });
	</script>
    <s:form method="post" theme="simple" action="submit_version_edit" enctype="multipart/form-data">
    <s:hidden name="accession" value="%{accession}" />
    <s:hidden name="version" value="%{version}" />
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
    <table>
    <tr>
      <td>Model format:</td> 
      <td><s:select name="submission.format" list="formats" /> <%-- <a href="<s:url namespace="/new" action="format" />">Add a New Model Format</a>--%></td>
    </tr>
    <tr>
      <td colspan="2" class="ghosttext" style="font-size:12px;">Leave 'Upload Model File' field blank if you do not want to replace the existing model file</td>
    </tr>
    <tr>
      <td>Upload Model File:
      <td> <s:file name="submission.model" size="55"/></td>
    </tr>
    <tr>
      <td colspan="2">
        <s:fielderror>
          <s:param value="%{'submission.comments'}" />
        </s:fielderror>
      </td>
    </tr>
    <tr>
      <td>Version Comments:
      <img src="../images/tip.gif" class="toolTip" title="Version Comments::Explanation of the differences between this version of the model and previous versions" alt="Info" /></td>
      <td><s:textarea cols="80" rows="6" id="submissionComments" name="submission.comments" /></td>
    </tr>
    </table>
    <div class="subcontainer">
      <%--<s:fielderror>
        <s:param>allImages</s:param>
      </s:fielderror>
      <span style="font-weight:bold">Add Images:</span>
      <span>
        Previous versions of this model already have associated images. 
        Please select the images you want to associate with this version of
        the model. New images for this version can also be uploaded. 
        Note: At least ONE image must be associated with this version.
      </span>
      <table cellpadding="4" width="100%">
        <s:iterator value="modelData.images">
        <tr>
          <td>
            <s:checkbox name="submission.imageIds" fieldValue="%{id}" />
            <a class="data" href="<s:property value="dataFileFileName" />" target="new"><s:property value="description" /></a>
          </td>
        </tr>
        </s:iterator>
      </table>--%>
      <span style="font-weight:bold">Add Images:</span>
      <span> 
        Please upload any new images you want to associate with this version of
        the model.
      </span>
      <s:iterator value="submission.images" status="stat">
        <div id="<s:property value="#stat.index" />_imageFileInputContainer" class="subcontainer">
          <div id="<s:property value="#stat.index" />_imageFileInputFieldsContainer" >
            <s:fielderror>
              <s:param value="%{'submission.images['+#stat.index+'].dataFile'}" />
              <s:param value="%{'submission.images['+#stat.index+'].description'}"/>
            </s:fielderror>
            <p>Image/Screen Shot of the Model:
            <img src="../images/tip.gif" class="toolTip" title="Image/Screen shot of the Model::Provide at least one image associated with the model. E.g. graphical representation of the model/submodels within the model, simulation results" /> 
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
    <%--<span style="font-weight:bold">Add Supplementary Data Files (Optional):</span>
      <s:if test="modelData.supplementaryFiles != null && modelData.supplementaryFiles.size() > 0">
      <span>
        Previous versions of this model already have associated data files. 
        Please select the files you want to associate with this version of
        the model. New supplementary data files for this version can also be uploaded. 
      </span>
      <table cellpadding="4" width="100%">
        <s:iterator value="modelData.supplementaryFiles">
        <tr>
          <td>
            <s:checkbox name="submission.supFileIds" fieldValue="%{id}" />
            <a class="data" href="<s:property value="dataFileFileName" />" target="new"><s:property value="description" /></a>
          </td>
        </tr>
        </s:iterator>
      </table>
      </s:if>
      <s:else>
      <span>
        Please upload associated data files to accompany this version of the model.  
      </span>
      </s:else>--%>
      <span style="font-weight:bold">Add Supplementary Data Files:</span>
      <span>
        Please upload any new data files to accompany this version of the model.  
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
    <div><s:submit value="Submit Changes" /> or <a href="<s:url namespace="/models" action="model"><s:param name="accession" value="%{accession}" /><s:param name="version" value="%{version}" /></s:url>">Cancel</a></div>
    </s:form>
  </s:else>
</div>
<s:include value="includes/footer.jsp" />