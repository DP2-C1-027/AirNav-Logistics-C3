
package acme.features.any.reviews;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.review.Review;

@GuiService
public class AnyReviewShowService extends AbstractGuiService<Any, Review> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyReviewRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {

		boolean isAuthorised = false;

		if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData("id", Integer.class) != null) {
			Integer reviewId = super.getRequest().getData("id", Integer.class);
			Review review = this.repository.findReview(reviewId);
			isAuthorised = review != null;
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		Review review;
		int id;

		id = super.getRequest().getData("id", int.class);
		review = this.repository.findReview(id);

		super.getBuffer().addData(review);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "name", "moment", "subject", "text", "score", "recommended");

		super.getResponse().addData(dataset);
	}
}
