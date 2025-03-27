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
	<acme:list-column code="customers.booking-record.list.label.booking" path="booking" width="20%"/>
	<acme:list-column code="customers.booking-record.list.label.passenger" path="passenger" width="20%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="customers.booking-record.form.label.create" action="/customers/booking-record/create"/>
</jstl:if>
	

