
package acme.features.any.reviews;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.review.Review;

@Repository
public interface AnyReviewRepository extends AbstractRepository {

	@Query("SELECT b FROM Review b WHERE b.id=:id")
	Review findReview(int id);

	@Query("SELECT a FROM Review a")
	Collection<Review> findAllReview();

	@Query("SELECT COUNT(r) FROM Review r WHERE YEAR(r.moment) = :year")
	int countReviewsByYear(@Param("year") int year);

	@Query("SELECT r FROM Review r WHERE YEAR(r.moment) = :year ORDER BY r.moment DESC")
	Page<Review> findManyReviewsByYear(@Param("year") int year, Pageable pageable);

}
