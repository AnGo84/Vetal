package ua.com.vetal.mapper;


public abstract class AbstractMapper<T, E> {

    public abstract E convertToEntity(T object);

    public abstract T convertToObject(E entity);
}
