
package acme.entities.courses;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
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
	String						name;

	@Mandatory
	@ValidLongText
	String						description;

	@Mandatory
	@ValidShortText
	String						courseCategory;

	@Mandatory
	@Valid
	Integer						courseDuration;

	@Mandatory
	@ValidShortText
	String						courseInstructor;

	@Mandatory
	@Valid
	Money						coursePrice;

	@Mandatory
	@ValidShortText
	String						language;

	@Mandatory
	@Future
	@Valid
	Date						courseStartDate;

	@Mandatory
	@Valid
	@Future
	Date						courseEndDate;

	@Mandatory
	@Valid
	Boolean						certification;

	@Mandatory
	@Valid
	@Min(1)
	@Max(500)
	Integer						numberInscriptions;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
