
package acme.features.administrator.maintanenceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintanenceRecords.MaintanenceRecord;

@GuiController
public class AdministratorRecordController extends AbstractGuiController<Administrator, MaintanenceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRecordListService	listService;

	@Autowired
	private AdministratorRecordShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

	}
}
