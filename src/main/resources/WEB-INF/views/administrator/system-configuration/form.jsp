<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
     <acme:input-select code="administrator.system-configuration.form.label.currency" path="currency" choices="${systemCurrencies}"/>
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update')}">
            <acme:submit code="administrator.system-configuration.form.button.update" action="/administrator/system-configuration/update"/>
        </jstl:when>
    </jstl:choose>
</acme:form>
