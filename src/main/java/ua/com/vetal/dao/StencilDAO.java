package ua.com.vetal.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.OrderViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StencilDAO extends OrderDAO<Stencil> {

    @PersistenceContext
    private EntityManager entityManager;

    public StencilDAO() {
        super(Stencil.class);
    }

    public Long getMaxID() {
        return getMaxID(entityManager, Stencil.class.getName());
    }

    public List<Stencil> findByFilterData(OrderViewFilter orderViewFilter) {
        return findByFilterData(entityManager, orderViewFilter);
    }
}
