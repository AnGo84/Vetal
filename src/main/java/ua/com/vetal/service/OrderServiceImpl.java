package ua.com.vetal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.vetal.dao.OrderDAO;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.OrderRepository;

import java.util.List;

@Service("orderService")
//@Transactional
public class OrderServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderDAO orderDAO;

	public List<Order> findAllObjects() {
		// logger.info("Get all TASKS");
		List<Order> getList = orderRepository.findAll(sortByDateBegin(Sort.Direction.ASC));
		// List<Task> getList = stencilRepository.findAllByOrderByDateBeginDesc();
		// logger.info("List size: " + getList.size());
		return getList;
	}

	/**
	 * https://www.baeldung.com/rest-search-language-spring-jpa-criteria
	 * http://qaru.site/questions/293915/spring-data-jpa-query-by-example
	 */
	public List<Order> findByFilterData(FilterData filterData) {
		List<Order> list = orderDAO.findByFilterData(filterData);

		if (filterData == null) {
			return findAllObjects();
		}
		return list;
	}

	private Sort sortByDateBegin(Sort.Direction direction) {
		//return new Sort(direction, "dateBegin");
		return Sort.by(direction, "dateBegin");
	}

}
