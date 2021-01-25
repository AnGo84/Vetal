package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.PrintingUnitDirectory;
import ua.com.vetal.repositories.common.CommonDirectoryRepository;

@Repository
public interface PrintingUnitDirectoryRepository extends CommonDirectoryRepository<PrintingUnitDirectory> {

}
/*public interface PrintingUnitDirectoryRepository extends JpaRepository<PrintingUnitDirectory, Long> {

    PrintingUnitDirectory findByName(String name);

}*/
