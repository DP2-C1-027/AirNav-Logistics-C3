<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.airline.form.label.name" path="name" placeholder = "acme.placeholders.form.airline.name"/>
	<acme:input-textbox code="administrator.airline.form.label.codigo" path="codigo" placeholder = "acme.placeholders.form.airline.codigo"/>
	<acme:input-url code="administrator.airline.form.label.website" path="website" placeholder = "acme.placeholders.form.airline.website"/>
	<acme:input-select code="administrator.airline.form.label.type" path="type" choices="${types}"/>
	<acme:input-moment  code="administrator.airline.form.label.foundationMoment" path="foundationMoment" placeholder = "acme.placeholders.form.airline.foundationMoment"/>
	<acme:input-email code="administrator.airline.form.label.email" path="email" placeholder = "acme.placeholders.form.airline.email"/>
	<acme:input-textbox code="administrator.airline.form.label.phoneNumber" path="phoneNumber" placeholder = "acme.placeholders.form.airline.phoneNumber"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
		<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airline.form.button.create" action="/administrator/airline/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airline.form.button.update" action="/administrator/airline/update"/>
		</jstl:when>
	</jstl:choose>
	
</acme:form>