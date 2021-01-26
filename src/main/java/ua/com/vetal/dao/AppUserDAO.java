package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.AppUser;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
@Transactional(readOnly = true)
public class AppUserDAO {

    public static final String PREPARED_QUERY = "Select e from %s e " //
            + " Where e.userName = :userName ";

    @PersistenceContext
    private EntityManager entityManager;

    public AppUser findUserAccount(String userName) {
        try {
            String sql = String.format(PREPARED_QUERY, AppUser.class.getName());
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userName", userName);
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
