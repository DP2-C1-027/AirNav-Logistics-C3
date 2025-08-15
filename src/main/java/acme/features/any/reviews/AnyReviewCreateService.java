
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
		boolean isAuthorised = false;

		if (super.getRequest().getMethod().equals("GET"))
			isAuthorised = true;

		// Only is allowed to create a review if post method include a valid review.
		if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", Integer.class) != null)
			isAuthorised = super.getRequest().getData("id", Integer.class).equals(0);

		super.getResponse().setAuthorised(isAuthorised);
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
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Review review) {
		this.repository.save(review);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "name", "moment", "subject", "text", "score", "recommended");
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}

}
