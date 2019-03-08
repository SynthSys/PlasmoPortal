<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3>Create New User Account</h3>
<div class="panels">
<s:form theme="simple" action="create_new_user" namespace="/login">
<s:actionerror/>


<h4>Anti-spam test</h4>
<div class="panel">
<s:include value="includes/recaptcha.jsp" />
</div>


<h4>Contact Information</h4>
<div class="panel">
<table>
  <tr>
    <td colspan="2">
      <s:fielderror>
        <s:param value="%{'user.given'}" />
      </s:fielderror>
    </td>
  </tr>
  <tr>
    <td>First Name:<span class="errorMessage">*</span></td><td><s:textfield name="user.given" size="30" /></td>
  </tr>
  <tr>
    <td colspan="2">
      <s:fielderror>
        <s:param value="%{'user.family'}" />
      </s:fielderror>
    </td>
  </tr>
  <tr>
    <td>Last Name:<span class="errorMessage">*</span></td><td><s:textfield name="user.family" /></td>
  </tr>
  <tr>
    <td colspan="2">
      <s:fielderror>
        <s:param value="%{'user.email'}" />
      </s:fielderror>
    </td>
  </tr>
  <tr>
    <td>Email address:<span class="errorMessage">*</span></td><td><s:textfield name="user.email" /></td>
  </tr>
  <tr>
    <td colspan="2">
      <s:fielderror>
        <s:param value="%{'user.organisation'}" />
      </s:fielderror>
    </td>
  </tr>
  <tr>
    <td>Organisation:<span class="errorMessage">*</span></td><td><s:textfield name="user.organisation" /></td>
  </tr>
  
</table>
</div>
<h4>Login Information</h4>
<div class="panel">
<table>
  <tr>
    <td colspan="2">
      <s:fielderror>
        <s:param value="%{'user.userName'}" />
      </s:fielderror>
    </td>
  </tr>
  <tr>
    <td>Username:<span class="errorMessage">*</span></td><td><s:textfield name="user.userName" /></td>
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

<s:submit value="Create Account" />
</s:form>
</div>
<s:include value="includes/footer.jsp" />