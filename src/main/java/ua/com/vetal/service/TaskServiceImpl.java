package ua.com.vetal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.TaskDAO;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.TaskRepository;

import java.util.List;
import java.util.Locale;

@Service("taskService")
@Transactional
public class TaskServiceImpl implements SimpleService<Task> {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskDAO taskDAO;

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

    public Task findByAccount(String account) {
        return taskRepository.findByAccount(account);
    }

    public List<Task> findByFilterData(FilterData filterData) {
        List<Task> tasks = taskDAO.findByFilterData(filterData);

        if (filterData == null) {
            return findAllObjects();
        }
/*

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);

        Predicate predicate = builder.conjunction();

        if (filterData.getAccount() != null && !filterData.getAccount().equals("")) {
            predicate = builder.and(predicate, builder.equal(root.get("account"), filterData.getAccount()));
            */
        /*
         * predicate = builder.and(predicate,
         * builder.like(builder.lower(builder.toString(root.get("account")))
         * , ("%" + filterData.getAccount() + "%").toLowerCase()));
         *//*

            predicate = builder.and(predicate, builder.like(builder.lower(root.get("account")),
                    ("%" + filterData.getAccount() + "%").toLowerCase()));
        }


        if (filterData.getNumber() != null && !filterData.getNumber().equals("")) {
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullNumber")),
                    ("%" + filterData.getNumber() + "%").toLowerCase()));

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
        if (filterData.getPaper() != null && filterData.getPaper().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("paper"), filterData.getPaper()));
        }
        if (filterData.getProduction() != null && filterData.getProduction().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("production"), filterData.getProduction()));
        }

        if (filterData.getDateBeginFrom() != null) {
            predicate = builder.and(predicate,
                    builder.greaterThanOrEqualTo(root.get("dateBegin"), filterData.getDateBeginFrom()));
        }

        if (filterData.getDateBeginTill() != null) {
            predicate = builder.and(predicate,
                    builder.lessThanOrEqualTo(root.get("dateBegin"), filterData.getDateBeginTill()));
        }

        */
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
         *//*

        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        tasks = entityManager.createQuery(query).getResultList();
*/

        // https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        // http://qaru.site/questions/293915/spring-data-jpa-query-by-example
        return tasks;
    }

    @Override
    public boolean isObjectExist(Task task) {
        return findById(task.getId()) != null;
    }

    public boolean isAccountValueExist(Task task) {
        Task findTask = findByAccount(task.getAccount());
        return (findTask != null && findTask.getId() != null && !findTask.getId().equals(task.getId()));

        // return findByAccount(task.getAccount()) != null;
    }

    public Long getMaxID() {
        return taskDAO.getMaxID();
    }

    private Sort sortByDateBeginDesc() {
        //return new Sort(Sort.Direction.DESC, "dateBegin");
        return Sort.by(Sort.Direction.DESC, "dateBegin");
    }

    public String taskMailingDeclineReason(Task task) {

        String mailFrom = task.getManager().getEmail();
        if (mailFrom == null || mailFrom.equals("")) {
            return messageSource.getMessage("message.email.miss_from_for_manager",
                    null, new Locale("ru")) + " " + task.getContractor().getFullName();
        }

        String mailTo = task.getContractor().getEmail();
        if (mailTo == null || mailTo.equals("")) {
            return messageSource.getMessage("message.email.miss_to_for_contractor",
                    null, new Locale("ru")) + " " + task.getManager().getFullName();
        }

        return "";
    }

}
