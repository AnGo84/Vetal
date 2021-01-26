package ua.com.vetal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.ViewTaskDAO;
import ua.com.vetal.entity.ViewTask;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.ViewTaskRepository;

import java.util.List;

@Service("viewTaskService")
@Transactional
@Slf4j
public class ViewTaskServiceImpl {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ViewTaskRepository viewTaskRepository;
    @Autowired
    private ViewTaskDAO viewTaskDAO;

    public ViewTask findById(Long id) {
        return viewTaskRepository.getOne(id);
    }

    public List<ViewTask> findAllObjects() {
        List<ViewTask> getList = viewTaskRepository.findAll(sortByDateBeginDesc());
        return getList;
    }

    public List<ViewTask> findByFilterData(OrderViewFilter orderViewFilter) {
        List<ViewTask> viewTasks = viewTaskDAO.findByFilterData(orderViewFilter);

        if (orderViewFilter == null) {
            return findAllObjects();
        }

        return viewTasks;
    }


    private Sort sortByDateBeginDesc() {
        return Sort.by(Sort.Direction.DESC, "dateBegin");
    }


}
