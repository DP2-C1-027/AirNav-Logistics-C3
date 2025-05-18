/*
 * AssistanceAgentClaimUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.claims;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		AssistanceAgent assistance = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		if (super.getRequest().getPrincipal().hasRealm(assistance))
			if (super.getRequest().hasData("id"))
				try {
					int claimId = super.getRequest().getData("id", int.class);

					if (claimId != 0) {
						Claim claim = this.repository.findOneClaimById(claimId);

						status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assistance);
					}
				} catch (Exception e) {
					status = false;
				}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findOneClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "indicator", "linkedTo");

	}

	@Override
	public void validate(final Claim claim) {
		if (!super.getBuffer().getErrors().hasErrors("linkedTo"))
			if (claim.getId() != 0) {
				Collection<Leg> validLegs = this.repository.findCompletedLegsByClaimId(claim.getId());
				boolean isValidLeg = claim.getLinkedTo() != null && validLegs.stream().anyMatch(leg -> Integer.valueOf(leg.getId()).equals(claim.getLinkedTo().getId()));
				super.state(isValidLeg, "linkedTo", "assistance-agent.claim.form.error.invalid-leg");
			}
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> legs;
		legs = this.repository.findCompletedLegsByClaimId(claim.getId());

		Dataset dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "indicator", "type", "linkedTo");
		SelectChoices statusChoices = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLinkedTo());
		dataset.put("linkedTo", legsChoices);
		dataset.put("typeChoice", statusChoices);

		super.getResponse().addData(dataset);
	}

}
