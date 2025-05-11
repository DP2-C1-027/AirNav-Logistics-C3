<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form>
	<acme:input-textbox code="authenticated.airlineManager.form.label.codigo" path="codigo"/>
	
	<acme:input-integer code="authenticated.airlineManager.form.label.yearsOfService" path="yearsOfService"/>
	<acme:input-moment code="authenticated.airlineManager.form.label.dateOfBirth" path="dateOfBirth"/>
	<acme:input-url code="authenticated.airlineManager.form.label.photo" path="photo"/>
	

	
	<jstl:if test="${_command == 'create'}">
		<acme:submit  code="authenticated.airlineManager.form.button.create" action="/authenticated/airline-manager/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.airlineManager.form.button.update" action="/authenticated/airline-manager/update"/>
	</jstl:if>
</acme:form>
