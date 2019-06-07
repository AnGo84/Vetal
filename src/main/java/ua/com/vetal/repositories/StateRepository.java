package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.State;
import ua.com.vetal.entity.UserRole;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    State findByName(String name);

}
