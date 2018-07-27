package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.Manager;
import ua.com.vetal.repositories.ManagerRepository;

@Service("managerService")
@Transactional
public class ManagerServiceImpl implements SimpleService<Manager> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ManagerRepository personRepository;

	@Override
	public Manager findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
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
		// return findByName(manager.getName()) != null;
		return false;
	}

}
