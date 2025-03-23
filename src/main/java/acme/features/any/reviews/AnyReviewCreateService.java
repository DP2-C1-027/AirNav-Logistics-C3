
package acme.features.any.reviews;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.review.Review;

@GuiService
public class AnyReviewCreateService extends AbstractGuiService<Any, Review> {

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
		Review review;

		review = new Review();

		super.getBuffer().addData(review);
	}

	@Override
	public void bind(final Review review) {

		super.bindObject(review, "name", "moment", "subject", "text", "score", "recommended");
	}

	@Override
	public void validate(final Review review) {
		;
	}

	@Override
	public void perform(final Review review) {
		this.repository.save(review);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "name", "moment", "subject", "text", "score", "recommended");

		super.getResponse().addData(dataset);
	}

}
