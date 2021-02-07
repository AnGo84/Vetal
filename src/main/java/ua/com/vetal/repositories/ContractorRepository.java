package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.repositories.common.CommonContragentRepository;

@Repository
public interface ContractorRepository extends CommonContragentRepository<Contractor> {

}
