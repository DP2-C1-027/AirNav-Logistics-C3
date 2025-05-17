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
	<acme:input-textbox code="customers.booking.form.label.locatorCode" path="locatorCode" placeholder="acme.placeholder.customer.booking.code"/>
	<acme:input-moment code="customers.booking.form.label.purchaseMoment" path="purchaseMoment"  readonly="true"/>
	<acme:input-select code="customers.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>
	<acme:input-money  code="customers.booking.form.label.price" path="price" readonly="true"/>
	<acme:input-textbox code="customers.booking.form.label.lastNibble" path="lastNibble" placeholder="acme.placeholder.customer.booking.nibble"/>
	
	
	
	
	<jstl:choose>
	<jstl:when test="${_command == 'show' && draftMode == false}">
	<acme:input-textbox  code="customers.booking.form.label.vuelos" path="flight" readonly="true"/>
		<acme:button code="customers.bookingPassenger.list.button.passenger" action="/customers/passenger/passengerList?bookingId=${id}"/>
		
			
		
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
		<acme:input-select  code="customers.booking.form.label.vuelos" path="flight" choices="${vuelos}"/>
		<acme:submit code="customers.booking.form.button.create" action="/customers/booking/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true }">
		<acme:input-textbox code="customers.booking.form.label.vuelos" path="flight" readonly="true"/>
		<acme:button code="customers.bookingPassenger.list.button.passenger" action="/customers/passenger/passengerList?bookingId=${id}"/>
		<acme:submit code="customers.booking.form.button.update" action="/customers/booking/update"/>
		<acme:submit code="customers.booking.form.button.publish" action="/customers/booking/publish"/>
		<acme:submit code="customers.booking.form.button.delete" action="/customers/booking/delete"/>
			<acme:button code="customers.passenger.form.button.createBooking" action="/customers/passenger/createBooking?bookingId=${id}"/>
		</jstl:when>
	</jstl:choose>
	
	
</acme:form>
