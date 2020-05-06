package ua.com.vetal.service.common;

import ua.com.vetal.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface CommonService<E extends AbstractEntity> {
    Optional<E> save(E entity);

    List<E> saveAll(List<E> entities);

    Optional<E> update(E entity);

    Optional<E> get(Long id);

    List<E> getAll();

    Boolean deleteById(Long id);

    Boolean deleteAll();
}
