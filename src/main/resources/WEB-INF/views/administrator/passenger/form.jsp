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

<acme:form >
	<acme:input-textarea code="administrator.passenger.form.label.fullName" path="fullName"/>
	<acme:input-email code="administrator.passenger.form.label.email" path="email"/>
	<acme:input-textbox code="administrator.passenger.form.label.passportNumber" path="passportNumber" placeholder="acme.placeholders.customer.passenger.number"/>
	<acme:input-moment code="administrator.passenger.form.label.dateOfBirth" path="dateOfBirth"/>
	<acme:input-textbox code="administrator.passenger.form.label.specialNeeds" path="specialNeeds"/>
	
	 
	
	
</acme:form>
