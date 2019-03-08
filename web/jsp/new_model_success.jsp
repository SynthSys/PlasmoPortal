<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="includes/header.jsp" />
  <h3>Model Submitted</h3>
  <p>
    You have successfully submitted your model. The id of the model is 
    <a href="<s:url namespace="/models" action="model"><s:param name="accession" value="%{submission.accession}" /></s:url>" class="data"><s:property value="submission.accession"/></a> 
    and can be viewed 
    <a href="<s:url namespace="/models" action="model"><s:param name="accession" value="%{submission.accession}" /></s:url>" class="data">here</a>.
  </p>
<s:include value="includes/footer.jsp" />