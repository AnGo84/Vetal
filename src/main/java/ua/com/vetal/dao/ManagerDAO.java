package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class ManagerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * public User findUserAccount(String name) { try { String sql =
     * "Select e from " + PaperDirectory.class.getName() + " e " // +
     * " Where e.name = :name "; // System.out.println(sql); Query query =
     * entityManager.createQuery(sql, PaperDirectory.class);
     * query.setParameter("name", name);
     *
     * return (User) query.getSingleResult(); } catch (NoResultException e) {
     * return null; } }
     */

}
