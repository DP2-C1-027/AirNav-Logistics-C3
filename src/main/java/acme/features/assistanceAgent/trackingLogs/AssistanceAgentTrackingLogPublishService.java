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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
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
		boolean status;
		int jobId;
		Job job;
		TrackingLog employer;

		jobId = super.getRequest().getData("id", int.class);
		//job = this.repository.findJobById(jobId);
		//employer = job == null ? null : job.getEmployer();
		//status = job != null && job.isDraftMode() && super.getRequest().getPrincipal().hasRealm(employer);

		//super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Job job;
		int id;

		id = super.getRequest().getData("id", int.class);
		//job = this.repository.findJobById(id);

		//super.getBuffer().addData(job);
	}

	@Override
	public void bind(final TrackingLog job) {

	}

	@Override
	public void validate(final TrackingLog job) {
		;
	}

	@Override
	public void perform(final TrackingLog job) {

	}

	@Override
	public void unbind(final TrackingLog job) {
		int employerId;
		Collection<TrackingLog> contractors;
		SelectChoices choices;
		Dataset dataset;

		employerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		//contractors = this.repository.findContractorsByEmployerId(employerId);
		//choices = SelectChoices.from(contractors, "name", job.getContractor());

		dataset = super.unbindObject(job, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "indicator", "claim");
		//dataset.put("contractor", choices.getSelected().getKey());
		//dataset.put("contractors", choices);

		super.getResponse().addData(dataset);
	}

}
