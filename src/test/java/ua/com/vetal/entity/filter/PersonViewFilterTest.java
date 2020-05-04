package ua.com.vetal.entity.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.Manager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PersonViewFilterTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void onHasData() {
        PersonViewFilter personViewFilter = new PersonViewFilter();
        assertFalse(personViewFilter.hasData());
        personViewFilter.setCorpName("");
        assertFalse(personViewFilter.hasData());

        personViewFilter.setCorpName("TestCorpName");
        assertTrue(personViewFilter.hasData());

        personViewFilter.setCorpName(null);
        personViewFilter.setManager(new Manager());
        assertTrue(personViewFilter.hasData());

        personViewFilter.setManager(TestDataUtils.getManager(1l));
        assertTrue(personViewFilter.hasData());

        personViewFilter.setCorpName("TestCorpName");
        personViewFilter.setManager(TestDataUtils.getManager(1l));
        assertTrue(personViewFilter.hasData());
    }


    @Test
    public void onGetDefault() {
        PersonViewFilter personViewFilter = new PersonViewFilter();
        PersonViewFilter defaultPersonViewFilter = (PersonViewFilter) personViewFilter.getDefault();
        assertNotNull(defaultPersonViewFilter);
        assertNull(defaultPersonViewFilter.getCorpName());
        assertNull(defaultPersonViewFilter.getManager());
    }

    @Test
    public void onGetPredicate() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contractor> query = builder.createQuery(Contractor.class);
        Root<Contractor> root = query.from(Contractor.class);
        Predicate predicate = builder.conjunction();

        PersonViewFilter personViewFilter = new PersonViewFilter();
        personViewFilter.setCorpName("TestCorpName");
        personViewFilter.setManager(TestDataUtils.getManager(1l));
        predicate = personViewFilter.getPredicate(builder, root, predicate);
        assertNotNull(predicate);

        personViewFilter.setCorpName(null);
        predicate = personViewFilter.getPredicate(builder, root, predicate);
        assertNotNull(predicate);

        personViewFilter.setCorpName("TestCorpName");
        personViewFilter.setManager(null);
        predicate = personViewFilter.getPredicate(builder, root, predicate);
        assertNotNull(predicate);

        personViewFilter.setCorpName(null);
        personViewFilter.setManager(null);
        predicate = personViewFilter.getPredicate(builder, root, predicate);
        assertNotNull(predicate);
    }
}