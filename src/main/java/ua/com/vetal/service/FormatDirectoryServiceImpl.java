package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.repositories.FormatDirectoryRepository;

@Service("formatDirectoryService")
@Transactional
public class FormatDirectoryServiceImpl implements SimpleService<FormatDirectory> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private FormatDirectoryRepository directoryRepository;

	@Override
	public FormatDirectory findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return directoryRepository.getOne(id);
	}

	@Override
	public FormatDirectory findByName(String name) {
		return directoryRepository.findByName(name);
	}

	@Override
	public void saveObject(FormatDirectory directory) {
		directoryRepository.save(directory);
	}

	@Override
	public void updateObject(FormatDirectory directory) {
		saveObject(directory);
	}

	@Override
	public void deleteById(Long id) {
		directoryRepository.deleteById(id);
	}

	@Override
	public List<FormatDirectory> findAllObjects() {
		return directoryRepository.findAll();
	}

	@Override
	public boolean isObjectExist(FormatDirectory directory) {
		return findByName(directory.getName()) != null;
	}

}
