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
	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.moment" path="moment" readonly="true" placeholder = "acme.placeholders.form.flightAssignment.moment"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.duty" path="duty" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.currentStatus" path="currentStatus" readonly="true"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks" readonly="true" placeholder = "acme.placeholders.form.flightAssignment.remarks"/>

	<h2>
		<acme:print code="flight-crew-member.flight-assignment.form.title.flight-crew-member"/>
	</h2>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.flightCrewMember" path="flightCrewMember" readonly="true"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.codigo" path="codigo" readonly="true"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.phoneNumber" path="phoneNumber" readonly="true"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.languageSkills" path="languageSkills" readonly="true"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.availabilityStatus" path="availabilityStatus" readonly="true"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.salary" path="salary" readonly="true"/>
	<acme:input-integer code="authenticated.flight-crew-member.form.label.yearsOfExperience" path="yearsOfExperience" readonly="true"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.airline" path="airline" readonly="true"/>
				
	<h2>
		<acme:print code="flight-crew-member.flight-assignment.form.title.leg"/>
	</h2>
	<acme:input-textbox code="airlineManager.leg.form.label.flightNumber" path="flightNumber" readonly="true"/>
	<acme:input-moment code="airlineManager.leg.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
	<acme:input-moment code="airlineManager.leg.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.status" path="status" readonly="true"/>
	<acme:input-integer code="airlineManager.leg.form.label.duration" path="duration" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.departureAirport" path="departureAirport" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.arrivalAirport" path="arrivalAirport" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.aircraft" path="aircraft" readonly="true"/>
	<acme:input-textbox code="airlineManager.leg.form.label.flight" path="flight" readonly="true"/>
	<acme:input-textbox code="authenticated.flight-crew-member.form.label.airline" path="legAirline" readonly="true"/>
</acme:form>
