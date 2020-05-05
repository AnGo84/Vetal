package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.filter.PersonViewFilter;

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
public class ContractorDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Contractor> findByFilterData(PersonViewFilter filterData) {
        if (filterData == null) {
            return new ArrayList<>();
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contractor> query = builder.createQuery(Contractor.class);
        Root<Contractor> root = query.from(Contractor.class);

        Predicate predicate = filterData.getPredicate(builder, root);
        query.where(predicate);
        //query.orderBy(builder.desc(root.get("fullName")));
        List<Contractor> list = entityManager.createQuery(query).getResultList();

		return list;
	}
}
