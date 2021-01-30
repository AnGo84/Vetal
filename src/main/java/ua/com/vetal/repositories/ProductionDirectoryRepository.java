package ua.com.vetal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.ProductionDirectory;

@Repository
public interface ProductionDirectoryRepository extends JpaRepository<ProductionDirectory, Long> {
    ProductionDirectory findByFullName(String name);
}
