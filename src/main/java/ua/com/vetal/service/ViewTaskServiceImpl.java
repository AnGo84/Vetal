package ua.com.vetal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.dao.ViewTaskDAO;
import ua.com.vetal.entity.ViewTask;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.ViewTaskRepository;

import java.util.List;

@Service("viewTaskService")
@Transactional
public class ViewTaskServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(ViewTaskServiceImpl.class);
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
        // logger.info("Get all TASKS");
        List<ViewTask> getList = viewTaskRepository.findAll(sortByDateBeginDesc());
        // List<Task> getList = taskRepository.findAllByOrderByDateBeginDesc();
        // logger.info("List size: " + getList.size());
        return getList;
    }

    public List<ViewTask> findByFilterData(FilterData filterData) {
        List<ViewTask> viewTasks = viewTaskDAO.findByFilterData(filterData);

        if (filterData == null) {
            return findAllObjects();
        }

        return viewTasks;
    }


    private Sort sortByDateBeginDesc() {
        //return new Sort(Sort.Direction.DESC, "dateBegin");
        return Sort.by(Sort.Direction.DESC, "dateBegin");
    }


}
