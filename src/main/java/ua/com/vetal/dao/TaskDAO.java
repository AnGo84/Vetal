package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.OrderViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class TaskDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Long getMaxID() {
        try {
            String sql = "Select max(e.id) from " + Task.class.getName() + " e ";
            Query query = entityManager.createQuery(sql);
            Long nom = (Long) query.getSingleResult();
            if (nom == null) {
                nom = 0L;
            }
            return nom;
        } catch (NoResultException e) {
            return 0L;
        }
    }

    public List<Task> findByFilterData(OrderViewFilter orderViewFilter) {
        if (orderViewFilter == null) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);

        Predicate predicate = orderViewFilter.getPredicate(builder, root);

        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        List<Task> list = entityManager.createQuery(query).getResultList();
        return list;
    }

}
