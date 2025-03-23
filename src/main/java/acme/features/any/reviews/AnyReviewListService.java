
package acme.features.any.reviews;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
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
		Collection<Review> anouncements;

		anouncements = this.repository.findReviewsByYear(2025);

		super.getBuffer().addData(anouncements);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "name", "moment", "subject");

		super.getResponse().addData(dataset);

	}
}
