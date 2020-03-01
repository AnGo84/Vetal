package ua.com.vetal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.OrderRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements SimpleService<Order> {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order findById(Long id) {
        /*
         * Optional<User> optinalEntity = userRepository.findById(id); User user
         * = optinalEntity.get(); return user;
         */
        return orderRepository.getOne(id);
    }

    @Override
    public Order findByName(String name) {
        return null;
    }

    @Override
    public void saveObject(Order order) {

    }

    @Override
    public void updateObject(Order order) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Order> findAllObjects() {
        // logger.info("Get all TASKS");
        List<Order> getList = orderRepository.findAll(sortByDateBeginDesc());
        // List<Task> getList = stencilRepository.findAllByOrderByDateBeginDesc();
        // logger.info("List size: " + getList.size());
        return getList;
    }

    public List<Order> findByFilterData(FilterData filterData) {
        List<Order> list = null;

        if (filterData == null) {
            return findAllObjects();
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        Predicate predicate = builder.conjunction();


        if (filterData.getClient() != null && filterData.getClient().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("client"), filterData.getClient()));
        }

        if (filterData.getManager() != null && filterData.getManager().getId() != 0) {
            predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
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

        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();

        // https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        // http://qaru.site/questions/293915/spring-data-jpa-query-by-example
        return list;
    }

    @Override
    public boolean isObjectExist(Order order) {
        return findById(order.getId()) != null;
    }

    private Sort sortByDateBeginDesc() {
        return new Sort(Sort.Direction.DESC, "dateBegin");
    }
}
