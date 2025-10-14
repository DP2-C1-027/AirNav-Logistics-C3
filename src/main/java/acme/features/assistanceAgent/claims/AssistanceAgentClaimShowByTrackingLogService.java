/*
 * AssistanceAgentClaimShowService.java
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
public class AssistanceAgentClaimShowByTrackingLogService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;

		if (super.getRequest().hasData("trackingLogId")) {
			AssistanceAgent assistance = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
			Integer trackingLogId;
			String isInteger;
			isInteger = super.getRequest().getData("trackingLogId", String.class);
			if (isInteger != null && !isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				trackingLogId = Integer.valueOf(isInteger);
			else
				trackingLogId = Integer.valueOf(-1);

			if (trackingLogId == null || !trackingLogId.equals(Integer.valueOf(0)) && this.repository.findClaimByTrackingLogId(trackingLogId) == null)
				status = false;

			Claim claim = this.repository.findClaimByTrackingLogId(trackingLogId);
			if (claim == null)
				status = false;
			else if (status) {
				Claim claim2 = this.repository.findOneClaimById(claim.getId());
				if (claim2.isDraftMode() && !claim2.getRegisteredBy().equals(assistance))
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

		id = super.getRequest().getData("trackingLogId", int.class);
		claim = this.repository.findClaimByTrackingLogId(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> legs;
		legs = this.repository.findCompletedLegsByClaimId(claim.getId());
		Dataset dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "indicator", "type", "linkedTo", "draftMode");
		SelectChoices statusChoices = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLinkedTo());
		dataset.put("linkedTo", legsChoices);
		dataset.put("typeChoice", statusChoices);

		super.getResponse().addData(dataset);
	}

}
