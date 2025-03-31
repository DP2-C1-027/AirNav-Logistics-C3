
package acme.features.technician.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintanenceRecords.Task;
import acme.realms.Technician;

@GuiController
public class TechnicianTaskController extends AbstractGuiController<Technician, Task> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskListService		listService;

	@Autowired
	private TechnicianTaskShowService		showService;

	@Autowired
	private TechnicianTaskCreateService		createService;

	@Autowired
	private TechnicianTaskCreateService2	createService2;

	@Autowired
	private TechnicianRecordTaskListService	taskListService;

	@Autowired
	private TechnicianTaskUpdateService		updateService;

	@Autowired
	private TechnicianTaskPublishService	publishService;

	@Autowired
	private TechnicianTaskDeleteService		deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("taskList", "list", this.taskListService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("createRecord", "create", this.createService2);
	}
}
