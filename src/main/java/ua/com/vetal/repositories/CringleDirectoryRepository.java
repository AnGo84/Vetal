package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.CringleDirectory;

@Repository
public interface CringleDirectoryRepository extends JpaRepository<CringleDirectory, Long> {

	CringleDirectory findByName(String name);

}
