package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.CringleDirectory;
import ua.com.vetal.repositories.CringleDirectoryRepository;

@Service("cringleDirectoryService")
@Transactional
public class CringleDirectoryServiceImpl implements SimpleService<CringleDirectory> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private CringleDirectoryRepository directoryRepository;

	@Override
	public CringleDirectory findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return directoryRepository.getOne(id);
	}

	@Override
	public CringleDirectory findByName(String name) {
		return directoryRepository.findByName(name);
	}

	@Override
	public void saveObject(CringleDirectory directory) {
		directoryRepository.save(directory);
	}

	@Override
	public void updateObject(CringleDirectory directory) {
		saveObject(directory);
	}

	@Override
	public void deleteById(Long id) {
		directoryRepository.deleteById(id);
	}

	@Override
	public List<CringleDirectory> findAllObjects() {
		return directoryRepository.findAll();
	}

	@Override
	public boolean isObjectExist(CringleDirectory directory) {
		return findByName(directory.getName()) != null;
	}

}
