package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.StatisticOrder;
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

	public List<StatisticOrder> findByFilterData(OrderViewFilter orderViewFilter) {
		List<StatisticOrder> list = null;
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StatisticOrder> query = builder.createQuery(StatisticOrder.class);
		Root<StatisticOrder> root = query.from(StatisticOrder.class);

		Predicate predicate = builder.conjunction();

		if (orderViewFilter != null && orderViewFilter.hasData()) {
			predicate = orderViewFilter.getPredicate(builder, root, predicate);
		}
		query.where(predicate);
		query.orderBy(builder.desc(root.get("dateBegin")));

		list = entityManager.createQuery(query).getResultList();

        return list;
    }

}
