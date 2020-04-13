package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Stencil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
@Transactional
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
}
