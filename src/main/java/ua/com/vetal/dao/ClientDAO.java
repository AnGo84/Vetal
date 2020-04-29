package ua.com.vetal.dao;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.filter.ClientFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ClientDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Client> findByFilterData(ClientFilter filterData) {
        //https://www.baeldung.com/rest-api-search-language-spring-data-specifications
        List<Client> list = null;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = builder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        Predicate predicate = builder.conjunction();
        if (filterData != null) {
            if (!Strings.isBlank(filterData.getFullName())) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullName")),
                        ("%" + filterData.getFullName() + "%").toLowerCase()));
            }

            if (filterData.getManager() != null && filterData.getManager().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
            }
        }
        query.where(predicate);
        //query.orderBy(builder.desc(root.get("fullName")));
        list = entityManager.createQuery(query).getResultList();

        // https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        // http://qaru.site/questions/293915/spring-data-jpa-query-by-example
        return list;
    }


}
