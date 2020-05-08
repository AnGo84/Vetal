package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.repositories.common.CommonDirectoryRepository;

@Repository
public interface ChromaticityDirectoryRepository extends CommonDirectoryRepository<ChromaticityDirectory> {
    //public interface ChromaticityDirectoryRepository extends JpaRepository<ChromaticityDirectory, Long> {
    //ChromaticityDirectory findByName(String name);

}
