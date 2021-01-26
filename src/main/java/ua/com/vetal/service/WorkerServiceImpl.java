package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Worker;
import ua.com.vetal.repositories.WorkerRepository;
import ua.com.vetal.service.common.AbstractEmployeeService;

@Service("workerService")
@Transactional
public class WorkerServiceImpl extends AbstractEmployeeService<Worker, WorkerRepository> {

	public WorkerServiceImpl(WorkerRepository repository) {
		super(repository);
	}
}