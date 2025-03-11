
package acme.features.assistanceagents.flightstatus;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.FlightStatus;

@GuiService
public class AssistanceAgentsFlightStatusService extends AbstractGuiService<Any, FlightStatus> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightStatusRepository repository; // Repositorio para FlightStatus

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true); // Autorizamos siempre
	}

	@Override
	public void load() {
		Collection<FlightStatus> flightStatuses;

		flightStatuses = null;
		// aqui se hace la llamada a la otra api y se mete los datos aqui
		super.getBuffer().addData(flightStatuses);
	}

	@Override
	public void unbind(final FlightStatus flightStatus) {
		Dataset dataset;

		dataset = super.unbindObject(flightStatus, "flightName", "status");
		super.addPayload(dataset, flightStatus, //
			"description", "lastUpdated");

		super.getResponse().addData(dataset);
	}
}
