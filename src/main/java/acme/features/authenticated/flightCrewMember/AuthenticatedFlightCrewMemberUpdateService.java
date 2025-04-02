/*
 * AuthenticatedFlightCrewMemberUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.flightCrewMember;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.AvailabilityStatus;
import acme.realms.FlightCrewMember;

@GuiService
public class AuthenticatedFlightCrewMemberUpdateService extends AbstractGuiService<Authenticated, FlightCrewMember> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedFlightCrewMemberRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberByUserAccountId(userAccountId);

		super.getBuffer().addData(flightCrewMember);
	}

	@Override
	public void bind(final FlightCrewMember flightCrewMember) {

		super.bindObject(flightCrewMember, "codigo", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");
	}

	@Override
	public void validate(final FlightCrewMember flightCrewMember) {

		String cod = flightCrewMember.getCodigo();
		Collection<FlightCrewMember> codigo = this.repository.findFlightCrewMemberCode(cod).stream().filter(x -> x.getId() != flightCrewMember.getId()).toList();

		if (!codigo.isEmpty())
			super.state(false, "codigo", "acme.validation.error.repeat-code");

	}

	@Override
	public void perform(final FlightCrewMember flightCrewMember) {
		this.repository.save(flightCrewMember);
	}

	@Override
	public void unbind(final FlightCrewMember flightCrewMember) {
		Dataset dataset = super.unbindObject(flightCrewMember, "codigo", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");

		// Status choices
		SelectChoices statusChoices = SelectChoices.from(AvailabilityStatus.class, flightCrewMember.getAvailabilityStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		// Airlines choices
		Collection<Airline> airlines = this.repository.finAllAirlines();
		SelectChoices legChoices = SelectChoices.from(airlines, "name", flightCrewMember.getAirline());
		dataset.put("airlineChoices", legChoices);
		dataset.put("airline", legChoices.getSelected().getKey());

		dataset.put("readOnly", true);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
