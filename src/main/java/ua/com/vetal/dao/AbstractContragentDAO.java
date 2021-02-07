package ua.com.vetal.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.NoRepositoryBean;
import ua.com.vetal.entity.common.AbstractContragentEntity;
import ua.com.vetal.entity.filter.ContragentViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@NoRepositoryBean
@RequiredArgsConstructor
public abstract class AbstractContragentDAO<E extends AbstractContragentEntity> {

    private final Class<E> objectClass;

    @PersistenceContext
    private EntityManager entityManager;

    public List<E> findByFilterData(ContragentViewFilter filterData) {
        if (filterData == null) {
            return new ArrayList<>();
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = builder.createQuery(objectClass);
        Root<E> root = query.from(objectClass);

        Predicate predicate = filterData.getPredicate(builder, root);
        query.where(predicate);
        //query.orderBy(builder.desc(root.get("fullName")));
        List<E> list = entityManager.createQuery(query).getResultList();

        return list;
    }
}
