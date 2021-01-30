package ua.com.vetal.service.common;

import lombok.extern.slf4j.Slf4j;
import ua.com.vetal.entity.common.AbstractEmployeeEntity;
import ua.com.vetal.repositories.common.CommonRepository;

@Slf4j
public abstract class AbstractEmployeeService<E extends AbstractEmployeeEntity, R extends CommonRepository<E>>
        extends AbstractService<E, R> {

    public AbstractEmployeeService(R repository) {
        super(repository);
    }

    @Override
    public Boolean isExist(E entity) {
		log.info("Check on exist: {}", entity);
		if (entity == null) {
			return false;
		}
		return get(entity.getId()) != null;
	}
}
