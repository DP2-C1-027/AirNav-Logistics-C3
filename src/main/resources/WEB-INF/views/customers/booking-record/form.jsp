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
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update') && draftMode == true}">
			<acme:input-select code="customers.booking-record.form.label.booking" path="booking" choices="${bookings}"/>
			<acme:input-select code="customers.booking-record.form.label.passenger" path="passenger" choices="${passengers}"/>
			<acme:submit code="customers.booking-record.form.label.update" action="/customers/booking-record/update"/>
			
		</jstl:when>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:input-select code="customers.booking-record.form.label.booking" path="booking" choices="${bookings}" readonly="true"/>
			<acme:input-select code="customers.booking-record.form.label.passenger" path="passenger" choices="${passengers}" readonly="true"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="customers.booking-record.form.label.booking" path="booking" choices="${bookings}"/>
			<acme:input-select code="customers.booking-record.form.label.passenger" path="passenger" choices="${passengers}"/>
			<acme:submit code="customers.booking-record.form.label.create" action="/customers/booking-record/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>