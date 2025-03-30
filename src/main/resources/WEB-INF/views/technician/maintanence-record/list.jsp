<%--
- list.jsp
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

<acme:list>
	
	<acme:list-column code="technician.maintanence-record.form.label.maintanenceMoment" path="maintanenceMoment" width="5%"/>
	<acme:list-column code="technician.maintanence-record.form.label.status" path="status" width="5%"/>
	<acme:list-column code="technician.maintanence-record.form.label.estimatedCost" path="estimatedCost" width="5%"/>
	<acme:list-column code="technician.maintanence-record.form.label.nextMaintanence" path="nextMaintanence" width="5%"/>
	<acme:list-column code="technician.maintanence-record.form.label.notes" path="notes" width="20%"/>
	
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.maintanence-record.list.button.create" action="/technician/maintanence-record/create"/>
</jstl:if>	


