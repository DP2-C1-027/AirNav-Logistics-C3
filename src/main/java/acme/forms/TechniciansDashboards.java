
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.StatusMaintanenceRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechniciansDashboards extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long				serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Map<StatusMaintanenceRecord, Integer>	numberOfMaintenanceRecordsGroupedByStatus;
	MaintanenceRecord						nearestInspectionDueMaintenanceRecord;
	List<Aircraft>							topFiveAircraftsWithMostMaintenanceTasks;
	Double									averageEstimatedCostOfMaintenanceRecordsLastYear;
	Double									minEstimatedCostOfMaintenanceRecordsLastYear;
	Double									maxEstimatedCostOfMaintenanceRecordsLastYear;
	Double									standardDeviationEstimatedCostOfMaintenanceRecordsLastYear;
	Double									averageEstimatedDurationOfTasksInvolved;
	Double									minEstimatedDurationOfTasksInvolved;
	Double									maxEstimatedDurationOfTasksInvolved;
	Double									standardDeviationEstimatedDurationOfTasksInvolved;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
