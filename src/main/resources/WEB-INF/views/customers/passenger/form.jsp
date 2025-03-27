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
	<acme:input-textbox code="customers.passenger.form.label.fullName" path="fullName"/>
	<acme:input-email code="customers.passenger.form.label.email" path="email"/>
	<acme:input-textbox code="customers.passenger.form.label.passportNumber" path="passportNumber"/>
	<acme:input-moment code="customers.passenger.form.label.dateOfBirth" path="dateOfBirth"/>
	<acme:input-textbox code="customers.passenger.form.label.specialNeeds" path="specialNeeds"/>
	
	 
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customers.passenger.form.button.create" action="/customers/passenger/create"/>
		</jstl:when>
		<jstl:when test="${_command == 'createBooking'}">
			<acme:submit code="customers.passenger.form.button.createBooking" action="/customers/passenger/createBooking?bookingId=${id }"/>
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true }">
			<acme:submit code="customers.passenger.form.button.update" action="/customers/passenger/update"/>
			<acme:submit code="customers.passenger.form.button.publish" action="/customers/passenger/publish"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
