<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form>
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.codigo" path="codigo"/>
	
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.spokenLanguages" path="spokenLanguages"/>
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.moment" path="moment"/>
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.salary" path="salary"/>
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.photo" path="photo"/>
	<acme:input-textbox code="authenticated.assistanceAgent.form.label.airline" path="airline"/>
	

	
	<jstl:if test="${_command == 'create'}">
		<acme:submit  code="authenticated.assistanceAgent.form.button.create" action="/authenticated/assistanceAgent/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.consassistanceAgentumer.form.button.update" action="/authenticated/assistanceAgent/update"/>
	</jstl:if>
</acme:form>
