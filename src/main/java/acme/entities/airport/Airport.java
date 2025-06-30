
package acme.entities.airport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidEmailOptional;
import acme.constraints.ValidIATAcode;
import acme.constraints.ValidPhoneNumberOptional;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(indexes = {
	@Index(columnList = "codigo")
})
public class Airport extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidShortText
	private String				name;

	@Mandatory
	@Automapped
	@ValidIATAcode
	@Column(unique = true)
	private String				codigo;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private OperationalScope	operationalScope;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				city;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				country;

	@Optional
	@Automapped
	@ValidUrl
	private String				website;

	@Optional
	@Automapped
	@ValidEmailOptional
	private String				email;

	@Optional
	@Automapped
	@ValidPhoneNumberOptional
	private String				phoneNumber;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
