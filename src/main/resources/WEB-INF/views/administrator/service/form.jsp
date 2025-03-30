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
	<acme:input-textbox code="administrator.service.form.label.name" path="name"/>
	<acme:input-textbox code="administrator.service.form.label.picture" path="picture"/>
	<acme:input-textbox code="administrator.service.form.label.averageDwellTime" path="averageDwellTime"/>
	<acme:input-textbox code="administrator.service.form.label.promotionCode" path="promotionCode"/>
	<acme:input-money  code="administrator.service.form.label.money" path="money"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
		<acme:input-checkbox code="administrator.service.form.label.confirmation" path="confirmation"/> 
			<acme:submit code="administrator.service.form.button.create" action="/administrator/service/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="administrator.service.form.button.update" action="/administrator/service/update"/>
			<acme:submit code="administrator.service.form.button.delete" action="/administrator/service/delete"/>
		</jstl:when>
	</jstl:choose>
	
</acme:form>