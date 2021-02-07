package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.ContractorDAO;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.repositories.ContractorRepository;
import ua.com.vetal.service.common.AbstractContragentService;

@Service("contractorService")
@Transactional
public class ContractorServiceImpl extends AbstractContragentService<Contractor, ContractorRepository> {

	public ContractorServiceImpl(ContractorRepository repository, ContractorDAO contragentDAO) {
		super(repository, contragentDAO);
	}
}