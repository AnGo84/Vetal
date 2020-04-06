package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.entity.DBFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DBFileRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DBFileRepository fileRepository;

    private DBFile dbFile;

    @BeforeEach
    public void beforeEach() {
        fileRepository.deleteAll();
        dbFile = new DBFile("file1", "content_type", "file data".getBytes());
        dbFile = entityManager.persistAndFlush(dbFile);
    }

    @Test
    public void whenFindByID_thenReturnFile() {
        // when
        Optional<DBFile> foundDBFile = fileRepository.findById(dbFile.getId());
        // then
        assertTrue(foundDBFile.isPresent());
        assertEquals(dbFile, foundDBFile.get());
    }

    @Test
    public void whenFindByIDByWrongID_thenTReturnEmpty() {
        assertFalse(fileRepository.findById(123654l).isPresent());
    }

    @Test
    public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            fileRepository.findById(null);
        });
    }


    @Test
    public void it_should_save_dbFile() {
        DBFile newDBFile = new DBFile("file2", "content_type", "file2 data".getBytes());
        newDBFile = fileRepository.save(newDBFile);
        DBFile foundDBFile = fileRepository.findById(newDBFile.getId()).get();

        // then
        assertNotNull(foundDBFile);
        assertNotNull(foundDBFile.getId());
        assertEquals(newDBFile.getId(), foundDBFile.getId());
        assertEquals(newDBFile.getFileName(), foundDBFile.getFileName());
        assertEquals(newDBFile.getFileType(), foundDBFile.getFileType());
        assertEquals(newDBFile.getData(), foundDBFile.getData());
    }

    @Test
    public void whenSaveNullDBFile_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            fileRepository.save(null);
        });
    }

    @Test
    public void whenDeleteById_thenOk() {
        //given
        DBFile newDBFile = new DBFile("file2", "content_type", "file2 data".getBytes());
        newDBFile = entityManager.persistAndFlush(newDBFile);
        assertEquals(fileRepository.findAll().size(), 2);
        assertTrue(fileRepository.findById(newDBFile.getId()).isPresent());

        // when
        fileRepository.deleteById(newDBFile.getId());
        // then

        assertEquals(fileRepository.findAll().size(), 1);
        assertFalse(fileRepository.findById(newDBFile.getId()).isPresent());
    }

    @Test
    public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            fileRepository.deleteById(10000000l);
        });
    }
}