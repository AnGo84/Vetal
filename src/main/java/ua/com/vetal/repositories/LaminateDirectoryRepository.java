package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.LaminateDirectory;

@Repository
public interface LaminateDirectoryRepository extends JpaRepository<LaminateDirectory, Long> {

	LaminateDirectory findByName(String name);

}
