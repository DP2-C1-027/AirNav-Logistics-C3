
package acme.entities.recommendations;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidLongText;
import acme.constraints.ValidLongTextOptional;
import acme.constraints.ValidShortTextOptional;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Recommendation extends AbstractEntity {

	//Foursquare Places API

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Mandatory
	@Automapped
	@ValidLongText
	private String				type;       // Tipo de recomendación 

	@Automapped
	@Mandatory
	@ValidLongText
	private String				name;       // Nombre de la recomendación

	@Automapped
	@Mandatory
	@ValidLongText
	private String				description; // Descripción de la recomendación

	@Optional
	@ValidLongTextOptional
	private String				address;    // Dirección

	@Automapped
	@Optional
	@ValidShortTextOptional
	private String				category;   // Categoría de la recomendación

	@Automapped
	@Mandatory
	@Min(-90)
	@Max(90)
	private double				latitude;   // Latitud

	@Automapped
	@Mandatory
	@Min(-180)
	@Max(180)
	private double				longitude;  // Longitud

	@Automapped
	@ValidUrl
	@Optional
	private String				url;        // URL de más información

	@Automapped
	@Optional
	@Min(0)
	@Max(5)
	private double				rating;     // Puntuación 

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Valid
	@Automapped
	private Airport				airport;  // Relación con el aeropuerto

}
