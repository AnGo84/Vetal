package ua.com.vetal.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.vetal.entity.FilterData;
import ua.com.vetal.entity.Task;
import ua.com.vetal.repositories.TaskRepository;

@Service("taskService")
@Transactional
public class TaskServiceImpl implements SimpleService<Task> {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

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
		// logger.info("Get all TASKS");
		List<Task> getList = taskRepository.findAll(sortByDateBeginDesc());
		// List<Task> getList = taskRepository.findAllByOrderByDateBeginDesc();
		// logger.info("List size: " + getList.size());
		return getList;
	}

	public Task findByAccount(Long account) {
		return taskRepository.findByAccount(account);
	}

	public List<Task> findByFilterData(FilterData filterData) {
		List<Task> tasks = null;

		if (filterData == null) {
			return findAllObjects();
		}

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Task> query = builder.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);

		Predicate predicate = builder.conjunction();

		if (filterData.getAccount() != null && !filterData.getAccount().equals("")) {
			predicate = builder.and(predicate, builder.equal(root.get("account"), filterData.getAccount()));
			/*
			 * predicate = builder.and(predicate,
			 * builder.like(builder.lower(builder.toString(root.get("account")))
			 * , ("%" + filterData.getAccount() + "%").toLowerCase()));
			 */
		}

		if (filterData.getFileName() != null && !filterData.getFileName().equals("")) {
			predicate = builder.and(predicate, builder.like(builder.lower(root.get("fileName")),
					("%" + filterData.getFileName() + "%").toLowerCase()));
		}

		if (filterData.getClient() != null && filterData.getClient().getId() != 0) {
			predicate = builder.and(predicate, builder.equal(root.get("client"), filterData.getClient()));
		}

		if (filterData.getContractor() != null && filterData.getContractor().getId() != 0) {
			predicate = builder.and(predicate, builder.equal(root.get("contractor"), filterData.getContractor()));
		}

		if (filterData.getManager() != null && filterData.getManager().getId() != 0) {
			predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
		}

		if (filterData.getDateBeginFrom() != null) {
			predicate = builder.and(predicate,
					builder.greaterThanOrEqualTo(root.get("dateBegin"), filterData.getDateBeginFrom()));
		}

		if (filterData.getDateBeginTill() != null) {
			predicate = builder.and(predicate,
					builder.lessThanOrEqualTo(root.get("dateBegin"), filterData.getDateBeginTill()));
		}

		/*
		 * for (SearchCriteria param : params) { if
		 * (param.getOperation().equalsIgnoreCase(">")) { predicate =
		 * builder.and(predicate,
		 * builder.greaterThanOrEqualTo(root.get(param.getKey()),
		 * param.getValue().toString())); } else if
		 * (param.getOperation().equalsIgnoreCase("<")) { predicate =
		 * builder.and(predicate,
		 * builder.lessThanOrEqualTo(root.get(param.getKey()),
		 * param.getValue().toString())); } else if
		 * (param.getOperation().equalsIgnoreCase(":")) { if
		 * (r.get(param.getKey()).getJavaType() == String.class) { predicate =
		 * builder.and(predicate, builder.like(root.get(param.getKey()), "%" +
		 * param.getValue() + "%")); } else { predicate = builder.and(predicate,
		 * builder.equal(root.get(param.getKey()), param.getValue())); } } }
		 */
		query.where(predicate);
		query.orderBy(builder.desc(root.get("dateBegin")));

		tasks = entityManager.createQuery(query).getResultList();

		// https://www.baeldung.com/rest-search-language-spring-jpa-criteria
		// http://qaru.site/questions/293915/spring-data-jpa-query-by-example
		return tasks;
	}

	@Override
	public boolean isObjectExist(Task task) {
		// return findByName(manager.getName()) != null;
		return findById(task.getId()) != null;
	}

	public boolean isAccountValueExist(Task task) {

		Task findTask = findByAccount(task.getAccount());

		/*
		 * System.out.println(findTask); System.out.println(findTask != null &&
		 * findTask.getId()!=null && !findTask.getId().equals(task.getId()));
		 * System.out.println((findTask != null )+": "+ (findTask.getId()!=null)
		 * +": "+ (!findTask.getId().equals(task.getId())));
		 * System.out.println(findTask.getId() +" : "+task.getId());
		 */
		return (findTask != null && findTask.getId() != null && !findTask.getId().equals(task.getId()));

		// return findByAccount(task.getAccount()) != null;
	}

	private Sort sortByDateBeginDesc() {
		return new Sort(Sort.Direction.DESC, "dateBegin");
	}
}
