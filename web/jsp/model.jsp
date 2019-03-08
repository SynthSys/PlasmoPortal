<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<span class="errorMessage"><s:property value="#parameters.msg"/></span>
<span class="errorMessage"><s:property value="msg"/></span>
<s:if test="(#session.user.oid != null && #session.user.oid == modelData.owner.oid && #session.user.supervisesUsers.size() > 1) || (#session.user.oid != null && #session.user.oid == modelData.owner.supervisor.oid)">
<p>
  <s:form theme="simple" namespace="/edit" action="update_model_owner">
    <span class="itemLabel">Change ownership of this model to:</span>
    <s:select name="ownerUserName" list="#session.user.supervisesUsers" listKey="userName" listValue="%{given + ' ' + family +' ('+userName+')'}"/>
    <s:hidden name="accession" value="%{modelData.accession}" />
    <s:submit value="Go" />
  </s:form>
</p>
</s:if>
<s:if test="#session.user.oid != null && #session.user.oid == modelData.owner.oid">
<ul class="buttons">
  <li><a class="toolTip" title="Add New Version::Upload a revised version of this model. Previously uploaded versions are not lost and can be accessed if required." href="<s:url namespace="/new" action="version"><s:param name="accession" value="modelData.accession" /></s:url>"><img src="../images/add.gif" alt="Add Version" /> Add New Version</a></li>
  <li><a class="toolTip" title="Edit Version::Edit/overwrite a specific version of a model." href="<s:url namespace="/edit" action="version"><s:param name="accession" value="modelData.accession" /><s:param name="version" value="modelData.version" /></s:url>"><img src="../images/edit.gif" alt="Edit Version" /> Edit This Version</a></li>
  <li><a class="toolTip" title="Edit Model::Edit model meta data associated with the model as a whole (e.g. the name of the model) as opposed to meta data associated with specific versions of this model." href="<s:url namespace="/edit" action="model"><s:param name="accession" value="modelData.accession" /></s:url>"><img src="../images/edit.gif" alt="Edit Model" /> Edit Model</a></li>
  <li><a class="toolTip" title="Edit Publications::Edit publications associated with the model as a whole." href="<s:url namespace="/edit" action="publications"><s:param name="accession" value="modelData.accession" /></s:url>"><img src="../images/edit.gif" alt="Edit Publications" /> Edit Publications</a></li>
  <li><a href="<s:url namespace="/delete" action="model"><s:param name="accession" value="modelData.accession" /></s:url>"><img src="../images/delete.gif" alt="Delete Model" /> Delete Model</a></li>
</ul>
</s:if>
<script type="text/javascript">

jQuery().ready(function() {
	jQuery("#modelBox").tabs();
	
	jQuery('#commentInput').ckeditor(
		function() {},
		{
			toolbar: 'Basic',
		 	height:250, 
		 	width:650,
		 	uiColor: '#CBF5CB',
		 	resize_enabled: false
		} 
	);

	jQuery("#commentForm").submit(function() {

		var ajxInput = jQuery("<input/>");
		ajxInput.attr("name", "ajxInput");
		ajxInput.attr("type", "hidden");
		ajxInput.attr("value", "true");
		$("#commentForm").prepend(ajxInput);
		
		var editor = $('#commentInput').ckeditorGet();
		editor.updateElement();
		
		$(this).ajaxSubmit(
			{
				target: '#comments_container',
				url: '<s:url namespace="/edit" action="comments"  />',
				type: 'POST',
				clearForm: true,
				success: processResponse
			}
		);
		return false;
	});

	function processResponse(responseText, statusText) {
		jQuery('#commentInput').ckeditorGet().setData('');
		var target = jQuery("#comments_container");
		var targetOffset = target.offset().top;
		jQuery("html,body").animate({scrollTop: targetOffset}, 750);
		
	};

	jQuery(".toolTip").tooltip({
		showURL: false,
		showBody: "::"
	});

	
});
</script>

<table width="100%" class="defBg nowrap" style="border:1px solid #CCCCCC; margin-top:2px;" cellspacing="2" cellpadding="0" border="0">
  <tr>
    <td>
      <s:if test="modelData.private">
        <img id="privateIcon" style="float:left;padding-right:2px" src="../images/padlock.gif" class="toolTip" title="Access: Restricted::The model can currently only be accessed by certain users." />
      </s:if>
      <h4>Model Name: <s:property value="modelData.name" /></h4>
    </td>    
    <td class="tenth">
      <h4 class="nowrap"><a target="_new" href="<s:url action="download"><s:param name="accession" value="modelData.accession" /><s:param name="version" value="modelData.version" /></s:url>">Download Model (<s:property value="modelData.format" />)</a></h4>
    </td>    
  </tr>
