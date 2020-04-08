package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.repositories.ManagerRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ManagerServiceImplTest {
    @Autowired
    private ManagerServiceImpl managerService;
    @MockBean
    private ManagerRepository mockManagerRepository;
    private Manager manager;

    @BeforeEach
    public void beforeEach() {
        manager = TestBuildersUtils.getManager(1l, "firstName", "lastName", "middleName", "email");
    }

    @Test
    void whenFindById_thenReturnManager() {
        when(mockManagerRepository.getOne(1L)).thenReturn(manager);
        long id = 1;
        Manager found = managerService.findById(id);

        assertNotNull(found);
        assertEquals(found.getId(), manager.getId());
        assertEquals(found.getFirstName(), manager.getFirstName());
        assertEquals(found.getLastName(), manager.getLastName());
        assertEquals(found.getMiddleName(), manager.getMiddleName());
        assertEquals(found.getPhone(), manager.getPhone());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockManagerRepository.getOne(1L)).thenReturn(manager);
        long id = 2;
        Manager found = managerService.findById(id);
        assertNull(found);
    }

/*    @Test
    void whenFindByName_thenReturnUser() {
        when(mockUserRepository.findByName(manager.getName())).thenReturn(manager);
        User found = managerService.findByName("User");

        assertNotNull(found);
        assertEquals(found.getId(), manager.getId());
        assertEquals(found.getName(), manager.getName());
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockUserRepository.findByName(manager.getName())).thenReturn(manager);
        User found = managerService.findByName("wrong name");
        assertNull(found);
    }*/

    @Test
    void whenSaveObject_thenSuccess() {
        Manager newManager = TestBuildersUtils.getManager(null, "firstName2", "lastName2", "middleName2", "email2");
        managerService.saveObject(newManager);
        verify(mockManagerRepository, times(1)).save(newManager);
    }

    @Test
    void whenSaveObject_thenNPE() {
        when(mockManagerRepository.save(any(Manager.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            managerService.saveObject(manager);
        });
    }

    @Test
    void whenUpdateObject_thenSuccess() {
        manager.setCorpName("corpName2");
        managerService.updateObject(manager);
        verify(mockManagerRepository, times(1)).save(manager);
    }

    @Test
    void whenUpdateObject_thenThrowNPE() {
        when(mockManagerRepository.save(any(Manager.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            managerService.updateObject(manager);
        });
    }

    @Test
    void whenDeleteById_thenSuccess() {
        managerService.deleteById(1l);
        verify(mockManagerRepository, times(1)).deleteById(1l);
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        doThrow(new EmptyResultDataAccessException(0)).when(mockManagerRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            managerService.deleteById(1000000l);
        });
    }

    @Test
    void findAllObjects() {
        when(mockManagerRepository.findAll()).thenReturn(Arrays.asList(manager));
        List<Manager> objects = managerService.findAllObjects();
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

    @Test
    void isObjectExist() {
        when(mockManagerRepository.getOne(manager.getId())).thenReturn(manager);
        assertTrue(managerService.isObjectExist(manager));

        when(mockManagerRepository.getOne(anyLong())).thenReturn(null);
        assertFalse(managerService.isObjectExist(manager));
    }

}