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
	<acme:input-textbox code="flight-crew-member.activity-log.form.label.typeOfIncident" path="typeOfIncident" readonly="draftMode" placeholder = "acme.placeholders.form.activityLog.typeOfIncident"/>
	<acme:input-textarea code="flight-crew-member.activity-log.form.label.description" path="description" readonly="draftMode" placeholder = "acme.placeholders.form.activityLog.description"/>
	<acme:input-integer code="flight-crew-member.activity-log.form.label.severityLevel" path="severityLevel" readonly="draftMode" placeholder = "acme.placeholders.form.activityLog.severityLevel"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create' && showAction}">
			<acme:submit code="flight-crew-member.activity-log.form.button.create" action="/flight-crew-member/activity-log/create?assignmentId=${assignmentId}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update') && showAction && draftMode == true}">
			<acme:submit code="flight-crew-member.activity-log.form.button.update" action="/flight-crew-member/activity-log/update"/>
			<acme:submit code="flight-crew-member.activity-log.form.button.delete" action="/flight-crew-member/activity-log/delete"/>
		</jstl:when>
	</jstl:choose>
		<jstl:if test="${acme:anyOf(_command, 'show|update') && showAction && draftMode == true && draftModeFlightAssignment == false}">
			<acme:submit code="flight-crew-member.activity-log.form.button.publish" action="/flight-crew-member/activity-log/publish"/>
		</jstl:if>
</acme:form>
