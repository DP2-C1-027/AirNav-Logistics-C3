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

<acme:form>
	<acme:input-moment code="technician.maintanence-record.form.label.maintanenceMoment" path="maintanenceMoment"/>
	<acme:input-select code="technician.maintanence-record.form.label.status" path="status" choices="${StatusRecord}"/>
	<acme:input-money code="technician.maintanence-record.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-moment code="technician.maintanence-record.form.label.nextMaintanence" path="nextMaintanence"/>
	<acme:input-textbox code="technician.maintanence-record.form.label.notes" path="notes"/>
	
</acme:form>
