package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.PaperDirectory;

@Repository
public interface PaperDirectoryRepository extends JpaRepository<PaperDirectory, Long> {

	PaperDirectory findByName(String name);

}
