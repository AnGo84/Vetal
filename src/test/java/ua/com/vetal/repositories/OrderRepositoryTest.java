package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ua.com.vetal.entity.Order;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    public void beforeEach() {
        orderRepository.deleteAll();
        /*order = TestDataUtils.getManager("firstName", "lastName", "middleName", "email");
        order = entityManager.persistAndFlush(order);*/

    }

    @Test
    public void whenFindAll_thenReturnListOfManagers() {
        //given
        /*Manager newManager = TestDataUtils.getManager("firstName2", "lastName2", "middleName2", "email2");
        entityManager.persistAndFlush(newManager);
        // when
        List<Order> orders = orderRepository.findAll();
        // then
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(orders.size(), 2);*/

    }
}