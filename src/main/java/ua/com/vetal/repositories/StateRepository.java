package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    State findByName(String name);
}
