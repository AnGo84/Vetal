package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.FilterData;

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

   /* public User findStencilByAccount(String account) {
        try {
            String sql = "Select e from " + Stencil.class.getName() + " e " //
                    + " Where e.account = :account ";
            System.out.println(sql);
            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("account", account);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }*/

    public Long getMaxID() {
        try {
            String sql = "Select max(e.id) from " + Stencil.class.getName() + " e ";
            //System.out.println(sql);
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

    public List<Stencil> findByFilterData(FilterData filterData) {
        List<Stencil> list = null;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stencil> query = builder.createQuery(Stencil.class);
        Root<Stencil> root = query.from(Stencil.class);

        Predicate predicate = builder.conjunction();

        if (filterData != null) {
            if (filterData.getAccount() != null && !filterData.getAccount().equals("")) {
//			predicate = builder.and(predicate, builder.equal(root.get("account"), filterData.getAccount()));
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("account")),
                        ("%" + filterData.getAccount() + "%").toLowerCase()));

            }

            if (filterData.getNumber() != null && !filterData.getNumber().equals("")) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullNumber")),
                        ("%" + filterData.getNumber() + "%").toLowerCase()));

            }

		/*if (filterData.getFileName() != null && !filterData.getFileName().equals("")) {
			predicate = builder.and(predicate, builder.like(builder.lower(root.get("fileName")),
					("%" + filterData.getFileName() + "%").toLowerCase()));
		}*/

            if (filterData.getClient() != null && filterData.getClient().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("client"), filterData.getClient()));
            }

            if (filterData.getPrinter() != null && filterData.getPrinter().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("printer"), filterData.getPrinter()));
            }

            if (filterData.getManager() != null && filterData.getManager().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("manager"), filterData.getManager()));
            }

            if (filterData.getPaper() != null && filterData.getPaper().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("paper"), filterData.getPaper()));
            }

            if (filterData.getProduction() != null && filterData.getProduction().getId() != null) {
                predicate = builder.and(predicate, builder.equal(root.get("production"), filterData.getProduction()));
            }
            if (filterData.getDateBeginFrom() != null) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(root.get("dateBegin"), filterData.getDateBeginFrom()));
            }

            if (filterData.getDateBeginTill() != null) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(root.get("dateBegin"), filterData.getDateBeginTill()));
            }
        }
        query.where(predicate);
        query.orderBy(builder.desc(root.get("dateBegin")));

        list = entityManager.createQuery(query).getResultList();

        // https://www.baeldung.com/rest-search-language-spring-jpa-criteria
        // http://qaru.site/questions/293915/spring-data-jpa-query-by-example
        return list;
    }
}
