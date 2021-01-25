package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.LaminateDirectory;
import ua.com.vetal.repositories.LaminateDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("laminateDirectoryService")
@Transactional
public class LaminateDirectoryServiceImpl extends AbstractDirectoryService<LaminateDirectory, LaminateDirectoryRepository> {

    public LaminateDirectoryServiceImpl(LaminateDirectoryRepository repository) {
        super(repository);
    }
}
/*
public class LaminateDirectoryServiceImpl implements SimpleService<LaminateDirectory> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private LaminateDirectoryRepository directoryRepository;

    @Override
    public LaminateDirectory findById(Long id) {
        */
/*
 * Optional<User> optinalEntity = userRepository.findById(id); User user
 * = optinalEntity.get(); return user;
 *//*

        return directoryRepository.getOne(id);
    }

    @Override
    public LaminateDirectory findByName(String name) {
        return directoryRepository.findByName(name);
    }

    @Override
    public void saveObject(LaminateDirectory directory) {
        directoryRepository.save(directory);
    }

    @Override
    public void updateObject(LaminateDirectory directory) {
        saveObject(directory);
    }

    @Override
    public void deleteById(Long id) {
        directoryRepository.deleteById(id);
    }

    @Override
    public List<LaminateDirectory> findAllObjects() {
        return directoryRepository.findAll();
    }

    @Override
    public boolean isObjectExist(LaminateDirectory directory) {
        return findByName(directory.getName()) != null;
    }

}
*/
