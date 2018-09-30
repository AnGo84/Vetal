package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.entity.PrintingUnitDirectory;

@Repository
public interface PrintingUnitDirectoryRepository extends JpaRepository<PrintingUnitDirectory, Long> {

	PrintingUnitDirectory findByName(String name);

}
