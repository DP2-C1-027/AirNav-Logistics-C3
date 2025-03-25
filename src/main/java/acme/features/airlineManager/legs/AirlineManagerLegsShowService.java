
package acme.features.airlineManager.legs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegsShowService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegsRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Leg leg;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);
		//status = leg != null && !leg.isDraftMode();

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status", "departureAirport", "arrivalAirport", "aircraft", "flight");

		super.getResponse().addData(dataset);
	}

}
