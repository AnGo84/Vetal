package ua.com.vetal.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.vetal.entity.common.AbstractEntity;
import ua.com.vetal.exception.EntityErrorType;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.common.CommonRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractService<E extends AbstractEntity, R extends CommonRepository<E>>
        implements CommonService<E> {

    protected final R repository;
    private CommonOptional<E> commonOptional = new CommonOptional<>();

    @Override
    public E save(E entity) {
        log.info("Save entity: {}", entity);
        E savedEntity = repository.save(entity);
        return savedEntity;
        //return repository.save(entity);
    }

    @Override
    public List<E> saveAll(List<E> entities) {
        /*List<E> result = commonOptional.getListFromIterable(repository.saveAll(entities));
        return result;*/
        return repository.saveAll(entities);
    }

    @Override
    public E update(E entity) {
        return repository.save(entity);
    }

    @Override
    public E get(Long id) {
        Optional<E> entity = repository.findById(id);
        if (entity.isPresent()) {
            return entity.get();
        }
        return null;
    }

    @Override
    public List<E> getAll() {
        return repository.findAll();
        /*List<E> result = commonOptional.getListFromIterable(repository.findAll());
        return result;*/
    }

    @Override
    public Boolean deleteById(Long id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new EntityException(String.format(EntityErrorType.ENTITY_NOT_FOUND.getDescription(), id)));
        repository.deleteById(id);
        return !repository.findById(entity.getId()).isPresent();
    }

    @Override
    public Boolean deleteAll() {
        repository.deleteAll();
        /*List<E> result = commonOptional.getListFromIterable(repository.findAll());
        return result.isEmpty();*/
        return repository.findAll().isEmpty();
    }
}
