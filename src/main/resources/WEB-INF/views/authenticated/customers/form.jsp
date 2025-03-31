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
	<acme:input-textbox code="authenticated.customers.form.label.code" path="codigo" placeholder ="acme.placeholders.form.customer.codigo"/>
	<acme:input-textbox code="authenticated.customers.form.label.phone" path="phone"/>
	<acme:input-textarea code="authenticated.customers.form.label.physicalAddress" path="physicalAddress"/>
	<acme:input-textbox code="authenticated.customers.form.label.city" path="city"/>
	<acme:input-textbox code="authenticated.customers.form.label.country" path="country"/>
	<acme:input-textbox code="authenticated.customers.form.label.earnedPoints" path="earnedPoints"/>
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.customers.form.button.create" action="/authenticated/customers/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.customers.form.button.update" action="/authenticated/customers/update"/>
	</jstl:if>
</acme:form>
