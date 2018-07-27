package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.repositories.ChromaticityDirectoryRepository;

@Service("chronomaticityDirectoryService")
@Transactional
public class ChromaticityDirectoryServiceImpl implements SimpleService<ChromaticityDirectory> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ChromaticityDirectoryRepository directoryRepository;

	@Override
	public ChromaticityDirectory findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return directoryRepository.getOne(id);
	}

	@Override
	public ChromaticityDirectory findByName(String name) {
		return directoryRepository.findByName(name);
	}

	@Override
	public void saveObject(ChromaticityDirectory directory) {
		directoryRepository.save(directory);
	}

	@Override
	public void updateObject(ChromaticityDirectory directory) {
		saveObject(directory);
	}

	@Override
	public void deleteById(Long id) {
		directoryRepository.deleteById(id);
	}

	@Override
	public List<ChromaticityDirectory> findAllObjects() {
		return directoryRepository.findAll();
	}

	@Override
	public boolean isObjectExist(ChromaticityDirectory directory) {
		return findByName(directory.getName()) != null;
	}

}
