/*
 * EmployerJobPublishService.java
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
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		AssistanceAgent assistance;
		boolean status;
		assistance = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealm(assistance);
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
	public void bind(final TrackingLog TrackingLog) {
		super.bindObject(TrackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "indicator");
	}

	@Override
	public void validate(final TrackingLog TrackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog TrackingLog) {
		TrackingLog.setDraftMode(false);
		this.repository.save(TrackingLog);
	}

	@Override
	public void unbind(final TrackingLog TrackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(TrackingLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "indicator");

		super.getResponse().addData(dataset);
	}
}
