package ua.com.vetal.dao;

import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.OrderViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;


public abstract class OrderDAO<T extends Order> {
    private final Class<T> tClass;

    protected OrderDAO(Class<T> tClass) {
        this.tClass = tClass;
    }

    protected Long getMaxID(EntityManager entityManager, String className) {
        try {
            String sql = "Select max(e.id) from " + className + " e ";
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

    protected List<T> findByFilterData(EntityManager entityManager, OrderViewFilter orderViewFilter) {
        if (orderViewFilter == null) {
            return Collections.emptyList();
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(tClass);
        Root<T> root = query.from(tClass);

        Predicate predicate = orderViewFilter.getPredicate(builder, root);

        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        List<T> list = entityManager.createQuery(query).getResultList();
        return list;
    }
}
