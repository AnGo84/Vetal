package ua.com.vetal.repositories.common;

import org.springframework.data.repository.NoRepositoryBean;
import ua.com.vetal.entity.common.AbstractContragentEntity;

import java.util.List;

@NoRepositoryBean
public interface CommonContragentRepository<E extends AbstractContragentEntity> extends CommonRepository<E> {

    List<E> findByEmail(String email);

    E findByCorpName(String corpName);
}
