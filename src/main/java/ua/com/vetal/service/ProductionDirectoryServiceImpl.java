package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.repositories.ProductionDirectoryRepository;

@Service("productionDirectoryService")
@Transactional
public class ProductionDirectoryServiceImpl implements SimpleService<ProductionDirectory> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ProductionDirectoryRepository productionDirectoryRepository;

	@Override
	public ProductionDirectory findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return productionDirectoryRepository.getOne(id);
	}

	@Override
	public ProductionDirectory findByName(String name) {
		return productionDirectoryRepository.findByName(name);
	}

	@Override
	public void saveObject(ProductionDirectory productionDirectory) {
		productionDirectoryRepository.save(productionDirectory);
	}

	@Override
	public void updateObject(ProductionDirectory productionDirectory) {
		saveObject(productionDirectory);
	}

	@Override
	public void deleteById(Long id) {
		productionDirectoryRepository.deleteById(id);
	}

	@Override
	public List<ProductionDirectory> findAllObjects() {
		return productionDirectoryRepository.findAll();
	}

	@Override
	public boolean isObjectExist(ProductionDirectory productionDirectory) {
		return findByName(productionDirectory.getName()) != null;
	}

}
