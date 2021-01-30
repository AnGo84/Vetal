package ua.com.vetal.repositories.common;

import org.springframework.data.repository.NoRepositoryBean;
import ua.com.vetal.entity.common.AbstractDirectoryEntity;

@NoRepositoryBean
public interface CommonDirectoryRepository<E extends AbstractDirectoryEntity> extends CommonRepository<E> {
    E findByName(String name);
    //List<E> findAllByOrderByNameAsc();
}
