package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.repositories.common.CommonDirectoryRepository;

@Repository
public interface FormatDirectoryRepository extends CommonDirectoryRepository<FormatDirectory> {

}
/*
public interface FormatDirectoryRepository extends JpaRepository<FormatDirectory, Long> {
    FormatDirectory findByName(String name);
}
*/
