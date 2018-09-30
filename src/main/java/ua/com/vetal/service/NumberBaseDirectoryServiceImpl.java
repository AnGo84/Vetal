package ua.com.vetal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.entity.NumberBaseDirectory;
import ua.com.vetal.repositories.FormatDirectoryRepository;
import ua.com.vetal.repositories.NumberBaseDirectoryRepository;

import java.util.List;

@Service("numberBaseDirectoryService")
@Transactional
public class NumberBaseDirectoryServiceImpl implements SimpleService<NumberBaseDirectory> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private NumberBaseDirectoryRepository directoryRepository;

	@Override
	public NumberBaseDirectory findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return directoryRepository.getOne(id);
	}

	@Override
	public NumberBaseDirectory findByName(String name) {
		return directoryRepository.findByName(name);
	}

	@Override
	public void saveObject(NumberBaseDirectory directory) {
		directoryRepository.save(directory);
	}

	@Override
	public void updateObject(NumberBaseDirectory directory) {
		saveObject(directory);
	}

	@Override
	public void deleteById(Long id) {
		directoryRepository.deleteById(id);
	}

	@Override
	public List<NumberBaseDirectory> findAllObjects() {
		return directoryRepository.findAll();
	}

	@Override
	public boolean isObjectExist(NumberBaseDirectory directory) {
		return findByName(directory.getName()) != null;
	}

}
