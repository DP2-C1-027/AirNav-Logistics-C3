<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="airline-manager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="20%"/>
	<acme:list-column code="airline-manager.leg.list.label.scheduledArrival" path="scheduledArrival" width="20%"/>
	<acme:list-column code="airline-manager.leg.list.label.flightNumber" path="flightNumber" width="20%" sortable="${false}"/>
	<acme:list-column code="airline-manager.leg.list.label.departureAirport" path="departureAirport" width="20%" sortable="${false}"/>
	<acme:list-column code="airline-manager.leg.list.label.arrivalAirport" path="arrivalAirport" width="20%" sortable="${false}"/>
	<acme:list-column code="airline-manager.leg.list.label.flight" path="flight" width="20%" sortable="${false}"/>
	<acme:list-payload path="payload"/>
</acme:list>
<jstl:choose>
<jstl:when test="${_command == 'list-flight' && flightDraftMode==true}">
					<acme:button code="airline-manager.leg.form.button.create" action="/airline-manager/leg/create?flightId=${flightId}"/>
</jstl:when>
<jstl:when test="${_command == 'list'}">
					<acme:button code="airline-manager.leg.form.button.create" action="/airline-manager/leg/create"/>
</jstl:when>
</jstl:choose>