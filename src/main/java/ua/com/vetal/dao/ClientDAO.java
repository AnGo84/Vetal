package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Client;

@Repository
@Transactional(readOnly = true)
public class ClientDAO extends AbstractContragentDAO<Client> {
    public ClientDAO() {
        super(Client.class);
    }
}
