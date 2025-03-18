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

<acme:form  readonly="${readonly}">
	<acme:input-textbox code="customers.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-textbox code="customers.booking.form.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-textbox code="customers.booking.form.label.travelClass" path="travelClass"/>
	<acme:input-textbox code="customers.booking.form.label.lastNibble" path="lastNibble"/>
	<acme:input-textbox code="customers.booking.form.label.price" path="price"/>
	<acme:input-textbox code="customers.booking.form.label.passenger" path="passenger"/>
	
	
	<jstl:if test="${_command == 'passengers'}">
		<acme:submit code="customers.bookingPassenger.form.button.passenger" action="/customers/bookingPassenger/passengerList"/>
	</jstl:if>
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.provider.form.button.create" action="/authenticated/provider/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.provider.form.button.update" action="/authenticated/provider/update"/>
	</jstl:if>
</acme:form>
