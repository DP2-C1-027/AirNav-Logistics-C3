
package acme.entities.systemConfiguration;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SystemConfiguration extends AbstractEntity {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	private String				systemCurrency		= "EUR";

	@Mandatory
	@Automapped
	@ElementCollection(fetch = FetchType.EAGER)  // Permite mapear listas simples
	@CollectionTable(name = "accepted_currencies")  // Nombre de la tabla intermedia
	@Column(name = "currency")  // Nombre de la columna en la tabla intermedia
	private List<String>		acceptedCurrencies	= List.of("EUR", "USD", "GBP");

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
