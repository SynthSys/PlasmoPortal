<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3>Change Password</h3>

<div class="panels">
<s:form theme="simple" action="save_password" namespace="/new">
<s:actionmessage/>
<s:actionerror/>


<h4>Login Information</h4>
<div class="panel">
<table>
  <tr>
    <td>Name:</td><td><s:property value="#session.user.given"/>&nbsp;<s:property value="#session.user.family"/></td>
  </tr>
  <tr>
    <td>Username:</td><td><s:property value="#session.user.userName"/></td>
  </tr>
  <tr>
    <td colspan="2">
      <s:fielderror>
        <s:param value="%{'user.password'}" />
      </s:fielderror>
    </td>
  </tr>
  <tr>
    <td>Password:<span class="errorMessage">*</span></td><td><s:password name="user.password" /></td>
  </tr>
  <tr>
    <td colspan="2">
      <s:fielderror>
        <s:param value="%{'user.confirmPassword'}" />
      </s:fielderror>
    </td>
  </tr>
  <tr>
    <td>Confirm Password:<span class="errorMessage">*</span></td><td><s:password name="user.confirmPassword" /></td>
  </tr>
</table>

</div>
<s:submit value="Save New Password" />
</s:form>
</div>

<s:include value="includes/footer.jsp" />