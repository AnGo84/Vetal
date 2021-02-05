package ua.com.vetal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.vetal.dao.StatisticDAO;
import ua.com.vetal.entity.StatisticOrder;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.OrderRepository;

import java.util.List;

@Service("orderService")
//@Transactional
@Slf4j
public class OrderServiceImpl {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StatisticDAO statisticDAO;

	public List<StatisticOrder> findAllObjects() {
		List<StatisticOrder> getList = orderRepository.findAll(sortByDateBegin(Sort.Direction.ASC));
		return getList;
	}

	public List<StatisticOrder> findByFilterData(OrderViewFilter orderViewFilter) {
        List<StatisticOrder> list = statisticDAO.findByFilterData(orderViewFilter);

        if (orderViewFilter == null) {
            return findAllObjects();
        }
        return list;
    }

	private Sort sortByDateBegin(Sort.Direction direction) {
		return Sort.by(direction, "dateBegin");
	}

}
