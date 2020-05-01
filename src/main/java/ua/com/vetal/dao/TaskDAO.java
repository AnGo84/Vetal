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
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class TaskDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /*public User findTaskByAccount(String account) {
        try {
            String sql = "Select e from " + Task.class.getName() + " e " //
                    + " Where e.account = :account ";
            System.out.println(sql);
            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("account", account);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }*/


    public Long getMaxID() {
        try {
            String sql = "Select max(e.id) from " + Task.class.getName() + " e ";
            //System.out.println(sql);
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
        List<Task> list = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);

        Predicate predicate = builder.conjunction();

        if (orderViewFilter != null) {

            if (orderViewFilter.getAccount() != null && !orderViewFilter.getAccount().equals("")) {
                predicate = builder.and(predicate, builder.equal(root.get("account"), orderViewFilter.getAccount()));
                /*
                 * predicate = builder.and(predicate,
                 * builder.like(builder.lower(builder.toString(root.get("account")))
                 * , ("%" + filterData.getAccount() + "%").toLowerCase()));
                 */
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("account")),
                        ("%" + orderViewFilter.getAccount() + "%").toLowerCase()));
            }


            if (orderViewFilter.getNumber() != null && !orderViewFilter.getNumber().equals("")) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullNumber")),
                        ("%" + orderViewFilter.getNumber() + "%").toLowerCase()));

            }

            if (orderViewFilter.getFileName() != null && !orderViewFilter.getFileName().equals("")) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("fileName")),
                        ("%" + orderViewFilter.getFileName() + "%").toLowerCase()));
            }

            if (orderViewFilter.getClient() != null && orderViewFilter.getClient().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("client"), orderViewFilter.getClient()));
            }

            if (orderViewFilter.getContractor() != null && orderViewFilter.getContractor().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("contractor"), orderViewFilter.getContractor()));
            }

            if (orderViewFilter.getManager() != null && orderViewFilter.getManager().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("manager"), orderViewFilter.getManager()));
            }
            if (orderViewFilter.getPaper() != null && orderViewFilter.getPaper().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("paper"), orderViewFilter.getPaper()));
            }
            if (orderViewFilter.getProduction() != null && orderViewFilter.getProduction().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("production"), orderViewFilter.getProduction()));
            }

            if (orderViewFilter.getDateBeginFrom() != null) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(root.get("dateBegin"), orderViewFilter.getDateBeginFrom()));
            }

            if (orderViewFilter.getDateBeginTill() != null) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(root.get("dateBegin"), orderViewFilter.getDateBeginTill()));
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

        }
        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();

        // https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        // http://qaru.site/questions/293915/spring-data-jpa-query-by-example
        return list;
    }

}
