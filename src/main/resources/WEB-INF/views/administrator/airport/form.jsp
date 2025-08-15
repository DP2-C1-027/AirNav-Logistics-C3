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



<acme:form >
	<acme:input-textbox code="administrator.airport.form.label.name" path="name" placeholder = "acme.placeholders.form.airport.name"/>
	<acme:input-textbox code="administrator.airport.form.label.codigo" path="codigo" placeholder = "acme.placeholders.form.airport.codigo"/>
	<acme:input-select code="administrator.airport.form.label.operationalScope" path="operationalScope" choices="${operationalScope}"/>
	<acme:input-textbox code="administrator.airport.form.label.city" path="city" placeholder = "acme.placeholders.form.airport.city"/>
	<acme:input-textbox code="administrator.airport.form.label.country" path="country" placeholder = "acme.placeholders.form.airport.country"/>
	<acme:input-url code="administrator.airport.form.label.website" path="website" placeholder = "acme.placeholders.form.airport.website"/>
	<acme:input-email code="administrator.airport.form.label.email" path="email" placeholder = "acme.placeholders.form.airport.email"/>
	<acme:input-textbox code="administrator.airport.form.label.phoneNumber" path="phoneNumber" placeholder = "acme.placeholders.form.airport.phoneNumber"/>

	<jstl:choose>
				<jstl:when test="${acme:anyOf(_command, 'show|update')}">
					<acme:input-checkbox code="administrator.airport.form.label.confirmation" path="confirmation"/>
					<acme:submit code="administrator.airport.form.button.update" action="/administrator/airport/update"/>
				</jstl:when>
				<jstl:when test="${_command == 'create'}">
					<acme:input-checkbox code="administrator.airport.form.label.confirmation" path="confirmation"/>
					<acme:submit code="administrator.airport.form.button.create" action="/administrator/airport/create"/>
				</jstl:when>		
		</jstl:choose>	
</acme:form>
