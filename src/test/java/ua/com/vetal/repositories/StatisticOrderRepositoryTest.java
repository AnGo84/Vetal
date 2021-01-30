package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.StatisticOrder;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StatisticOrderRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StencilRepository stencilRepository;

    private Task task;
    private Stencil stencil;

    @BeforeEach
    public void beforeEach() {
        taskRepository.deleteAll();
        stencilRepository.deleteAll();

        task = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 1), testEntityManager);
        task = testEntityManager.persistAndFlush(task);
        testEntityManager.persistAndFlush(TestBuildersUtils.getOrderFromTask(task));
        stencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 1), testEntityManager);
        stencil = testEntityManager.persistAndFlush(stencil);

        testEntityManager.persistAndFlush(TestBuildersUtils.getOrderFromStencil(stencil));
    }

    @Test
    public void whenFindAll_thenReturnListOfManagers() {
        List<StatisticOrder> statisticOrders = orderRepository.findAll();

        List<Task> tasks = taskRepository.findAll();
        assertEquals(tasks.size(), 1);

        List<Stencil> stencils = stencilRepository.findAll();
        assertEquals(stencils.size(), 1);

        assertNotNull(statisticOrders);
        assertFalse(statisticOrders.isEmpty());
        assertEquals(statisticOrders.size(), 2);

    }

    @Test
    public void whenFindAllWithSortOption_thenReturnListOfManagers() {
        List<StatisticOrder> statisticOrders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "dateBegin"));

        List<Task> tasks = taskRepository.findAll(Sort.by(Sort.Direction.ASC, "dateBegin"));
        assertEquals(tasks.size(), 1);

        List<Stencil> stencils = stencilRepository.findAll(Sort.by(Sort.Direction.ASC, "dateBegin"));
        assertEquals(stencils.size(), 1);

        assertNotNull(statisticOrders);
        assertFalse(statisticOrders.isEmpty());
        assertEquals(statisticOrders.size(), 2);
    }

}