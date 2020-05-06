package ua.com.vetal.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import ua.com.vetal.entity.AbstractDirectoryEntity;

@NoRepositoryBean
public interface CommonDirectoryRepository<E extends AbstractDirectoryEntity> extends CommonRepository<E> {
    E findByName(String name);
}
