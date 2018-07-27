package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.ClientDirectory;

@Repository
public interface ClientDirectoryRepository extends JpaRepository<ClientDirectory, Long> {

	ClientDirectory findByName(String name);

}
