package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.NumberBaseDirectory;
import ua.com.vetal.repositories.common.CommonDirectoryRepository;

@Repository
public interface NumberBaseDirectoryRepository extends CommonDirectoryRepository<NumberBaseDirectory> {

}
/*public interface NumberBaseDirectoryRepository extends JpaRepository<NumberBaseDirectory, Long> {
    NumberBaseDirectory findByName(String name);
}*/
