package ua.com.vetal.service.common;

import lombok.RequiredArgsConstructor;
import ua.com.vetal.entity.AbstractEntity;
import ua.com.vetal.exception.EntityErrorType;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractService<E extends AbstractEntity, R extends CommonRepository<E>>
        implements CommonService<E> {

    protected final R repository;

    @Override
    public Optional<E> save(E entity) {
        return Optional.of(repository.save(entity));
    }

    @Override
    public List<E> saveAll(List<E> entities) {
        List<E> result = getListFromIterable(repository.saveAll(entities));
        return result;
    }

    @Override
    public Optional<E> update(E entity) {
        return Optional.of(repository.save(entity));
    }

    @Override
    public Optional<E> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<E> getAll() {
        List<E> result = getListFromIterable(repository.findAll());
        return result;
    }

    @Override
    public Boolean deleteById(Long id) {
        E entity = get(id)
                .orElseThrow(() -> new EntityException(String.format(EntityErrorType.ENTITY_NOT_FOUND.getDescription(), id)));
        repository.delete(entity);
        return !repository.findById(entity.getId()).isPresent();
    }

    @Override
    public Boolean deleteAll() {
        repository.deleteAll();
        List<E> result = getListFromIterable(repository.findAll());
        return result.isEmpty();
    }

    private List<E> getListFromIterable(Iterable<E> iterable) {
        List<E> result = new ArrayList<E>();
        iterable.forEach(result::add);
        return result;
    }

}
