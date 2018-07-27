package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.ClientDirectory;
import ua.com.vetal.repositories.ClientDirectoryRepository;

@Service("clientDirectoryService")
@Transactional
public class ClientDirectoryServiceImpl implements SimpleService<ClientDirectory> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ClientDirectoryRepository directoryRepository;

	@Override
	public ClientDirectory findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return directoryRepository.getOne(id);
	}

	@Override
	public ClientDirectory findByName(String name) {
		return directoryRepository.findByName(name);
	}

	@Override
	public void saveObject(ClientDirectory directory) {
		directoryRepository.save(directory);
	}

	@Override
	public void updateObject(ClientDirectory directory) {
		saveObject(directory);
	}

	@Override
	public void deleteById(Long id) {
		directoryRepository.deleteById(id);
	}

	@Override
	public List<ClientDirectory> findAllObjects() {
		return directoryRepository.findAll();
	}

	@Override
	public boolean isObjectExist(ClientDirectory directory) {
		return findByName(directory.getName()) != null;
	}

}
