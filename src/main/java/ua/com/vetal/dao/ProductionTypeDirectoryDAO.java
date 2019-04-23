package ua.com.vetal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class ProductionTypeDirectoryDAO {

    @Autowired
    private EntityManager entityManager;

    /*
     * public User findUserAccount(String name) { try { String sql =
     * "Select e from " + ProductionDirectory.class.getName() + " e " // +
     * " Where e.name = :name "; System.out.println(sql); Query query =
     * entityManager.createQuery(sql, ProductionDirectory.class);
     * query.setParameter("name", name);
     *
     * return (User) query.getSingleResult(); } catch (NoResultException e) {
     * return null; } }
     */

}
