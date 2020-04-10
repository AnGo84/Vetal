package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OrderRepositoryTest {
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
        testEntityManager.persistAndFlush(getOrder(task));
        stencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 1), testEntityManager);
        stencil = testEntityManager.persistAndFlush(stencil);

        testEntityManager.persistAndFlush(getOrder(stencil));
    }

    @Test
    public void whenFindAll_thenReturnListOfManagers() {
        List<Order> orders = orderRepository.findAll();
        System.out.println("Orders: " + orders.size());

        List<Task> tasks = taskRepository.findAll();
        System.out.println("Tasks: " + tasks.size());

        List<Stencil> stencils = stencilRepository.findAll();
        System.out.println("Stencils: " + stencils.size());

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(orders.size(), 2);

    }

    @Test
    public void whenFindAllWithSortOption_thenReturnListOfManagers() {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.ASC, "dateBegin"));

        System.out.println("Orders: " + orders.size());

        List<Task> tasks = taskRepository.findAll(Sort.by(Sort.Direction.ASC, "dateBegin"));
        System.out.println("Tasks: " + tasks.size());

        List<Stencil> stencils = stencilRepository.findAll(Sort.by(Sort.Direction.ASC, "dateBegin"));
        System.out.println("Stencils: " + stencils.size());

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(orders.size(), 2);
    }

    private Order getOrder(Task task) {
        Order order = new Order();
        order.setId(task.getId());
        order.setAmount(task.getAmountForContractor());
        order.setClient(task.getClient());
        order.setDateBegin(task.getDateBegin());
        order.setDebtAmount(task.getDebtAmount());
        order.setFullNumber(task.getFullNumber());
        order.setManager(task.getManager());
        order.setOrderType("task");
        order.setPrinting(task.getPrinting());
        order.setProduction(task.getProduction());
        return order;
    }

    private Order getOrder(Stencil stencil) {
        Order order = new Order();
        order.setId(stencil.getId());
        order.setAmount(stencil.getAmount());
        order.setClient(stencil.getClient());
        order.setDateBegin(stencil.getDateBegin());
        order.setDebtAmount(stencil.getDebtAmount());
        order.setFullNumber(stencil.getFullNumber());
        order.setManager(stencil.getManager());
        order.setOrderType("stencil");
        order.setPrinting(stencil.getPrinting());
        order.setProduction(stencil.getProduction());
        return order;
    }
}