<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:include value="includes/header.jsp" />
<h3><s:property value="submission.name" /> : Edit Model <s:property value="version" /></h3>

<div class="panels">
  <script type="text/javascript">
      jQuery().ready(function() {
          
    	  jQuery().newAttributeInput({
    			entriesIdx: <s:if test="submission.attributes != null"><s:property value="submission.attributes.size()" /></s:if><s:else>0</s:else>,
    			minEntries: 0
    		});

    		jQuery(".toolTip").tooltip({
    			showURL: false,
    			showBody: "::"
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
  <s:form method="post" theme="simple" action="submit_model_edit">
    <s:hidden name="accession" value="%{accession}" />
    <s:actionerror/>
    <s:include value="includes/edit_model_core.jsp"></s:include>
    
    <div><s:submit value="Submit Changes" /> or <a href="<s:url namespace="/models" action="model"><s:param name="accession" value="%{accession}" /></s:url>">Cancel</a></div>
  </s:form>
</div>


<s:include value="includes/footer.jsp" />