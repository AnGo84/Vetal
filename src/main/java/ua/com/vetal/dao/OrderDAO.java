package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.FilterData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class OrderDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Order> findByFilterData(FilterData filterData) {
        List<Order> list = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        Predicate predicate = builder.conjunction();

        if (filterData != null) {

            if (filterData.getClient() != null && filterData.getClient().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("client"), filterData.getClient()));
            }

            if (filterData.getManager() != null && filterData.getManager().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
            }

            if (filterData.getProduction() != null && filterData.getProduction().getId() != null) {
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


            if (filterData.getDebtAmountFrom() != null) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(root.get("debtAmount"), filterData.getDebtAmountFrom()));
            }

            if (filterData.getDebtAmountTill() != null) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(root.get("debtAmount"), filterData.getDebtAmountTill()));
            }

        }
        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();

        return list;
    }

}
