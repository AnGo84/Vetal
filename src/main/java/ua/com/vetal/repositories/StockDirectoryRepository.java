package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.vetal.entity.StockDirectory;

@Repository
public interface StockDirectoryRepository extends JpaRepository<StockDirectory, Long> {

	StockDirectory findByName(String name);

}
