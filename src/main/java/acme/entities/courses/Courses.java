
package acme.entities.courses;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Courses extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	String						name;

	@Mandatory
	@ValidLongText
	@Automapped
	String						description;

	@Mandatory
	@ValidShortText
	@Automapped
	String						courseCategory;

	@Mandatory
	@Valid
	@Automapped
	Integer						courseDuration;

	@Mandatory
	@ValidShortText
	@Automapped
	String						courseInstructor;

	@Mandatory
	@Valid
	@Automapped
	Money						coursePrice;

	@Mandatory
	@ValidShortText
	@Automapped
	String						language;

	@Mandatory
	@Future
	@Valid
	@Automapped
	Date						courseStartDate;

	@Mandatory
	@Valid
	@Future
	@Automapped
	Date						courseEndDate;

	@Mandatory
	@Valid
	@Automapped
	Boolean						certification;

	@Mandatory
	@Valid
	@Min(1)
	@Max(500)
	@Automapped
	Integer						numberInscriptions;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
