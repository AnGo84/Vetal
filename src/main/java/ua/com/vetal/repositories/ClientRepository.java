package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Client;
import ua.com.vetal.repositories.common.CommonContragentRepository;

@Repository
public interface ClientRepository extends CommonContragentRepository<Client> {

}
