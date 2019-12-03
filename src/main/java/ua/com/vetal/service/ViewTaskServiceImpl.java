package ua.com.vetal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.TaskDAO;
import ua.com.vetal.entity.FilterData;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.ViewTask;
import ua.com.vetal.repositories.TaskRepository;
import ua.com.vetal.repositories.ViewTaskRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Locale;

@Service("viewTaskService")
@Transactional
public class ViewTaskServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(ViewTaskServiceImpl.class);
    @Autowired
    MessageSource messageSource;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ViewTaskRepository viewTaskRepository;


    public ViewTask findById(Long id) {
        /*
         * Optional<User> optinalEntity = userRepository.findById(id); User user
         * = optinalEntity.get(); return user;
         */
        return viewTaskRepository.getOne(id);
    }

    public List<ViewTask> findAllObjects() {
        // logger.info("Get all TASKS");
        List<ViewTask> getList = viewTaskRepository.findAll(sortByDateBeginDesc());
        // List<Task> getList = taskRepository.findAllByOrderByDateBeginDesc();
        // logger.info("List size: " + getList.size());
        return getList;
    }

    public List<ViewTask> findByFilterData(FilterData filterData) {
        List<ViewTask> tasks = null;

        if (filterData == null) {
            return findAllObjects();
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ViewTask> query = builder.createQuery(ViewTask.class);
        Root<ViewTask> root = query.from(ViewTask.class);

        Predicate predicate = builder.conjunction();

        if (filterData.getAccount() != null && !filterData.getAccount().equals("")) {
            predicate = builder.and(predicate, builder.equal(root.get("account"), filterData.getAccount()));
            /*
             * predicate = builder.and(predicate,
             * builder.like(builder.lower(builder.toString(root.get("account")))
             * , ("%" + filterData.getAccount() + "%").toLowerCase()));
             */
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


    private Sort sortByDateBeginDesc() {
        return new Sort(Sort.Direction.DESC, "dateBegin");
    }


}
