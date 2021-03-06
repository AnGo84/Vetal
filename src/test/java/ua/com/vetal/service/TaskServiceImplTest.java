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
import ua.com.vetal.entity.DBFile;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.TaskRepository;
import ua.com.vetal.utils.StringUtils;

import java.io.IOException;
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
    private TaskServiceImpl taskService;
    @MockBean
    private TaskRepository mockTaskRepository;
    @MockBean
    private TaskDAO mockTaskDAO;
    @MockBean
    private DBFileStorageService mockDBFileStorageService;

    private Task task;

    private DBFile mockDBFile = new DBFile("new file", "type", "new file data".getBytes());

    @BeforeEach
    public void beforeEach() {
        task = TestDataUtils.getTask(1l, 1);
    }

    @Test
    public void whenFindById_thenReturnTask() {
        when(mockTaskRepository.getOne(1L)).thenReturn(task);
        long id = 1;
        Task foundTask = taskService.findById(id);

        // then
        assertNotNull(foundTask);
        assertNotNull(foundTask.getId());
        assertEquals(task, foundTask);
    }

    @Test
    public void whenFindById_thenReturnNull() {
        when(mockTaskRepository.getOne(1L)).thenReturn(task);
        long id = 2;
        Task found = taskService.findById(id);
        assertNull(found);
    }

    @Test
    public void whenFindByAccount_thenReturnTask() {
        when(mockTaskRepository.findByAccount(task.getAccount())).thenReturn(task);
        Task foundTask = taskService.findByAccount(task.getAccount());

        assertNotNull(foundTask);
        assertNotNull(foundTask.getId());
        assertEquals(task, foundTask);
    }

    @Test
    public void whenFindByAccount_thenReturnNull() {
        when(mockTaskRepository.findByAccount(anyString())).thenReturn(null);
        Task found = taskService.findByAccount("wrong name");
        assertNull(found);
    }

    @Test
    public void whenFindByName_thenReturnNull() {
        Task found = taskService.findByName(null);
        assertNull(found);
        found = taskService.findByName("wrong name");
        assertNull(found);
    }

    @Test
    public void whenSaveTask_thenSuccess() {
        Task newTask = TestDataUtils.getTask(null, 2);
        taskService.saveObject(newTask);
        verify(mockTaskRepository, times(1)).save(newTask);
    }

    @Test
    public void whenSaveTask_thenNPE() {
        when(mockTaskRepository.save(any(Task.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            taskService.saveObject(task);
        });
    }

    @Test
    public void whenUpdateTask_thenSuccess() throws IOException {
        task.setAccount("accountNew");
        task.setWorkName("workNameNew");

        taskService.updateObject(task);
        verify(mockTaskRepository, times(1)).save(task);

        taskService.updateObject(task, null);
        verify(mockTaskRepository, times(2)).save(task);
        taskService.updateObject(task, new DBFile("file", "", new byte[0]));
        verify(mockTaskRepository, times(3)).save(task);

        //case wrong file name

        task.getDbFile().setId(12l);
        task.setFileName(null);
        taskService.updateObject(task, null);
        verify(mockTaskRepository, times(4)).save(task);
        verify(mockDBFileStorageService, times(1)).deleteById(anyLong());

        task = TestDataUtils.getTask(1l, 1);
        task.setFileName("");
        task.getDbFile().setId(12l);
        taskService.updateObject(task, null);
        verify(mockTaskRepository, times(1)).save(task);
        verify(mockDBFileStorageService, times(2)).deleteById(anyLong());

        task = TestDataUtils.getTask(1l, 1);
        task.setFileName("  ");
        task.getDbFile().setId(12l);
        taskService.updateObject(task, null);
        verify(mockTaskRepository, times(1)).save(task);
        verify(mockDBFileStorageService, times(3)).deleteById(anyLong());
    }

    @Test
    public void whenUpdateTaskWithUploadFile_thenSuccess() throws IOException {
        task.setAccount("accountNew");
        task.setWorkName("workNameNew");

        DBFile dbFile = new DBFile("file1", "text/plain", "file data".getBytes());
        when(mockDBFileStorageService.storeMultipartFile(any())).thenReturn(dbFile);

        taskService.updateObject(task, mockDBFile);

        verify(mockTaskRepository, times(1)).save(task);
        verify(mockDBFileStorageService, times(0)).deleteById(anyLong());

        task.getDbFile().setId(12l);
        taskService.updateObject(task, mockDBFile);
        verify(mockTaskRepository, times(2)).save(task);
        verify(mockDBFileStorageService, times(1)).deleteById(anyLong());

    }

    @Test
    public void whenUpdateTask_thenThrow() {
        when(mockTaskRepository.save(any(Task.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            taskService.updateObject(task);
        });

        assertThrows(NullPointerException.class, () -> {
            taskService.updateObject(task, null);
        });

    }



    @Test
    public void whenDeleteById_thenSuccess() {
        taskService.deleteById(1l);
        verify(mockTaskRepository, times(1)).deleteById(1l);
    }

    @Test
    public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        doThrow(new EmptyResultDataAccessException(0)).when(mockTaskRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            taskService.deleteById(1000000l);
        });
    }

    @Test
    public void whenFindAllObjects() {
        when(mockTaskRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(task));
        List<Task> objects = taskService.findAllObjects();
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

    @Test
    public void whenIsObjectExist() {
        when(mockTaskRepository.getOne(task.getId())).thenReturn(task);
        assertTrue(taskService.isObjectExist(task));
        when(mockTaskRepository.getOne(anyLong())).thenReturn(null);
        assertFalse(taskService.isObjectExist(task));
    }

    @Test
    public void whenIssAccountValueExist() {
        when(mockTaskRepository.findByAccount(task.getAccount())).thenReturn(task);
        assertFalse(taskService.isAccountValueExist(task));

        Task newTask = TestDataUtils.getTask(2l, 2);
        when(mockTaskRepository.findByAccount(newTask.getAccount())).thenReturn(null);
        assertFalse(taskService.isAccountValueExist(newTask));

        newTask.setAccount(task.getAccount());
        assertTrue(taskService.isAccountValueExist(newTask));
    }

    @Test
    public void whenGetTaskMailingDeclineReason() {
        String taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertTrue(StringUtils.isEmpty(taskMailingDeclineReason));

        task.getManager().setEmail(null);
        taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertFalse(StringUtils.isEmpty(taskMailingDeclineReason));
        task.getManager().setEmail("");
        taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertFalse(StringUtils.isEmpty(taskMailingDeclineReason));

        task.getManager().setEmail("email");
        task.getContractor().setEmail(null);
        taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertFalse(StringUtils.isEmpty(taskMailingDeclineReason));
        task.getContractor().setEmail("");
        taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
        assertFalse(StringUtils.isEmpty(taskMailingDeclineReason));

    }

    @Test
    public void whenFindByFilterData() {
        when(mockTaskDAO.findByFilterData(any(OrderViewFilter.class))).thenReturn(Arrays.asList(task));
        List<Task> objects = taskService.findByFilterData(new OrderViewFilter());
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);

        when(mockTaskRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(task));
        objects = taskService.findByFilterData(null);
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

    @Test
    public void whenGetEmailMessage() {
        String subject = messageSource.getMessage("email.task", null, new Locale("ru"));
        String text = messageSource.getMessage("email.new_task", null, new Locale("ru"));
        EmailMessage emailMessage = taskService.getEmailMessage(task);
        assertNotNull(emailMessage);
        assertEquals(task.getManager().getEmail(), emailMessage.getFrom());
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
        assertTrue(emailMessage.getSubject().startsWith(subject));
        assertNotNull(emailMessage.getAttachments());
        assertTrue(emailMessage.getAttachments().isEmpty());

    }

    @Test
    public void whenGetMaxID_thenReturnResult() {
        long testMaxId = 1236l;
        when(mockTaskDAO.getMaxID()).thenReturn(testMaxId);
        assertNotNull(taskService.getMaxID());
        assertEquals(testMaxId, taskService.getMaxID());
    }
}