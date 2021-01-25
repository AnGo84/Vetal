package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Worker;
import ua.com.vetal.repositories.WorkerRepository;
import ua.com.vetal.service.common.AbstractEmployeeService;

@Service("workerService")
@Transactional
public class WorkerServiceImpl extends AbstractEmployeeService<Worker, WorkerRepository> {

	public WorkerServiceImpl(WorkerRepository repository) {
		super(repository);
	}
}
/*
public class WorkerServiceImpl implements SimpleService<Worker> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private WorkerRepository personRepository;

	@Override
	public Worker findById(Long id) {
		*/
/*
 * Optional<User> optinalEntity = userRepository.findById(id); User user
 * = optinalEntity.get(); return user;
 *//*

		return personRepository.getOne(id);
	}

	@Override
	public Worker findByName(String name) {
		// return directoryRepository.findByName(name);
		return null;
	}

	@Override
	public void saveObject(Worker person) {
		personRepository.save(person);
	}

	@Override
	public void updateObject(Worker person) {
		saveObject(person);
	}

	@Override
	public void deleteById(Long id) {
		personRepository.deleteById(id);
	}

	@Override
	public List<Worker> findAllObjects() {
		return personRepository.findAll();
	}

	@Override
	public boolean isObjectExist(Worker person) {
		return findById(person.getId()) != null;
	}

}
*/
