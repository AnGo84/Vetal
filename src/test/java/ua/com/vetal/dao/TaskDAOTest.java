package ua.com.vetal.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Task;
import ua.com.vetal.repositories.TaskRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ComponentScan("ua.com.vetal.dao")
public class TaskDAOTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskDAO taskDAO;

    private Task task;

    @BeforeEach
    public void beforeEach() {
        taskRepository.deleteAll();
        task = testEntityManager.persistAndFlush(
                TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 1), testEntityManager));
        task = testEntityManager.persistAndFlush(
                TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), testEntityManager));
    }

    @Test
    public void whenGetMaxID_thenReturnNewID() {
        Long newID = taskDAO.getMaxID();
        assertNotNull(newID);
        assertEquals(task.getId(), newID);
    }
}