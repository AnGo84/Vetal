package ua.com.vetal.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.Task;
import ua.com.vetal.repositories.TaskRepository;

@Service("taskService")
@Transactional
public class TaskServiceImpl implements SimpleService<Task> {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public Task findById(Long id) {
		/*
		 * Optional<User> optinalEntity = userRepository.findById(id); User user
		 * = optinalEntity.get(); return user;
		 */
		return taskRepository.getOne(id);
	}

	@Override
	public Task findByName(String name) {
		// return directoryRepository.findByName(name);
		return null;
	}

	@Override
	public void saveObject(Task task) {
		taskRepository.save(task);
	}

	@Override
	public void updateObject(Task task) {
		saveObject(task);
	}

	@Override
	public void deleteById(Long id) {
		taskRepository.deleteById(id);
	}

	@Override
	public List<Task> findAllObjects() {
		logger.info("Get all TASKS");
		List<Task> getList = taskRepository.findAll();
		logger.info("List size: " + getList.size());
		return getList;
	}

	public Task findByAccount(String account) {
		return taskRepository.findByAccount(account);
	}

	@Override
	public boolean isObjectExist(Task task) {
		// return findByName(manager.getName()) != null;
		return false;
	}

}
