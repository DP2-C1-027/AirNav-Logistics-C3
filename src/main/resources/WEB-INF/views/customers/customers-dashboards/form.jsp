<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<div class="container text-center">
    <!-- Estadísticas de reservas -->
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customers-dashboard.booking-statistics" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.count-booking" /></th>
            <td><acme:print value="${booking5Years.count}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.average-booking" /></th>
            <td><acme:print value="${booking5Years.average}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.min-booking" /></th>
            <td><acme:print value="${booking5Years.min}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.max-booking" /></th>
            <td><acme:print value="${booking5Years.max}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.devv-booking" /></th>
            <td><acme:print value="${booking5Years.deviation}" /></td>
        </tr>
    </table>

    <!-- Estadísticas de pasajeros -->
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customers-dashboard.passenger-statistics" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.count-passenger" /></th>
            <td><acme:print value="${passengersBooking.count}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.average-passenger" /></th>
            <td><acme:print value="${passengersBooking.average}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.min-passenger" /></th>
            <td><acme:print value="${passengersBooking.min}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.max-passenger" /></th>
            <td><acme:print value="${passengersBooking.max}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.devv-passenger" /></th>
            <td><acme:print value="${passengersBooking.deviation}" /></td>
        </tr>
    </table>

    <!-- Destinos más recientes -->
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customers-dashboard.last-five-destinations" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.last-five-destinations" /></th>
            <td><acme:print value="${theLastFiveDestinations}" /></td>
        </tr>
    </table>

    <!-- Dinero gastado en el último año -->
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customers-dashboard.money-spent-last-year" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.money-spent" /></th>
            <td><acme:print value="${moneySpentInBookingDuringLastYear}" /></td>
        </tr>
    </table>

    <!-- Número de viajes por clase -->
    <h3 class="mt-4 fw-bold">
        <acme:print code="customer.customers-dashboard.numBYTravelClass" />
    </h3>
    <table class="table table-bordered mx-auto">
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.totalNumTravelClassEconomy" /></th>
            <td><acme:print value="${totalNumTravelClassEconomy}" /></td>
        </tr>
        <tr>
            <th><acme:print code="customer.customers-dashboard.label.totalNumTravelClassBusiness" /></th>
            <td><acme:print value="${totalNumTravelClassBusiness}" /></td>
        </tr>
    </table>

    <div class="mt-4">
        <acme:return />
    </div>
</div>
