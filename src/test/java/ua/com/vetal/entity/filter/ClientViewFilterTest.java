package ua.com.vetal.entity.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.Manager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ClientViewFilterTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void onHasData() {
        ClientViewFilter clientViewFilter = new ClientViewFilter();
        assertFalse(clientViewFilter.hasData());
        clientViewFilter.setFullName("");
        assertFalse(clientViewFilter.hasData());

        clientViewFilter.setFullName("TestFullName");
        assertTrue(clientViewFilter.hasData());

        clientViewFilter.setFullName(null);
        clientViewFilter.setManager(new Manager());
        assertTrue(clientViewFilter.hasData());

        clientViewFilter.setManager(TestDataUtils.getManager(1l));
        assertTrue(clientViewFilter.hasData());

        clientViewFilter.setFullName("TestFullName");
        clientViewFilter.setManager(TestDataUtils.getManager(1l));
        assertTrue(clientViewFilter.hasData());
    }

    @Test
    public void onGetDefault() {
        ClientViewFilter clientViewFilter = new ClientViewFilter();
        ClientViewFilter defaultClientViewFilter = (ClientViewFilter) clientViewFilter.getDefault();
        assertNotNull(defaultClientViewFilter);
        assertNull(defaultClientViewFilter.getFullName());
        assertNull(defaultClientViewFilter.getManager());
    }

    @Test
    public void onGetPredicate() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = builder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        ClientViewFilter clientViewFilter = new ClientViewFilter();
        clientViewFilter.setFullName("TestFullName");
        clientViewFilter.setManager(TestDataUtils.getManager(1l));
        Predicate predicate = clientViewFilter.getPredicate(builder, root);
        assertNotNull(predicate);

        clientViewFilter.setFullName(null);
        predicate = clientViewFilter.getPredicate(builder, root);
        assertNotNull(predicate);

        clientViewFilter.setFullName("TestFullName");
        clientViewFilter.setManager(null);
        predicate = clientViewFilter.getPredicate(builder, root);
        assertNotNull(predicate);

        clientViewFilter.setFullName(null);
        clientViewFilter.setManager(null);
        predicate = clientViewFilter.getPredicate(builder, root);
        assertNotNull(predicate);
    }
}