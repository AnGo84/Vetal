package ua.com.vetal.service.common;

import ua.com.vetal.entity.common.AbstractEntity;

import java.util.List;

public interface CommonService<E extends AbstractEntity> {
    E save(E entity);

    List<E> saveAll(List<E> entities);

    E update(E entity);

    E get(Long id);

    List<E> getAll();

    Boolean deleteById(Long id);

    Boolean deleteAll();

    Boolean isExist(E entity);
}
