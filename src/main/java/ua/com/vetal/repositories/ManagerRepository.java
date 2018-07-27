package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

	// Manager findByName(String name);

}
