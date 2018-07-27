package ua.com.vetal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.Contractor;
import ua.com.vetal.repositories.ContractorRepository;

@Service("contractorService")
@Transactional
public class ContractorServiceImpl implements SimpleService<Contractor> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ContractorRepository personRepository;

	@Override
	public Contractor findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return personRepository.getOne(id);
	}

	@Override
	public Contractor findByName(String name) {
		// return directoryRepository.findByName(name);
		return null;
	}

	@Override
	public void saveObject(Contractor person) {
		personRepository.save(person);
	}

	@Override
	public void updateObject(Contractor person) {
		saveObject(person);
	}

	@Override
	public void deleteById(Long id) {
		personRepository.deleteById(id);
	}

	@Override
	public List<Contractor> findAllObjects() {
		return personRepository.findAll();
	}

	@Override
	public boolean isObjectExist(Contractor person) {
		// return findByName(manager.getName()) != null;
		return false;
	}

}
