package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.PaperDirectory;
import ua.com.vetal.repositories.common.CommonDirectoryRepository;

@Repository
public interface PaperDirectoryRepository extends CommonDirectoryRepository<PaperDirectory> {

}
