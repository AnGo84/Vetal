package ua.com.vetal.service.common;

import lombok.extern.slf4j.Slf4j;
import ua.com.vetal.entity.AbstractDirectoryEntity;
import ua.com.vetal.repositories.CommonDirectoryRepository;
import ua.com.vetal.utils.StringUtils;

@Slf4j
public abstract class AbstractDirectoryService<E extends AbstractDirectoryEntity, R extends CommonDirectoryRepository<E>>
        extends AbstractService<E, R> {

    public AbstractDirectoryService(R repository) {
        super(repository);
    }

    public E getByName(String name) {
        return repository.findByName(name);
    }

    public Boolean isExistByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return repository.findByName(name) != null;
    }

    @Override
    public Boolean isExist(E entity) {
        log.info("Check on exist: {}", entity);
        if (entity == null) {
            return false;
        }
        E foundEntity = repository.findByName(entity.getName());
        if (foundEntity == null) {
            return false;
        } else if (entity.getId() == null || !entity.getId().equals(foundEntity.getId())) {
            return true;
        }
        return false;
    }
}
