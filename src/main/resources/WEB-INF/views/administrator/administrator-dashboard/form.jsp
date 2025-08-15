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
            <acme:print code="administrator.administrator-dashboard.form.label.amount-airports-grouped-operational-scope"/>
        </th>
    </tr>
    <c:forEach var="entry" items="${amountAirportsGroupedByOperationalScope}">
        <tr>
            <td>${entry.key}: ${entry.value}</td>
        </tr>
    </c:forEach>

    <tr>
        <th scope="row">
            <acme:print code="administrator.administrator-dashboard.form.label.amount-airlines-grouped-type"/>
        </th>
    </tr>
    <c:forEach var="entry" items="${amountAirlineGroupedByType}">
        <tr>
            <td>${entry.key}: ${entry.value}</td>
        </tr>
    </c:forEach>

	<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.ratio-airlines-email-phone"/>
		</th>
		<td>
			<acme:print value="${ratioAirlinesEmailAndPhone}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.ratio-active-aircrafts"/>
		</th>
		<td>
			<acme:print value="${ratioActiveAircrafts}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.ratio-inactive-aircrafts"/>
		</th>
		<td>
			<acme:print value="${ratioInactiveAircrafts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.ratio-high-score-reviews"/>
		</th>
		<td>
			<acme:print value="${ratioHighScoreReviews}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:print code="administrator.administrator-dashboard.form.title.review-statistics"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.review-count"/>
		</th>
		<td>
			<acme:print value="${adminReviews.count}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.review-average"/>
		</th>
		<td>
			<acme:print value="${adminReviews.average}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.review-min"/>
		</th>
		<td>
			<acme:print value="${adminReviews.min}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.review-max"/>
		</th>
		<td>
			<acme:print value="${adminReviews.max}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.administrator-dashboard.form.label.review-deviation"/>
		</th>
		<td>
			<acme:print value="${adminReviews.deviation}"/>
		</td>
	</tr>
</table>

<acme:return/>