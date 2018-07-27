package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.FormatDirectory;

@Repository
public interface FormatDirectoryRepository extends JpaRepository<FormatDirectory, Long> {

	FormatDirectory findByName(String name);

}
