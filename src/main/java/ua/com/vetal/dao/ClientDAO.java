package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.filter.ClientViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ClientDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Client> findByFilterData(ClientViewFilter filterData) {
        if (filterData == null) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = builder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        Predicate predicate = filterData.getPredicate(builder, root);
        query.where(predicate);
        //query.orderBy(builder.desc(root.get("fullName")));
        List<Client> list = entityManager.createQuery(query).getResultList();

        return list;
    }
}
