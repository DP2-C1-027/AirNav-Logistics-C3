
package acme.features.any.reviews;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.review.Review;

@GuiService
public class AnyReviewListService extends AbstractGuiService<Any, Review> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyReviewRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		// Obtenemos el año actual
		Date moment = MomentHelper.getCurrentMoment();
		LocalDate localDateMoment = moment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int currentYear = localDateMoment.getYear();

		// Parámetros de paginación (con valores por defecto)
		int page = super.getRequest().getData("page", int.class, 0);
		int size = super.getRequest().getData("size", int.class, 100);

		Pageable pageable = PageRequest.of(page, size);
		Page<Review> reviewsPage = this.repository.findManyReviewsByYear(currentYear, pageable);

		super.getBuffer().addData(reviewsPage.getContent());

	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "name", "moment", "subject");

		super.getResponse().addData(dataset);

	}
}
