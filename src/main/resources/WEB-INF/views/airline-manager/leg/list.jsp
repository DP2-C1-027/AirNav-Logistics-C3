<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="airlineManager.leg.list.label.flightNumber" path="flightNumber" width="20%"/>
	<acme:list-column code="airlineManager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="20%"/>
	<acme:list-column code="airlineManager.leg.list.label.scheduledArrival" path="scheduledArrival" width="20%"/>
	<acme:list-column code="airlineManager.leg.list.label.departureAirport" path="departureAirport" width="20%"/>
	<acme:list-column code="airlineManager.leg.list.label.arrivalAirport" path="arrivalAirport" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>
<acme:button code="airlineManager.leg.list.button.create" action="/airline-manager/leg/create"/>