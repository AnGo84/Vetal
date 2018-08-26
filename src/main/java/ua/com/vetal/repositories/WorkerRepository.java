package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Printer;
import ua.com.vetal.entity.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

	// Manager findByName(String name);

}
