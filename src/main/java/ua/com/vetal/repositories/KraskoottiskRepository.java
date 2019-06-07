package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Kraskoottisk;
import ua.com.vetal.entity.Link;

import java.util.List;

@Repository
public interface KraskoottiskRepository extends JpaRepository<Kraskoottisk, Double> {

}
