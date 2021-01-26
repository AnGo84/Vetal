package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Kraskoottisk;

@Repository
public interface KraskoottiskRepository extends JpaRepository<Kraskoottisk, Double> {
}
