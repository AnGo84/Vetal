package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.ClientDAO;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.filter.ClientViewFilter;
import ua.com.vetal.repositories.ClientRepository;

import java.util.List;

@Service("clientService")
@Transactional
public class ClientServiceImpl implements SimpleService<Client> {

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private ClientRepository directoryRepository;

    @Override
    public Client findById(Long id) {
        return directoryRepository.getOne(id);
    }

    @Override
    public Client findByName(String name) {
        return directoryRepository.findByFullName(name);
    }

    @Override
    public void saveObject(Client directory) {
        directoryRepository.save(directory);
    }

    @Override
    public void updateObject(Client directory) {
        saveObject(directory);
    }

    @Override
    public void deleteById(Long id) {
        directoryRepository.deleteById(id);
    }

    @Override
    public List<Client> findAllObjects() {
        return directoryRepository.findAll();
    }

    @Override
    public boolean isObjectExist(Client directory) {
        return findById(directory.getId()) != null;
    }
    public boolean isFullNameExist(Client directory) {
        return findByName(directory.getFullName()) != null;
    }

    public List<Client> findByFilterData(ClientViewFilter filterData) {
        if (filterData == null) {
            return findAllObjects();
        }

        List<Client> list = clientDAO.findByFilterData(filterData);
        return list;
    }
}
