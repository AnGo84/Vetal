package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.ContractorDAO;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.filter.PersonViewFilter;
import ua.com.vetal.repositories.ContractorRepository;

import java.util.List;

@Service("contractorService")
@Transactional
public class ContractorServiceImpl implements SimpleService<Contractor> {

    @Autowired
    private ContractorDAO contractorDAO;

    @Autowired
    private ContractorRepository personRepository;

    @Override
    public Contractor findById(Long id) {
        return personRepository.getOne(id);
    }

    @Override
    public Contractor findByName(String name) {
        return personRepository.findByCorpName(name);
    }

    public Contractor findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    public void saveObject(Contractor person) {
        personRepository.save(person);
    }

    @Override
    public void updateObject(Contractor person) {
        saveObject(person);
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<Contractor> findAllObjects() {
        return personRepository.findAll();
    }

    @Override
    public boolean isObjectExist(Contractor person) {
        return findById(person.getId()) != null;
    }

    public boolean isCorpNameExist(Contractor person) {
        return findByName(person.getCorpName()) != null;
    }

    public List<Contractor> findByFilterData(PersonViewFilter filterData) {
        List<Contractor> list = contractorDAO.findByFilterData(filterData);

        if (filterData == null) {
            return findAllObjects();
        }
        return list;
    }
}
