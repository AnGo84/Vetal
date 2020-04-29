package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.State;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StateRepositoryTest {
    public static final String SECOND_DIRECTORY_NAME = "Second State";
    public static final String NAME_WITH_LENGTH_MORE_THEN_250_SYMBOLS = "NameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSaving";
    public static final String DIRECTORY_NAME = "State";

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private StateRepository stateRepository;
    private State state;

    @BeforeEach
    public void beforeEach() {
        stateRepository.deleteAll();
        state = TestBuildersUtils.getState(null, "name", "altname");
        entityManager.persistAndFlush(state);
    }

    @Test
    public void whenFindByName_thenReturnObject() {
        // when
        State foundState = stateRepository.findByName(state.getName());

        // then
        assertNotNull(foundState);
        assertNotNull(foundState.getId());
        assertEquals(foundState.getName(), state.getName());
        assertEquals(foundState.getAltName(), state.getAltName());
    }

    @Test
    public void whenFindByName_thenReturnEmpty() {
        assertNull(stateRepository.findByName("wrong name"));
    }

    @Test
    public void whenFindByID_thenReturnObject() {
        // when
        State state = stateRepository.findByName(this.state.getName());
        Optional<State> foundState = stateRepository.findById(state.getId());
        // then
        assertTrue(foundState.isPresent());
        assertEquals(foundState.get(), state);
    }

    @Test
    public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            stateRepository.findById(null);
        });

    }

    @Test
    public void whenFindByID_thenReturnEmpty() {
        // when
        Optional<State> foundState = stateRepository.findById(1123l);
        // then
        assertFalse(foundState.isPresent());
    }

    @Test
    public void whenFindAll_thenReturnListOfRecords() {
        //given
        State newState = TestBuildersUtils.getState(null, "name2", "altname2");
        entityManager.persistAndFlush(newState);
        // when
        List<State> States = stateRepository.findAll();
        // then
        assertNotNull(States);
        assertFalse(States.isEmpty());
        assertEquals(States.size(), 2);
    }

    @Test
    public void it_should_save_object() {
        State newState = TestBuildersUtils.getState(null, "name2", "altname2");
        newState = stateRepository.save(newState);
        State foundState = stateRepository.findByName(newState.getName());
        // then
        assertNotNull(foundState);
        assertNotNull(foundState.getId());
        assertEquals(foundState.getId(), newState.getId());
        assertEquals(foundState.getName(), newState.getName());
        assertEquals(foundState.getAltName(), newState.getAltName());
    }

    @Test
    public void it_should_save_object_with_empty_fields() {
        State newState = TestBuildersUtils.getState(null, "name2", "");
        newState = stateRepository.save(newState);
        State foundState = stateRepository.findByName(newState.getName());
        // then
        assertNotNull(foundState);
        assertNotNull(foundState.getId());
        assertEquals(foundState.getId(), newState.getId());
        assertEquals(foundState.getName(), newState.getName());
        assertEquals(foundState.getAltName(), newState.getAltName());
    }

    @Test
    public void it_should_save_object_with_null_fields() {
        State newState = TestBuildersUtils.getState(null, "name2", null);
        newState = stateRepository.save(newState);
        State foundState = stateRepository.findByName(newState.getName());
        // then
        assertNotNull(foundState);
        assertNotNull(foundState.getId());
        assertEquals(foundState.getId(), newState.getId());
        assertEquals(foundState.getName(), newState.getName());
        assertEquals(foundState.getAltName(), newState.getAltName());
    }

    @Test
    public void whenSaveObjectWithNameTooLong_thenThrowDataIntegrityViolationException() {

        State newState = TestBuildersUtils.getState(null, "nameWithLengthMoreThen30SymbolsIsTooLongForSaving", "altname2");
        assertThrows(DataIntegrityViolationException.class, () -> {
            stateRepository.save(newState);
        });
    }

    @Test
    public void whenSaveObjectWithNameTooShort_thenThrowConstraintViolationException() {
        State newState = TestBuildersUtils.getState(null, "", "altname2");
        assertThrows(ConstraintViolationException.class, () -> {
            stateRepository.save(newState);
        });
    }


    @Test
    public void whenSaveObjectWithAltNameTooLong_thenThrowDataIntegrityViolationException() {
        State newState = TestBuildersUtils.getState(null, "name", "altNameWithLengthMoreThen30SymbolsIsTooLongForSaving");
        assertThrows(DataIntegrityViolationException.class, () -> {
            stateRepository.save(newState);
        });
    }

    @Test
    public void whenDeleteById_thenOk() {
        //given
        State newState = TestBuildersUtils.getState(null, "name2", "altname2");

        entityManager.persistAndFlush(newState);
        assertEquals(stateRepository.findAll().size(), 2);

        State foundState = stateRepository.findByName(newState.getName());

        // when
        stateRepository.deleteById(foundState.getId());
        // then
        assertEquals(stateRepository.findAll().size(), 1);
    }

    @Test
    public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            stateRepository.deleteById(10000000l);
        });
    }
}