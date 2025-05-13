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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>
	<acme:print code="administrator.administrator-dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">

	<tr>
        <th scope="row">
            <acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.lastFiveDestinations"/>
        </th>
    </tr>
    <c:forEach var="item" items="${lastFiveDestinations}">
        <tr>
            <td><acme:print value = "${item}"/></td>
        </tr>
    </c:forEach>
</table>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.legsWithIncidentSeverity3"/>
		</th>
		<td>
			<acme:print value="${legsWithIncidentSeverity3}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.legsWithIncidentSeverity7"/>
		</th>
		<td>
			<acme:print value="${legsWithIncidentSeverity7}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.legsWithIncidentSeverity10"/>
		</th>
		<td>
			<acme:print value="${legsWithIncidentSeverity10}"/>
		</td>
	</tr>
</table>
<table class="table table-sm">	
	<tr>
        <th scope="row">
            <acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.lastLegCrewMembers"/>
        </th>
    </tr>
    <c:forEach var="item" items="${lastLegCrewMembers}">
        <tr>
            <td><acme:print value = "${item}"/></td>
        </tr>
    </c:forEach>
	
	<tr>
        <th scope="row">
            <acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.flightAssignmentsGroupedByStatus"/>
        </th>
    </tr>
    <c:forEach var="entry" items="${flightAssignmentsGroupedByStatus}">
        <tr>
            <td><acme:print value = "${entry.key}: ${entry.value}"/></td>
        </tr>
    </c:forEach>
    
</table>

<h2>
	<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.title.review-statistics"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.review-count"/>
		</th>
		<td>
			<acme:print value="${flightAssignmentsStatsLastMonth.count}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.review-average"/>
		</th>
		<td>
			<acme:print value="${flightAssignmentsStatsLastMonth.average}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.review-min"/>
		</th>
		<td>
			<acme:print value="${flightAssignmentsStatsLastMonth.min}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.review-max"/>
		</th>
		<td>
			<acme:print value="${flightAssignmentsStatsLastMonth.max}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.flight-crew-member-dashboard.form.label.review-deviation"/>
		</th>
		<td>
			<acme:print value="${flightAssignmentsStatsLastMonth.deviation}"/>
		</td>
	</tr>
</table>

<acme:return/>