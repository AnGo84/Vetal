package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.State;
import ua.com.vetal.repositories.StateRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StateServiceImplTest {
    @Autowired
    private StateServiceImpl stateService;
    @MockBean
    private StateRepository mockStateRepository;
    private State state;

    @BeforeEach
    public void beforeEach() {
        state = TestBuildersUtils.getState(1L, "name", "altname");
    }

    @Test
    void whenFindById_thenReturnObject() {
        when(mockStateRepository.getOne(1L)).thenReturn(state);
        long id = 1;
        State found = stateService.findById(id);

        assertNotNull(found);
        assertEquals(found.getId(), state.getId());
        assertEquals(found.getName(), state.getName());
        assertEquals(found.getAltName(), state.getAltName());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockStateRepository.getOne(1L)).thenReturn(state);
        long id = 221121;
        State found = stateService.findById(id);
        assertNull(found);
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockStateRepository.findByName(state.getName())).thenReturn(state);
        State found = stateService.findByName("wrong name");
        assertNull(found);
    }

    @Test
    void whenSaveObject_thenSuccess() {
        State newState = TestBuildersUtils.getState(null, "name2", "altname2");
        stateService.saveObject(newState);
        verify(mockStateRepository, times(1)).save(newState);
    }

    @Test
    void whenSaveObject_thenNPE() {
        when(mockStateRepository.save(any(State.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            stateService.saveObject(state);
        });
    }

    @Test
    void whenUpdateObject_thenSuccess() {
        state.setName("name2");
        stateService.updateObject(state);
        verify(mockStateRepository, times(1)).save(state);
    }

    @Test
    void whenUpdateObject_thenThrow() {
        when(mockStateRepository.save(any(State.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            stateService.updateObject(state);
        });
    }

    @Test
    void whenDeleteById_thenSuccess() {
        stateService.deleteById(1l);
        verify(mockStateRepository, times(1)).deleteById(1l);
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        doThrow(new EmptyResultDataAccessException(0)).when(mockStateRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            stateService.deleteById(1000000l);
        });
    }

    @Test
    void findAllObjects() {
        when(mockStateRepository.findAll()).thenReturn(Arrays.asList(state));
        List<State> directoriesList = stateService.findAllObjects();
        assertNotNull(directoriesList);
        assertFalse(directoriesList.isEmpty());
        assertEquals(directoriesList.size(), 1);
    }

    @Test
    void isObjectExist() {
        when(mockStateRepository.findByName(state.getName())).thenReturn(state);
        assertTrue(stateService.isObjectExist(state));
        when(mockStateRepository.findByName(state.getName())).thenReturn(state);
    }
}