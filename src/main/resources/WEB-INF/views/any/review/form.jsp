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
	<acme:input-textbox code="any.review.form.label.name" path="name"/>
	<acme:input-moment code="any.review.form.label.moment" path="moment"/>
	<acme:input-textbox code="any.review.form.label.subject" path="subject"/>
	<acme:input-textbox code="any.review.form.label.text" path="text"/>
	<acme:input-double code="any.review.form.label.score" path="score"/>
	<acme:input-textbox code="any.review.form.label.recommended" path="recommended"/>

	<jstl:choose>
				<jstl:when test="${_command == 'create'}">
					<acme:submit code="any.review.form.button.create" action="/any/review/create"/>
				</jstl:when>		
		</jstl:choose>	
</acme:form>
