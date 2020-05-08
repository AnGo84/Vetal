package ua.com.vetal.service.common;

import lombok.extern.slf4j.Slf4j;
import ua.com.vetal.dao.AbstractContragentDAO;
import ua.com.vetal.entity.common.AbstractContragentEntity;
import ua.com.vetal.entity.filter.ContragentViewFilter;
import ua.com.vetal.repositories.common.CommonContragentRepository;

import java.util.List;


@Slf4j
public abstract class AbstractContragentService<E extends AbstractContragentEntity, R extends CommonContragentRepository<E>>
        extends AbstractService<E, R> {

    private final AbstractContragentDAO<E> contragentDAO;

    public AbstractContragentService(R repository, AbstractContragentDAO<E> contragentDAO) {
        super(repository);
        this.contragentDAO = contragentDAO;
    }

    public E findByCorpName(String name) {
        return repository.findByCorpName(name);
    }

    public List<E> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    /*public Boolean isExistByCorpName(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return repository.findByCorpName(name) != null;
    }*/

    @Override
    public Boolean isExist(E entity) {
        log.info("Check on exist: {}", entity);
        if (entity == null) {
            return false;
        }
        E foundEntity = repository.findByCorpName(entity.getCorpName());
        if (foundEntity == null) {
            return false;
        } else if (entity.getId() == null || !entity.getId().equals(foundEntity.getId())) {
            return true;
        }
        return false;
    }

    public List<E> findByFilterData(ContragentViewFilter filterData) {
        List<E> list = contragentDAO.findByFilterData(filterData);

        if (filterData == null) {
            return getAll();
        }
        return list;
    }
}
