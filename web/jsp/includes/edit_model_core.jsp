<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<table>
      <tr>
        <td colspan="2">
          <s:fielderror>
            <s:param value="%{'submission.name'}" />
          </s:fielderror>
        </td>
      </tr>
      <tr>
        <td>Model Name:<span class="errorMessage">*</span></td>
        <td><s:textfield size="55" name="submission.name" /></td>
      </tr>
      <tr>
        <td colspan="2">
          <s:fielderror>
            <s:param value="%{'submission.description'}" />
          </s:fielderror>
        </td>
      </tr>
      <tr>
        <td>Model Description:
        <img src="../images/tip.gif" class="toolTip" title="Model Description::High level view of the model. E.g. Description of any assumptions made when encoding the model in its current format" /></td>
        <td><s:textarea cols="80" rows="6" id="submissionDescription" name="submission.description" /></td>
      </tr>
      </table>
      
      <div class="subcontainer">
        <p style="font-weight:bold">
          Access control:
          <img src="../images/tip.gif" class="toolTip" title="Access Control::The submitter can choose to restrict access to the submitted model. The submitter can choose which user groups, if any, can also access the model. If no user groups are selected, only the submitter will have access to the model." />
        </p>
        <s:checkbox name="submission.private" id="private" /> <s:label for="private" value="Restrict Access to this model"/>
        <div style="margin-left:20px;padding:5px" id="userGroupSelect">
          <span>Please select which user groups can access the model from the list below:</span>
          <s:iterator value="submission.groups" var="item" status="stat">
          <div>
            <s:checkbox id="user_ckbx_%{grpOid}" name="submission.accessGroupIds" fieldValue="%{grpOid}" value="%{selected}" />
            <s:label for="user_ckbx_%{grpOid}" value="%{grpName}"/>
            <s:if test="grpDescription != null && grpDescription != ''">
              <s:label for="user_ckbx_%{grpOid}" value="(%{grpDescription})"/>
            </s:if> 
          </div>
          </s:iterator>
        </div>
      </div>
      
      <s:iterator value="submission.attributes" status="stat">
        <div class="subcontainer" id="<s:property value="#stat.index"/>_attributeContainer">
          <s:fielderror>
            <s:param value="%{'submission.attributes['+#stat.index+'].name'}" />
            <s:param value="%{'submission.attributes['+#stat.index+'].value'}"/>
          </s:fielderror>
          <table>
            <tr><td colspan="2">New Attribute</td></tr>
            <tr><td>Name:</td><td><s:textfield name="%{'submission.attributes['+#stat.index+'].name'}" size="55"/></td></tr>
            <tr><td>Value:</td><td><s:textarea id="%{'attInput'+#stat.index}" name="%{'submission.attributes['+#stat.index+'].value'}" cols="80" rows="6" /></td></tr>
          </table>
        </div>
        <script type="text/javascript">
            jQuery('#attInput'+<s:property value="#stat.index" />).ckeditor(
        		function() {},
        		{
        			toolbar: 'Basic',
        		 	height:180, 
        		 	width:550,
        		 	uiColor: '#CBF5CB',
        		 	resize_enabled: false
        		} 
        	);
        </script>
      </s:iterator>
      
      <div style="padding:4px" id="attrButtonContainer">
        <s:submit id="addAttr" value="Add New Model Attribute" type="button" />
        <s:submit id="removeAttr" value="Remove Last Attribute" type="button" />
        <img src="../images/tip.gif" class="toolTip" title="Additional Attributes::Add any additional user-defined attributes for this model. Enter an attibute name and the value of the attribute" />
      </div>