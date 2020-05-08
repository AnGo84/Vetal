package ua.com.vetal.repositories.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ua.com.vetal.entity.common.AbstractEntity;

@NoRepositoryBean
public interface CommonRepository<E extends AbstractEntity> extends JpaRepository<E, Long> {
}
