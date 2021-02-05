package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.OrderViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class TaskDAO extends OrderDAO<Task> {

    @PersistenceContext
    private EntityManager entityManager;

    public TaskDAO() {
        super(Task.class);
    }

    public Long getMaxID() {
        return getMaxID(entityManager, Task.class.getName());
    }

    public List<Task> findByFilterData(OrderViewFilter orderViewFilter) {
        return findByFilterData(entityManager, orderViewFilter);
    }

}
