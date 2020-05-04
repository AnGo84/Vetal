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

        if (orderViewFilter != null) {
            if (orderViewFilter.getAccount() != null && !orderViewFilter.getAccount().equals("")) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("account")),
                        ("%" + orderViewFilter.getAccount() + "%").toLowerCase()));

            }

            if (orderViewFilter.getNumber() != null && !orderViewFilter.getNumber().equals("")) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullNumber")),
                        ("%" + orderViewFilter.getNumber() + "%").toLowerCase()));

            }
            if (orderViewFilter.getClient() != null && orderViewFilter.getClient().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("client"), orderViewFilter.getClient()));
            }

            if (orderViewFilter.getPrinter() != null && orderViewFilter.getPrinter().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("printer"), orderViewFilter.getPrinter()));
            }

            if (orderViewFilter.getManager() != null && orderViewFilter.getManager().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("manager"), orderViewFilter.getManager()));
            }

            if (orderViewFilter.getPaper() != null && orderViewFilter.getPaper().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("paper"), orderViewFilter.getPaper()));
            }

            if (orderViewFilter.getProduction() != null && orderViewFilter.getProduction().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("production"), orderViewFilter.getProduction()));
            }
            if (orderViewFilter.getDateBeginFrom() != null) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(root.get("dateBegin"), orderViewFilter.getDateBeginFrom()));
            }

            if (orderViewFilter.getDateBeginTill() != null) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(root.get("dateBegin"), orderViewFilter.getDateBeginTill()));
            }
        }
        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();
        return list;
    }
}
