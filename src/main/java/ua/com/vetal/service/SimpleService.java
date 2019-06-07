package ua.com.vetal.service;

import java.util.List;

public interface SimpleService<T> {

    T findById(Long id);

    T findByName(String name);

    void saveObject(T object);

    void updateObject(T object);

    void deleteById(Long id);

    List<T> findAllObjects();

    boolean isObjectExist(T object);
}
