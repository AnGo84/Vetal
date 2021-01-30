package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.AppUserRole;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AppRoleDAO {

    public static final String PREPARED_QUERY = "Select ur.appRole.roleName from %s ur " //
            + " where ur.appUser.userId = :userId ";

    @PersistenceContext
    private EntityManager entityManager;

    public List<String> getRoleNames(Long userId) {
        String sql = String.format(PREPARED_QUERY, AppUserRole.class.getName());
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
