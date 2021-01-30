package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.dao.ViewTaskDAO;
import ua.com.vetal.entity.ViewTask;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.ViewTaskRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ViewTaskServiceImplTest {

    @Autowired
    private ViewTaskServiceImpl viewTaskService;
    @MockBean
    private ViewTaskRepository mockViewTaskRepository;
    @MockBean
    private ViewTaskDAO mockViewTaskDAO;
    private ViewTask viewTask;

    @BeforeEach
    public void beforeEach() {
        viewTask = TestDataUtils.getViewTask(1l, 1);
    }

    @Test
    void whenFindById_thenReturnTask() {
        when(mockViewTaskRepository.getOne(1L)).thenReturn(viewTask);
        long id = 1;
        ViewTask foundViewTask = viewTaskService.findById(id);

        // then
        assertNotNull(foundViewTask);
        assertNotNull(foundViewTask.getId());
        assertEquals(viewTask, foundViewTask);
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockViewTaskRepository.getOne(1L)).thenReturn(viewTask);
        long id = 2;
        ViewTask found = viewTaskService.findById(id);
        assertNull(found);
    }

    @Test
    void whenFindAllObjects() {
        when(mockViewTaskRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(viewTask));
        List<ViewTask> objects = viewTaskService.findAllObjects();
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

    @Test
    void whenFindByFilterData() {
        when(mockViewTaskDAO.findByFilterData(any(OrderViewFilter.class))).thenReturn(Arrays.asList(viewTask));
        List<ViewTask> objects = viewTaskService.findByFilterData(new OrderViewFilter());
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }
}