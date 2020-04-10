package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.TaskRepository;
import ua.com.vetal.utils.StringUtils;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceImplTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TaskServiceImpl taskService;
    @MockBean
    private TaskRepository mockTaskRepository;
    private Task task;

    @BeforeEach
    public void beforeEach() {
        task = TestDataUtils.getTask(1l, 1);
    }

    @Test
    void whenFindById_thenReturnTask() {
        when(mockTaskRepository.getOne(1L)).thenReturn(task);
        long id = 1;
        Task foundTask = taskService.findById(id);

        // then
        assertNotNull(foundTask);
        assertNotNull(foundTask.getId());
        assertEquals(task, foundTask);
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockTaskRepository.getOne(1L)).thenReturn(task);
        long id = 2;
        Task found = taskService.findById(id);
        assertNull(found);
    }

    @Test
    void whenFindByAccount_thenReturnTask() {
        when(mockTaskRepository.findByAccount(task.getAccount())).thenReturn(task);
        Task foundTask = taskService.findByAccount(task.getAccount());

        assertNotNull(foundTask);
        assertNotNull(foundTask.getId());
        assertEquals(task, foundTask);
    }

    @Test
    void whenFindByAccount_thenReturnNull() {
        when(mockTaskRepository.findByAccount(anyString())).thenReturn(null);
        Task found = taskService.findByAccount("wrong name");
        assertNull(found);
    }

    @Test
    void whenSaveTask_thenSuccess() {
        Task newTask = TestDataUtils.getTask(null, 2);
        taskService.saveObject(newTask);
        verify(mockTaskRepository, times(1)).save(newTask);
    }

    @Test
    void whenSaveTask_thenNPE() {
        when(mockTaskRepository.save(any(Task.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            taskService.saveObject(task);
        });
    }

    @Test
    void whenUpdateTask_thenSuccess() {
        task.setAccount("accountNew");
        task.setWorkName("workNameNew");

        taskService.updateObject(task);
        verify(mockTaskRepository, times(1)).save(task);
    }

    @Test
    void whenUpdateTask_thenThrow() {
        when(mockTaskRepository.save(any(Task.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            taskService.updateObject(task);
        });
    }

    @Test
    void whenDeleteById_thenSuccess() {
        taskService.deleteById(1l);
        verify(mockTaskRepository, times(1)).deleteById(1l);
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        doThrow(new EmptyResultDataAccessException(0)).when(mockTaskRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            taskService.deleteById(1000000l);
        });
    }

    @Test
    void whenFindAllObjects() {
        when(mockTaskRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(task));
        List<Task> objects = taskService.findAllObjects();
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

    @Test
    void whenIsObjectExist() {
        when(mockTaskRepository.getOne(task.getId())).thenReturn(task);
        assertTrue(taskService.isObjectExist(task));
        when(mockTaskRepository.getOne(anyLong())).thenReturn(null);
        assertFalse(taskService.isObjectExist(task));
    }

    @Test
    void whenIssAccountValueExist() {
        when(mockTaskRepository.findByAccount(task.getAccount())).thenReturn(task);
        assertFalse(taskService.isAccountValueExist(task));

        Task newTask = TestDataUtils.getTask(2l, 2);
        when(mockTaskRepository.findByAccount(newTask.getAccount())).thenReturn(null);
        assertFalse(taskService.isAccountValueExist(newTask));

        newTask.setAccount(task.getAccount());
        assertTrue(taskService.isAccountValueExist(newTask));
    }

    @Test
    void whenCheckTaskForMailing() {
        String result = taskService.checkTaskForMailing(task);
        assertTrue(StringUtils.isEmpty(result));

        task.getManager().setEmail(null);
        result = taskService.checkTaskForMailing(task);
        assertFalse(StringUtils.isEmpty(result));
        task.getManager().setEmail("");
        result = taskService.checkTaskForMailing(task);
        assertFalse(StringUtils.isEmpty(result));

        task.getContractor().setEmail(null);
        result = taskService.checkTaskForMailing(task);
        assertFalse(StringUtils.isEmpty(result));
        task.getContractor().setEmail("");
        result = taskService.checkTaskForMailing(task);
        assertFalse(StringUtils.isEmpty(result));
    }


    @Disabled("Disabled until refactoring filters")
    @Test
    void whenFindByFilterData() {
        List<Task> filteredList = taskService.findByFilterData(null);
        assertEquals(filteredList.size(), 1);

        FilterData filterData = new FilterData();
        filterData.setAccount(task.getAccount());
        filterData.setManager(task.getManager());

        filteredList = taskService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filteredList = taskService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setAccount(task.getAccount());
        filteredList = taskService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setManager(task.getManager());
        filteredList = taskService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new FilterData();
        filterData.setManager(new Manager());
        filteredList = taskService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new FilterData();
        filterData.setAccount("not exist name");
        filteredList = taskService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());
    }
}