<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="airlineManager.flight.list.label.tag" path="tag" width="20%"/>
	<acme:list-column code="airlineManager.flight.list.label.indication" path="indication" width="10%"/>
	<acme:list-column code="airlineManager.flight.list.label.cost" path="cost" width="70%"/>
	<acme:list-column code="airlineManager.flight.list.label.description" path="description" width="70%"/>
	<acme:list-payload path="payload"/>
</acme:list>
<acme:button code="airlineManager.flight.list.button.create" action="/airline-manager/flight/create"/>
