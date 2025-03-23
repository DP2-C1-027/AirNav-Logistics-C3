
package acme.features.any.reviews;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.review.Review;

@Repository
public interface AnyReviewRepository extends AbstractRepository {

	@Query("SELECT b FROM Review b WHERE b.id=:id")
	Review findReview(int id);

	@Query("SELECT a FROM Review a")
	Collection<Review> findAllReview();

	@Query("SELECT r FROM Review r WHERE YEAR(r.moment) = :year")
	Collection<Review> findReviewsByYear(int year);

}
