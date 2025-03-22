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


<jstl:if test="${_command == 'list'}">
<acme:list navigable="true">
	<acme:list-column code="customers.passenger.list.label.fullName" path="fullName" width="20%"/>
	<acme:list-column code="customers.passenger.list.label.email" path="email" width="10%"/>
	<acme:list-column code="customers.passenger.list.label.passportNumber" path="passportNumber" width="20%"/>
	<acme:list-column code="customers.passenger.list.label.dateOfBirth" path="dateOfBirth" width="20%"/>
	<acme:list-column code="customers.passenger.list.label.specialNeeds" path="specialNeeds" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>
	<acme:button code="customers.passenger.list.button.create" action="/customers/passenger/create"/>
</jstl:if>

<jstl:if test="${_command != 'list'}">
<acme:list  navigable="false">
	<acme:list-column code="customers.passenger.list.label.fullName" path="fullName" width="20%"/>
	<acme:list-column code="customers.passenger.list.label.email" path="email" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>
</jstl:if>
	

