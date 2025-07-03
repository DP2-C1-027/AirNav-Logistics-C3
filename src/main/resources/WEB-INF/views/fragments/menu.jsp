<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="http://www.example.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.nico" action="https://www.albumoftheyear.org//"/>
			<acme:menu-suboption code="master.menu.anonymous.mannizcob" action="https://refactoring.guru/"/>
			<acme:menu-suboption code="master.menu.anonymous.norapfr" action="https://clubalgoritmiaus.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.HHV4884-ETSII" action="https://astralshiftpro.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.jose" action="https://codeforces.com/problemset"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.any">
			<acme:menu-suboption code="master.menu.any.airport" action="/any/airport/list"/>
			<acme:menu-suboption code="master.menu.any.review" action="/any/review/list"/>
			<acme:menu-suboption code="master.menu.any.list-flight-assignments-published" action="/any/flight-assignment/list" />
			<acme:menu-suboption code="master.menu.any.service" action="/any/service/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.show-administrator-dashboard" action="/administrator/administrator-dashboard/show" />
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-suboption code="master.menu.administrator.system-configuration" action="/administrator/system-configuration/show" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-booking" action="/administrator/booking/list"/>
			<acme:menu-separator/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-record" action="/administrator/maintanence-record/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.claim" action="/administrator/claim/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list-airline" action="/administrator/airline/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-airport" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-aircraft" action="/administrator/aircraft/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.pruebapruebaprueba" action="/administrator/service/list"/>		
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
			
			<acme:menu-suboption code="master.menu.airlineManager.prueba" action="/administrator/flight/list" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.airlineManager" access="hasRealm('AirlineManager')">
			<acme:menu-suboption code="master.menu.airlineManager.list-my-flights" action="/airline-manager/flight/list" />
			<acme:menu-suboption code="master.menu.airlineManager.list-my-legs" action="/airline-manager/leg/list" />
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.assistanceAgent" access="hasRealm('AssistanceAgent')">
					<acme:menu-suboption code="master.menu.assistanceAgent.list-my-claims" action="/assistance-agent/claim/listCompleted" />
					<acme:menu-suboption code="master.menu.assistanceAgent.list-my-claimsUndergoing" action="/assistance-agent/claim/listUndergoing" />
					<acme:menu-suboption code="master.menu.assistanceAgent.list-my-trackingLogs" action="/assistance-agent/tracking-log/list" />
					<acme:menu-suboption code="master.menu.assistanceAgent.show-dashboard" action="/assistance-agent/assistance-agent-dashboard/show"/>


		</acme:menu-option>
		
		<acme:menu-option code="master.menu.flightCrewMember" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flightCrewMember.list-flight-assignments-planned" action="/flight-crew-member/flight-assignment/list-planned" />
			<acme:menu-suboption code="master.menu.flightCrewMember.list-flight-assignments-completed" action="/flight-crew-member/flight-assignment/list-completed" />
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.customers" access="hasRealm('Customers')">
			<acme:menu-suboption code="master.menu.customers.list-my-bookings" action="/customers/booking/list"/>
			<acme:menu-suboption code="master.menu.customers.show-dashboard" action="/customers/customers-dashboards/show"/>
			<acme:menu-suboption code="master.menu.customers.list-my-passengers" action="/customers/passenger/list"/>
		
		</acme:menu-option>
			<acme:menu-option code="master.menu.technicians" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technicians.list-my-maintenance-records-published" action="/technician/maintanence-record/publishList"/>
			<acme:menu-suboption code="master.menu.technicians.list-my-maintenance-records" action="/technician/maintanence-record/list"/>
			<acme:menu-suboption code="master.menu.technicians.list-my-tasks-published" action="/technician/task/publishList"/>
			<acme:menu-suboption code="master.menu.technicians.list-my-tasks" action="/technician/task/list"/>
			<acme:menu-suboption code="master.menu.technicians.list-my-involved-in" action="/technician/involved-in/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.dashboards" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.dashboards.flight-crew-member-dashboard" action="/flight-crew-member/flight-crew-member-dashboard/show"/>
		</acme:menu-option>
		
	</acme:menu-left>
	

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-customers" action="/authenticated/customers/create" access="!hasRealm('Customers')"/>
			<acme:menu-suboption code="master.menu.user-account.customers-profile" action="/authenticated/customers/update" access="hasRealm('Customers')"/>
			<acme:menu-suboption code="master.menu.user-account.become-flight-crew-member" action="/authenticated/flight-crew-member/create" access="!hasRealm('FlightCrewMember')"/>
			<acme:menu-suboption code="master.menu.user-account.flight-crew-member-profile" action="/authenticated/flight-crew-member/update" access="hasRealm('FlightCrewMember')"/>
			<acme:menu-suboption code="master.menu.user-account.become-airline-manager" action="/authenticated/airline-manager/create" access="!hasRealm('AirlineManager')"/>
			<acme:menu-suboption code="master.menu.user-account.airline-manager-profile" action="/authenticated/airline-manager/update" access="hasRealm('AirlineManager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-technician" action="/authenticated/technician/create" access="!hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.technician-profile" action="/authenticated/technician/update" access="hasRealm('Technician')"/>

			<acme:menu-suboption code="master.menu.user-account.become-assistance-agent" action="/authenticated/assistance-agent/create" access="!hasRealm('assistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.assistance-agent-profile" action="/authenticated/assistance-agent/update" access="hasRealm('assistanceAgent')"/>

		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>
