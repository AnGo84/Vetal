package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Kraskoottisk;

/**
 * Get kraskoottisk amount from view in DB
 *
 * @return List of Kraskoottisk
 * @deprecated This class replaced with storage procedure.
 * <p> Use {@link StencilRepository#getCurrentKrascoottiskAmount()} instead.
 */
@Deprecated
@Repository
public interface KraskoottiskRepository extends JpaRepository<Kraskoottisk, Double> {
}
