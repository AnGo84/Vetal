package ua.com.vetal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class StencilDAO {

    @Autowired
    private EntityManager entityManager;

    public User findStencilByAccount(String account) {
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
    }

    public Long getMaxID() {
        try {
            String sql = "Select max(e.id) from " + Stencil.class.getName() + " e ";
            //System.out.println(sql);
            Query query = entityManager.createQuery(sql);
            Long nom = (Long) query.getSingleResult();
            return nom;
        } catch (NoResultException e) {
            return 0L;
        }
    }
}
