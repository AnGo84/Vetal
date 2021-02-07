package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Contractor;

@Repository
@Transactional(readOnly = true)
public class ContractorDAO extends AbstractContragentDAO<Contractor> {

    public ContractorDAO() {
        super(Contractor.class);
    }
}
