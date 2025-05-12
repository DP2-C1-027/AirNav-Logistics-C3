<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airlineManager.flight.form.label.tag" path="tag"/>
	<acme:input-textbox code="airlineManager.flight.form.label.indication" path="indication"/>
	<acme:input-money code="airlineManager.flight.form.label.cost" path="cost"/>
	<acme:input-textbox code="airlineManager.flight.form.label.description" path="description"/>
	<acme:input-select code="airlineManager.flight.form.label.airlines" path="airline" choices="${airlines}"/>
	
	<jstl:choose>
				<jstl:when test="${_command == 'show' && draftMode == false}">
					<acme:button code="airlineManager.flight.form.button.legs" action="/airline-manager/leg/list-flight?masterId=${id}"/>			
				</jstl:when>
				<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')  && draftMode == true}">
					<acme:submit code="airlineManager.flight.form.button.update" action="/airline-manager/flight/update"/>
					<acme:submit code="airlineManager.flight.form.button.delete" action="/airline-manager/flight/delete"/>
					<acme:submit code="airlineManager.flight.form.button.publish" action="/airline-manager/flight/publish"/>
				</jstl:when>
				<jstl:when test="${_command == 'create'}">
					<acme:submit code="airlineManager.flight.form.button.create" action="/airline-manager/flight/create"/>
				</jstl:when>		
		</jstl:choose>	
</acme:form>