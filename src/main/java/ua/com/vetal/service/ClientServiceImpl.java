package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.ClientDAO;
import ua.com.vetal.entity.Client;
import ua.com.vetal.repositories.ClientRepository;
import ua.com.vetal.service.common.AbstractContragentService;

@Service("clientService")
@Transactional
public class ClientServiceImpl extends AbstractContragentService<Client, ClientRepository> {

	public ClientServiceImpl(ClientRepository repository, ClientDAO contragentDAO) {
		super(repository, contragentDAO);
	}
}
