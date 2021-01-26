package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Printer;
import ua.com.vetal.repositories.common.CommonRepository;

@Repository
public interface PrinterRepository extends CommonRepository<Printer> {

}
