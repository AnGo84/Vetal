package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.State;
import ua.com.vetal.repositories.StateRepository;

import java.util.List;

@Service("stateService")
@Transactional
public class StateServiceImpl implements SimpleService<State> {

    @Autowired
    private StateRepository stareRepository;

    @Override
    public State findById(Long id) {
        return stareRepository.getOne(id);
    }

    @Override
    public State findByName(String name) {
        return stareRepository.findByName(name);
    }

    @Override
    public void saveObject(State object) {
        stareRepository.save(object);
    }

    @Override
    public void updateObject(State object) {
        saveObject(object);
    }

    @Override
    public void deleteById(Long id) {
        stareRepository.deleteById(id);
    }

    @Override
    public List<State> findAllObjects() {
        return stareRepository.findAll();
    }

    @Override
    public boolean isObjectExist(State object) {
        return findByName(object.getName()) != null;
    }
}
