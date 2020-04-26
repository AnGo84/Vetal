package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.dao.TaskDAO;
import ua.com.vetal.email.EmailMessage;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.TaskRepository;
import ua.com.vetal.utils.StringUtils;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceImplTest {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TaskServiceImpl taskService;
    @MockBean
    private TaskRepository mockTaskRepository;
    @MockBean
    private TaskDAO mockTaskDAO;
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
	void whenFindByName_thenReturnNull() {
		//when(mockManagerRepository.findByName(anyString())).thenReturn(manager);
		Task found = taskService.findByName(null);
		assertNull(found);
		found = taskService.findByName("wrong name");
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
    void whenGetTaskMailingDeclineReason() {
        String taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertTrue(StringUtils.isEmpty(taskMailingDeclineReason));

        task.getManager().setEmail(null);
        taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertFalse(StringUtils.isEmpty(taskMailingDeclineReason));
        task.getManager().setEmail("");
        taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertFalse(StringUtils.isEmpty(taskMailingDeclineReason));

        task.getContractor().setEmail(null);
        taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertFalse(StringUtils.isEmpty(taskMailingDeclineReason));
        task.getContractor().setEmail("");
        taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertFalse(StringUtils.isEmpty(taskMailingDeclineReason));
    }

    @Test
    void whenFindByFilterData() {
        when(mockTaskDAO.findByFilterData(any(FilterData.class))).thenReturn(Arrays.asList(task));
        List<Task> objects = taskService.findByFilterData(new FilterData());
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

    @Test
    void whenGetEmailMessage() {
        String subject = messageSource.getMessage("email.task", null, new Locale("ru"));
        String text = messageSource.getMessage("email.new_task", null, new Locale("ru"));
        EmailMessage emailMessage = taskService.getEmailMessage(task);
        assertNotNull(emailMessage);
        assertEquals(task.getManager().getEmail(), emailMessage.getFrom());
        assertEquals(task.getContractor().getEmail(), emailMessage.getTo());
        assertEquals(task.getContractor().getEmail(), emailMessage.getTo());
        assertTrue(emailMessage.getSubject().startsWith(subject));
        assertEquals(text, emailMessage.getText());
        assertNotNull(emailMessage.getAttachments());
        assertFalse(emailMessage.getAttachments().isEmpty());
        assertTrue(emailMessage.getAttachments().size() == 1);

        task.setDbFile(null);
        emailMessage = taskService.getEmailMessage(task);
        assertNotNull(emailMessage);
        assertEquals(task.getManager().getEmail(), emailMessage.getFrom());
        assertEquals(task.getContractor().getEmail(), emailMessage.getTo());
        assertEquals(task.getContractor().getEmail(), emailMessage.getTo());
        assertTrue(emailMessage.getSubject().startsWith(subject));
        assertNotNull(emailMessage.getAttachments());
        assertTrue(emailMessage.getAttachments().isEmpty());
    }
}