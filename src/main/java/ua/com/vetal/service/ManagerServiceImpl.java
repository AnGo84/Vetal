package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.repositories.ManagerRepository;
import ua.com.vetal.service.common.AbstractEmployeeService;

@Service("managerService")
@Transactional
public class ManagerServiceImpl extends AbstractEmployeeService<Manager, ManagerRepository> {

    public ManagerServiceImpl(ManagerRepository repository) {
        super(repository);
    }
}
/*public class ManagerServiceImpl implements SimpleService<Manager> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ManagerRepository personRepository;

    @Override
    public Manager findById(Long id) {
        *//*
 * Optional<User> optinalEntity = userRepository.findById(id); User user
 * = optinalEntity.get(); return user;
 *//*
        return personRepository.getOne(id);
    }

    @Override
    public Manager findByName(String name) {
        // return directoryRepository.findByName(name);
        return null;
    }

    @Override
    public void saveObject(Manager person) {
        personRepository.save(person);
    }

    @Override
    public void updateObject(Manager person) {
        saveObject(person);
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<Manager> findAllObjects() {
        return personRepository.findAll();
    }

    @Override
    public boolean isObjectExist(Manager person) {
        return findById(person.getId()) != null;
    }

}*/
