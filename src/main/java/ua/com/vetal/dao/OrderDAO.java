package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.OrderViewFilter;

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

    public List<Order> findByFilterData(OrderViewFilter orderViewFilter) {
        List<Order> list = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        Predicate predicate = builder.conjunction();

        if (orderViewFilter != null) {

            if (orderViewFilter.getClient() != null && orderViewFilter.getClient().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("client"), orderViewFilter.getClient()));
            }

            if (orderViewFilter.getManager() != null && orderViewFilter.getManager().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("manager"), orderViewFilter.getManager()));
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


            if (orderViewFilter.getDebtAmountFrom() != null) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(root.get("debtAmount"), orderViewFilter.getDebtAmountFrom()));
            }

            if (orderViewFilter.getDebtAmountTill() != null) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(root.get("debtAmount"), orderViewFilter.getDebtAmountTill()));
            }

        }
        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();

        return list;
    }

}
