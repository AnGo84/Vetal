package ua.com.vetal.repositories;

import org.springframework.stereotype.Repository;
import ua.com.vetal.entity.Worker;
import ua.com.vetal.repositories.common.CommonRepository;

@Repository
public interface WorkerRepository extends CommonRepository<Worker> {

}
