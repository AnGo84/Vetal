package ua.com.vetal.service.common;

import ua.com.vetal.entity.AbstractDirectoryEntity;
import ua.com.vetal.repositories.CommonDirectoryRepository;

import java.util.Optional;


public abstract class AbstractDirectoryService<E extends AbstractDirectoryEntity, R extends CommonDirectoryRepository<E>>
        extends AbstractService<E, R> {

    public AbstractDirectoryService(R repository) {
        super(repository);
    }

    public Optional<E> getByName(String name) {
        return Optional.of(repository.findByName(name));
    }

    public Boolean isExistByName(String name) {
        return repository.findByName(name) != null;
    }
}
