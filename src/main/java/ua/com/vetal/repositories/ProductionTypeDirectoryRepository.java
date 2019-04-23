package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.ProductionTypeDirectory;

@Repository
public interface ProductionTypeDirectoryRepository extends JpaRepository<ProductionTypeDirectory, Long> {

    ProductionTypeDirectory findByName(String name);

}
