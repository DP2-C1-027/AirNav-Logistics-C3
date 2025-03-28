
package acme.features.technician.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.realms.Technician;

@GuiController
public class TechnicianMaintenenceRecordController extends AbstractGuiController<Technician, MaintanenceRecord> {

	@Autowired
	private TechnicianMaintenenceRecordServiceShow	showService;

	@Autowired
	private TechnicianMaintenenceRecordServiceList	listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
