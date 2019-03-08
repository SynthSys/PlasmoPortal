<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="/Templates/plasmo.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>PlaSMo - Plant Systems Biology Modelling</title>
<!-- InstanceEndEditable -->
<!-- InstanceBeginEditable name="head" -->
<link href="${pageContext.request.contextPath}/css/main.css" type="text/css" rel="stylesheet" />
<!--[if lt IE 7]>
  <style media="screen" type="text/css">
    .col1 {
	    width:100%;
	}
  </style>
<![endif]-->
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/mootools-1.2.1-core-nc.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/mootools-1.2-more.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/clientcide-trunk-773.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/model-submission.js"></script>--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.toggleGroup.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.modelSubmission.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.bgiframe.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.dimensions.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.form.js"></script>
<!--<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery.CKEditor.js"></script>-->
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/ckeditor/adapters/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-1.7.2.custom.min.js"></script>
<link href="${pageContext.request.contextPath}/css/custom-theme/jquery-ui-1.7.2.custom.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">var CKEDITOR_BASEPATH = '${pageContext.request.contextPath}/javascript/ckeditor/';</script>
<!-- InstanceEndEditable -->
</head>

<body>
<div id="header">
    <img src="${pageContext.request.contextPath}/images/plasmo_circle_logo.png" width="110" height="110" style="float:left" />
    <h1>Plant Systems Biology Modelling</h1>
    <h3>A Database of Plant Growth Models</h3>
    <div id="topMenu">
      <ul class="menuTabs">
        <li><a class="menuItem" href="<s:url namespace="/models" action="all" />">Browse Models</a></li>
        <li><a class="menuItem" href="<s:url namespace="/new" action="model" />">Submit New Model</a></li>
        <%--<li>
             <s:if test="#session.user.isSupervisor == true">
            <a class="menuItem" href="<s:url namespace="/usermanagment" action=""/>">User Management</a>
  		</s:if>
  		</li>--%>
        <li>
        <s:if test="#session.user == null">
          <a class="menuItem" href="<s:url namespace="/login"  action="user_login"/>">Login</a>
        </s:if>
        <s:else>        
          <a class="menuItem" href="<s:url namespace="/login" action="logout" />">Logout: <s:property value="#session.user.userName"/></a>
          <%-- (<a class="menuItem" href="<s:url namespace="/users" action="user" />"><s:property value="#session.user.userName"/></a>)--%>
        </s:else>
        </li>
      </ul>
      <div class="search">
        <s:form namespace="/" method="post" theme="simple" action="search">
          Search
          <%--<s:hidden name="type" value="models" />--%>
          <s:textfield size="20" name="query" />
          <s:submit value="Search" />
        </s:form>
      </div>
    </div>
</div>
<div id="advert">
    <a href="http://www.timing-metabolism.eu/"><img height="78" src="${pageContext.request.contextPath}/images/timet-logo-sm.png" /></a>
<a href="http://www.sbsi.ed.ac.uk/"><img src="${pageContext.request.contextPath}/images/sbsi.png" /></a>
</div>
<div class="colmask leftmenu">
  <div class="colright">
    <div class="col1wrap">
      <div class="col1">