<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.review.list.label.name" path="name" width="10%"/>
	<acme:list-column code="any.review.list.label.moment" path="moment" width="20%"/>
	<acme:list-column code="any.review.list.label.subject"  path="subject" width="20%"/>
	
	<acme:list-payload path="payload"/>
</acme:list>
<acme:button code="any.review.list.button.create" action="/any/review/create"/>