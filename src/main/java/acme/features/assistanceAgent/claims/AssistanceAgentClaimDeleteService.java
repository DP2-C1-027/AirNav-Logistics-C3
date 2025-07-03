/*
 * AssistanceAgentClaimDeleteService.java
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
import acme.entities.claims.TrackingLog;
import acme.entities.legs.Leg;
import acme.features.assistanceAgent.trackingLogs.AssistanceAgentTrackingLogRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository			repository;

	@Autowired
	private AssistanceAgentTrackingLogRepository	repositoryTL;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status = true;

		if (super.getRequest().hasData("id")) {
			AssistanceAgent assistance = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
			Integer claimId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class);
			if (isInteger != null && !isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				claimId = Integer.valueOf(isInteger);
			else
				claimId = Integer.valueOf(-1);

			if (claimId == null || claimId.equals(Integer.valueOf(0)) || !claimId.equals(Integer.valueOf(0)) && this.repository.findOneClaimById(claimId) == null)
				status = false;
			else
				try {
					Claim claim = this.repository.findOneClaimById(claimId);

					status = claim.getRegisteredBy().equals(assistance) && claim.isDraftMode();

				} catch (NumberFormatException e) {
					status = false;
				}

		} else
			status = false;

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
		super.bindObject(claim, "passengerEmail", "description", "linkedTo");

	}

	@Override
	public void validate(final Claim claim) {
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(claim.isDraftMode(), "draftMode", "claim.form.error.draft-mode");

	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> tl;
		tl = this.repository.findTrackingLogsByClaimId(claim.getId());
		this.repositoryTL.deleteAll(tl);
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Collection<Leg> legs;
		legs = this.repository.findCompletedLegsByRegistrationMoment(claim.getRegistrationMoment());
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "indicator", "type");
		SelectChoices legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLinkedTo());
		SelectChoices statusChoices = SelectChoices.from(ClaimType.class, claim.getType());
		dataset.put("typeChoice", statusChoices);
		dataset.put("linkedTo", legsChoices);

		super.getResponse().addData(dataset);
	}

}