</table>
<div id="modelBox">
<%--<script type="text/javascript">
      window.addEvent('domready', function() {
          var modelTab = new TabSwapper({
              selectedClass: 'on',
        	  deselectedClass: 'off',
        	  tabs: $$('#modelBox ul.tabs li'),
        	  clickers: $$('#modelBox li a'),
        	  sections: $$('div.panels div.panel')//,
        	  /*use transitions to fade across*/
        	  //smooth: true,
        	  //smoothSize: true
          });
      });
</script>
 <ul class="tabs">
  <li class="off"><a>Overview</a></li>
  <li class="off"><a>Publications</a></li>
  <s:if test="modelData.transformOptions != null">
    <li class="off"><a>Model</a></li>
  </s:if>
</ul>--%>
<ul>
  <li><a href="#panel1">Overview</a></li>
  <s:if test="modelData.transformOptions != null">
    <li><a href="#panel2">Display/Run</a></li>
  </s:if>
  <li><a href="#panel3">Publications</a></li>
  <li><a href="#panel4">Comments</a></li>
</ul>

  <%-- <h4>
  <span class="icon_holder"></span>
  Overview
  </h4>--%>
  <div id="panel1">
    <s:include value="includes/model_overview.jsp" />
  </div>
  <s:if test="modelData.transformOptions != null">
  <%-- <h4>
  <span class="icon_holder"></span>
  Model
  </h4>--%>
  <div id="panel2">
    <%--<s:iterator value="modelData.transformOptions"  var="item">
      <div class="item">
        <a href="#"><s:property value="#item[1]" /></a>
        <div style="padding-top:4px;"><iframe frameborder="0" width="100%" height="300px" src="<s:url action="transform"><s:param name="accession" value="modelData.accession" /><s:param name="tf" value="#item[0]" /><s:param name="version" value="modelData.version" /></s:url>"></iframe></div>
      </div>
    </s:iterator>--%>
    <table width="100%" cellspacing="6" cellpadding="0">
      <s:iterator value="modelData.transformOptions"  var="item">
       <tr>
        <td class="item">
          <a onclick="window.open('<s:url action="transform"><s:param name="accession" value="modelData.accession" /><s:param name="tf" value="#item[0]" /><s:param name="version" value="modelData.version" /></s:url>', 'plasmo_window','status=0,toolbar=0,location=0,menubar=0,directories=0,scrollbars=yes,resizable=yes,height=300,width=800');return false; " href="#"><s:property value="#item[1]" /></a>
        </td>
      </tr>
      </s:iterator>
      <s:if test="modelData.simileWebURL != null">
        <tr>
        
          <td class="item">
            <a href="<s:property value="modelData.simileWebURL" />">
              View this model in SimleWeb, an online tool for running and displaying models. 
              NB: SimileWeb does not currently work in Internet Explorer 7 and 8 but should work in 
              all recent versions of Firefox, Chrome and Safari browsers.
            </a>
          </td>
        </tr>
      </s:if>
    </table>
  </div>
  </s:if>
  <%-- <h4>
  <span class="icon_holder"></span>
  Publications
  </h4>--%>
  <div id="panel3">
    <s:include value="includes/model_publications.jsp" />
  </div>
  <%-- <h4>Comments</h4>--%>
  <div id="panel4">
    <s:if test="#session.user == null">
      <a href="<s:url namespace="/login"  action="user_login"/>">Login to comment</a>
    </s:if>
    <s:else>
      <s:if test="modelData.comments != null">
        <a href="#comment">Add a comment</a>
      </s:if>
    </s:else>
    <div id="comments_container">
    <s:include value="includes/model_comments.jsp" />
    </div>
    <s:if test="#session.user != null">
      <div>
        <a name="comment"></a><p>Add a comment</p>
        <s:form id="commentForm" theme="simple" method="post" namespace="/edit" action="comments">
          <s:hidden name="accession" value="%{modelData.accession}"/>
          <s:textarea name="comment" id="commentInput" rows="15" cols="100" />
          <br/>
          <s:submit value="Submit Comment" />
        </s:form>
      </div>
    </s:if>
  </div>
  
</div>
<s:include value="includes/footer.jsp" />