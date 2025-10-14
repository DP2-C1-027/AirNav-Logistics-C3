/*
 * AssistanceAgentTrackingLogShowService.java
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

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.Indicator;
import acme.entities.claims.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = true;
		TrackingLog trackingLog;
		AssistanceAgent assistance;
		Claim claim;
		if (super.getRequest().hasData("id")) {
			Integer trackingLogId;
			String isInteger;
			isInteger = super.getRequest().getData("id", String.class).trim();
			if (!isInteger.isBlank() && isInteger.chars().allMatch((e) -> e > 47 && e < 58))
				trackingLogId = Integer.valueOf(isInteger);
			else
				trackingLogId = Integer.valueOf(-1);

			trackingLog = !trackingLogId.equals(Integer.valueOf(-1)) ? this.repository.findOneTrackingLogById(trackingLogId) : null;
			claim = trackingLog != null ? this.repository.findClaimByTrackingLogId(trackingLogId) : null;
			assistance = claim != null ? claim.getRegisteredBy() : null;
			status = assistance == null ? false : !trackingLog.isDraftMode() || super.getRequest().getPrincipal().hasRealm(assistance);

		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog TrackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		TrackingLog = this.repository.findOneTrackingLogById(id);

		super.getBuffer().addData(TrackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "creationMoment", "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "indicator", "draftMode", "claim");

		dataset.put("claimId", trackingLog.getClaim().getId());

		SelectChoices statusChoices = SelectChoices.from(Indicator.class, trackingLog.getIndicator());
		dataset.put("indicator", statusChoices);

		super.getResponse().addData(dataset);
	}

}
