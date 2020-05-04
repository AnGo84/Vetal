package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.OrderViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StencilDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Long getMaxID() {
        try {
            String sql = "Select max(e.id) from " + Stencil.class.getName() + " e ";
            Query query = entityManager.createQuery(sql);
            Long nom = (Long) query.getSingleResult();
            if (nom == null) {
                nom = 0L;
            }
            return nom;
        } catch (NoResultException e) {
            return 0L;
        }
    }

    public List<Stencil> findByFilterData(OrderViewFilter orderViewFilter) {
        List<Stencil> list = null;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stencil> query = builder.createQuery(Stencil.class);
        Root<Stencil> root = query.from(Stencil.class);

        Predicate predicate = builder.conjunction();

        if (orderViewFilter != null && orderViewFilter.hasData()) {
            predicate = orderViewFilter.getPredicate(builder, root, predicate);
        }
        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();
        return list;
    }
}
