
package acme.features.airlineManager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegsListService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int airlineManagerId;

		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		legs = this.repository.findLegsByAirlineManagerId(airlineManagerId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		dataset.put("departureAirport", leg.getDepartureAirport().getCodigo());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getCodigo());
		dataset.put("flight", leg.getFlight().getTag());
		super.addPayload(dataset, leg);

		super.getResponse().addData(dataset);
	}

}
