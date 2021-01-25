package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.StockDirectory;
import ua.com.vetal.repositories.StockDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("stockDirectoryService")
@Transactional
public class StockDirectoryServiceImpl extends AbstractDirectoryService<StockDirectory, StockDirectoryRepository> {
    public StockDirectoryServiceImpl(StockDirectoryRepository repository) {
        super(repository);
    }
}
/*
public class StockDirectoryServiceImpl implements SimpleService<StockDirectory> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private StockDirectoryRepository directoryRepository;

    @Override
    public StockDirectory findById(Long id) {
        */
/*
 * Optional<User> optinalEntity = userRepository.findById(id); User user
 * = optinalEntity.get(); return user;
 *//*

        return directoryRepository.getOne(id);
    }

    @Override
    public StockDirectory findByName(String name) {
        return directoryRepository.findByName(name);
    }

    @Override
    public void saveObject(StockDirectory directory) {
        directoryRepository.save(directory);
    }

    @Override
    public void updateObject(StockDirectory directory) {
        saveObject(directory);
    }

    @Override
    public void deleteById(Long id) {
        directoryRepository.deleteById(id);
    }

    @Override
    public List<StockDirectory> findAllObjects() {
        return directoryRepository.findAll();
    }

    @Override
    public boolean isObjectExist(StockDirectory directory) {
        return findByName(directory.getName()) != null;
    }

}
*/
