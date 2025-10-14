/*
 * AssistanceAgentTrackingLogListService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListByClaimService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status = true;

		if (super.getRequest().hasData("claimId")) {
			AssistanceAgent assistance = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
			Integer claimId;
			String isInteger;
			isInteger = super.getRequest().getData("claimId", String.class);
			if (isInteger != null && !isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				claimId = Integer.valueOf(isInteger);
			else
				claimId = Integer.valueOf(-1);

			if (claimId == null || claimId.equals(Integer.valueOf(0)) || !claimId.equals(Integer.valueOf(0)) && this.repository.findOneClaimById(claimId) == null)
				status = false;
			else
				try {
					Claim claim = this.repository.findOneClaimById(claimId);

					status = assistance == null ? false : claim.getRegisteredBy().equals(assistance) || !claim.isDraftMode();

				} catch (NumberFormatException e) {
					status = false;
				}

		} else
			status = false;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLog;

		int id;

		id = super.getRequest().getData("claimId", int.class);
		trackingLog = this.repository.findTrackingLogsByClaimId(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		int id = super.getRequest().getData("claimId", int.class);

		dataset = super.unbindObject(trackingLog, "stepUndergoing", "resolutionPercentage", "indicator", "claim");
		dataset.put("claim", id);

		super.addPayload(dataset, trackingLog, "stepUndergoing", "resolutionPercentage", "indicator", "claim");

		super.getResponse().addData(dataset);
	}

}
