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
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.duty" path="duty" choices="${dutyChoices}" readonly="draftMode"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.currentStatus" path="currentStatus" choices="${statusChoices}" readonly="draftMode"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks" readonly="draftMode" placeholder = "acme.placeholders.form.flightAssignment.remarks"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.leg" path="leg" choices="${legChoices}" readonly="draftMode" />
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete')}">
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
		</jstl:when>
	</jstl:choose>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:button code="flight-crew-member.flight-assignment.form.button.activity-log" action="/flight-crew-member/activity-log/list?assignmentId=${id}"/>
			<acme:button code="flight-crew-member.flight-assignment.form.button.visa-requirements" action="/flight-crew-member/visa-requirements/list?assignmentId=${id}"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update?assignmentId=${id}"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/flight-assignment/delete"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.publish" action="/flight-crew-member/flight-assignment/publish"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
			<acme:button code="flight-crew-member.flight-assignment.form.button.activity-log" action="/flight-crew-member/activity-log/list?assignmentId=${id}"/>
			<acme:button code="flight-crew-member.flight-assignment.form.button.visa-requirements" action="/flight-crew-member/visa-requirements/list?assignmentId=${id}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
