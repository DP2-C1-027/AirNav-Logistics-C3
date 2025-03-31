<%--
- assistance-agent-dashboard/form.jsp
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
    <acme:print value="assistance-agent.assistance-agent-dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
    <!-- Ratios -->
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.ratioOfResolvedClaims"/>
        </th>
        <td>
            <acme:print value="${ratioOfResolvedClaims}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.ratioOfRejectedClaims"/>
        </th>
        <td>
            <acme:print value="${ratioOfRejectedClaims}"/>
        </td>
    </tr>
    
    <!-- Top 3 meses con más claims -->
    <tr>
        <th scope="row" colspan="2">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.topThreeMonthsWithMostClaims"/>
        </th>
    </tr>
    <jstl:forEach var="month" items="${topThreeMonthsWithMostClaims}">
        <tr>
            <td colspan="2">
                <acme:print value="${month}"/>
            </td>
        </tr>
    </jstl:forEach>
</table>

<h2>
    <acme:print value="assistance-agent.assistance-agent-dashboard.form.title.logs-statistics"/>
</h2>

<table class="table table-sm">
    <!-- Estadísticas de logs por claim -->
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.averageNumberOfLogsPerClaim"/>
        </th>
        <td>
            <acme:print value="${statsLogsPerClaim.average}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.minimumNumberOfLogsPerClaim"/>
        </th>
        <td>
            <acme:print value="${statsLogsPerClaim.min}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.maximumNumberOfLogsPerClaim"/>
        </th>
        <td>
            <acme:print value="${statsLogsPerClaim.max}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.standardDeviationOfLogsPerClaim"/>
        </th>
        <td>
            <acme:print value="${statsLogsPerClaim.deviation}"/>
        </td>
    </tr>
</table>

<h2>
    <acme:print value="assistance-agent.assistance-agent-dashboard.form.title.last-month-statistics"/>
</h2>

<table class="table table-sm">
    <!-- Estadísticas de claims del último mes -->
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.averageNumberOfClaimsLastMonth"/>
        </th>
        <td>
            <acme:print value="${statsClaimsLastMonth.average}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.minimumNumberOfClaimsLastMonth"/>
        </th>
        <td>
            <acme:print value="${statsClaimsLastMonth.min}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.maximumNumberOfClaimsLastMonth"/>
        </th>
        <td>
            <acme:print value="${statsClaimsLastMonth.max}"/>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print value="assistance-agent.assistance-agent-dashboard.form.label.standardDeviationOfClaimsLastMonth"/>
        </th>
        <td>
            <acme:print value="${statsClaimsLastMonth.deviation}"/>
        </td>
    </tr>
</table>

<acme:return/>