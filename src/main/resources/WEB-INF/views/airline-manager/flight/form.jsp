<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airlineManager.flight.form.label.tag" path="tag"/>
	<acme:input-textbox code="airlineManager.flight.form.label.indication" path="indication"/>
	<acme:input-money code="airlineManager.flight.form.label.cost" path="cost"/>
	<acme:input-textbox code="airlineManager.flight.form.label.description" path="description"/>
	
</acme:form>