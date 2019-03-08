<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3>Add New Model Format</h3>
    
    <script>
    jQuery(".toolTip").tooltip({
		showURL: false,
		showBody: "::"
	});
    </script>

<s:form theme="simple" action="submit_format" method="post" enctype="multipart/form-data">
  <div class="subcontainer">
    <table>
      <tbody>
        <tr>
          <td>
            <s:fielderror>
            <s:param value="%{'format'}" />
            </s:fielderror>
          </td>
        </tr>
        <tr>
          <td>
            Format Name:<span class="errorMessage">*</span>
            <img src="../images/tip.gif" class="toolTip" title="Format Name::The name of the model format being uploaded" />
          </td>
        </tr>
        <tr>
          <td><s:textfield name="format" size="40" /></td>
        </tr>
        <tr>
          <td>
            <s:if test="!fieldErrors['validSchema'].isEmpty">
            <span class="errorMessage">
              The file you have submitted is not a valid XMLSchema.
              Please correct the following errors before submitting the file:
            </span>
            <div>
              <ul>
              <s:iterator value="fieldErrors['validSchema']">
                <li><span class="errorMessage"><s:property/></span></li>
              </s:iterator>
              </ul>
            </div>
            </s:if>
          </td>
        </tr>
        <tr>
          <td>
            Schema File:
            <img src="../images/tip.gif" class="toolTip" title="Schema File::Optional. An XML Schema file that defines the model format. The schema will be used to validate models uploaded in this format." />
          </td>
        </tr>
        <tr>
          <td><s:file name="schema" size="70"/></td>
        </tr>
        <tr>
          <td colspan="2">
            <s:fielderror>
              <s:param value="%{'description'}" />
            </s:fielderror>
          </td>
        </tr>
        <tr>
          <td>
            Description:
            <img src="../images/tip.gif" class="toolTip" title="Description::Optional. A description of the model format." />
          </td>
        </tr>
        <tr>
          <td><s:textarea name="description" cols="60" rows="5" /></td>
        </tr>
        <tr>
          <td><s:submit value="Add New Format"/></td>
        </tr>
      </tbody>
    </table>
  </div>
</s:form>










<s:include value="includes/footer.jsp" />