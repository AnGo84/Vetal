package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.Printer;

@Repository
public interface PrinterRepository extends JpaRepository<Printer, Long> {

    // Manager findByName(String name);

}
