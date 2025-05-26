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
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:input-select code="technician.involved-in.form.label.maintanenceRecord" path="maintanenceRecord" choices="${maintanenceRecord}"/>
			<acme:input-select code="technician.involved-in.form.label.task" path="task" choices="${task}"/>
			<acme:submit code="technician.involved-in.form.label.update" action="/technician/involved-in/update"/>
			<acme:submit code="technician.involved-in.form.label.delete" action="/technician/involved-in/delete"/>
			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show') && draftMode == false}">
			<acme:input-select code="technician.involved-in.form.label.maintanenceRecord" path="maintanenceRecord" choices="${maintanenceRecord}" readonly="true"/>
			<acme:input-select code="technician.involved-in.form.label.task" path="task" choices="${task}" readonly="true"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="technician.involved-in.form.label.maintanenceRecord" path="maintanenceRecord" choices="${maintanenceRecord}"/>
			<acme:input-select code="technician.involved-in.form.label.task" path="task" choices="${task}"/>
			<acme:submit code="technician.involved-in.form.label.create" action="/technician/involved-in/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
