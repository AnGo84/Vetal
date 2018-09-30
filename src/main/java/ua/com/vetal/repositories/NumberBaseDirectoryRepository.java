package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.entity.NumberBaseDirectory;

@Repository
public interface NumberBaseDirectoryRepository extends JpaRepository<NumberBaseDirectory, Long> {

	NumberBaseDirectory findByName(String name);

}
