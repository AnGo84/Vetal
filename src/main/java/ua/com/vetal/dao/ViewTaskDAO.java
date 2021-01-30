package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.ViewTask;
import ua.com.vetal.entity.filter.OrderViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ViewTaskDAO {

    @PersistenceContext
    private EntityManager entityManager;


    public List<ViewTask> findByFilterData(OrderViewFilter orderViewFilter) {
        if (orderViewFilter == null) {
            return new ArrayList<>();
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ViewTask> query = builder.createQuery(ViewTask.class);
        Root<ViewTask> root = query.from(ViewTask.class);

        Predicate predicate = orderViewFilter.getPredicate(builder, root);
        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        List<ViewTask> list = entityManager.createQuery(query).getResultList();
        return list;
    }

}
