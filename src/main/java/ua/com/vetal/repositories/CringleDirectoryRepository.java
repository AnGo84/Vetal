package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.CringleDirectory;
import ua.com.vetal.repositories.common.CommonDirectoryRepository;

@Repository
public interface CringleDirectoryRepository extends CommonDirectoryRepository<CringleDirectory> {

}
/*public interface CringleDirectoryRepository extends JpaRepository<CringleDirectory, Long> {
    CringleDirectory findByName(String name);
}*/
