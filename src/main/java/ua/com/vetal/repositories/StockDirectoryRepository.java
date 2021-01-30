package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.StockDirectory;
import ua.com.vetal.repositories.common.CommonDirectoryRepository;

@Repository
public interface StockDirectoryRepository extends CommonDirectoryRepository<StockDirectory> {

}
