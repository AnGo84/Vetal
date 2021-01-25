package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.repositories.ProductionTypeDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("productionTypeDirectoryService")
@Transactional
public class ProductionTypeDirectoryServiceImpl extends AbstractDirectoryService<ProductionTypeDirectory, ProductionTypeDirectoryRepository> {

    public ProductionTypeDirectoryServiceImpl(ProductionTypeDirectoryRepository repository) {
        super(repository);
    }
}
/*
public class ProductionTypeDirectoryServiceImpl implements SimpleService<ProductionTypeDirectory> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ProductionTypeDirectoryRepository productionTypeDirectoryRepository;

    @Override
    public ProductionTypeDirectory findById(Long id) {
        */
/*
 * Optional<User> optinalEntity = userRepository.findById(id); User user
 * = optinalEntity.get(); return user;
 *//*

        return productionTypeDirectoryRepository.getOne(id);
    }

    @Override
    public ProductionTypeDirectory findByName(String name) {
        return productionTypeDirectoryRepository.findByName(name);
    }

    @Override
    public void saveObject(ProductionTypeDirectory productionTypeDirectory) {
        productionTypeDirectoryRepository.save(productionTypeDirectory);
    }

    @Override
    public void updateObject(ProductionTypeDirectory productionTypeDirectory) {
        saveObject(productionTypeDirectory);
    }

    @Override
    public void deleteById(Long id) {
        productionTypeDirectoryRepository.deleteById(id);
    }

    @Override
    public List<ProductionTypeDirectory> findAllObjects() {
        return productionTypeDirectoryRepository.findAll();
    }

    @Override
    public boolean isObjectExist(ProductionTypeDirectory productionTypeDirectory) {
        return findByName(productionTypeDirectory.getName()) != null;
    }

}
*/
