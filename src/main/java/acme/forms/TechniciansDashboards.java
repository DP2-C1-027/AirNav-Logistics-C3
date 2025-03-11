
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintanenceRecords.MaintanenceRecord;
import acme.entities.maintanenceRecords.StatusMaintanenceRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechniciansDashboards extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long						serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Map<StatusMaintanenceRecord, Integer>	numberOfMaintenanceRecordsGroupedByStatus;
	private MaintanenceRecord						nearestInspectionDueMaintenanceRecord;
	private List<Aircraft>							topFiveAircraftsWithMostMaintenanceTasks;

	private Statistics								estimatedCostOfMaintenanceRecordsLastYear;
	private Statistics								estimatedDurationOfTasksInvolved;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
