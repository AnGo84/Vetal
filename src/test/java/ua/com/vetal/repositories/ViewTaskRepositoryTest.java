package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.ViewTask;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ViewTaskRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ViewTaskRepository viewTaskRepository;

    private Task task;
    private ViewTask viewTask;

    @BeforeEach
    public void beforeEach() {
        /*viewTaskRepository.deleteAll();

        viewTask = TestDataServiceUtils.saveViewTaskParts(TestDataUtils.getViewTask(null, 1), entityManager);
        viewTask = entityManager.persistAndFlush(viewTask);*/

        taskRepository.deleteAll();
        task = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 1), entityManager);
        task = entityManager.persistAndFlush(task);
        viewTask = viewTaskRepository.getOne(task.getId());
    }

    @Test
    public void whenFindByID_thenReturnManager() {

        //User user = userRepository.findByName("User");
        // when
        Optional<ViewTask> foundViewTask = viewTaskRepository.findById(task.getId());
        // then
        assertTrue(foundViewTask.isPresent());
        assertEquals(viewTask, foundViewTask.get());
    }

    @Test
    public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            viewTaskRepository.findById(null);
        });

    }

    @Test
    public void whenFindByID_thenReturnEmpty() {
        // when
        Long wrongId = 123654L;
        Optional<ViewTask> foundTask = viewTaskRepository.findById(wrongId);
        // then
        assertFalse(foundTask.isPresent());
    }

    @Test
    public void whenFindAll_thenReturnListOfManagers() {
        //given
        Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
        newTask = entityManager.persistAndFlush(newTask);

        entityManager.persistAndFlush(newTask);
        // when
        List<ViewTask> viewTaskList = viewTaskRepository.findAll();
        // then
        assertNotNull(viewTaskList);
        assertFalse(viewTaskList.isEmpty());
        assertEquals(viewTaskList.size(), 2);
    }
}