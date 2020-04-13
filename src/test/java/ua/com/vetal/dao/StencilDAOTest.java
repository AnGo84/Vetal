package ua.com.vetal.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.repositories.StencilRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ComponentScan("ua.com.vetal.dao")
public class StencilDAOTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private StencilRepository stencilRepository;
    @Autowired
    private StencilDAO stencilDAO;

    private Stencil stencil;

    @BeforeEach
    public void beforeEach() {
        stencilRepository.deleteAll();
        stencil = testEntityManager.persistAndFlush(
                TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 1), testEntityManager));
        stencil = testEntityManager.persistAndFlush(
                TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), testEntityManager));
    }

    @Test
    public void whenGetMaxID_thenReturnNewID() {
        Long newID = stencilDAO.getMaxID();
        assertNotNull(newID);
        assertEquals(stencil.getId(), newID);
    }
}