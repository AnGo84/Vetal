package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.repositories.ManagerRepository;
import ua.com.vetal.service.common.AbstractEmployeeService;

@Service("managerService")
@Transactional
public class ManagerServiceImpl extends AbstractEmployeeService<Manager, ManagerRepository> {

    public ManagerServiceImpl(ManagerRepository repository) {
        super(repository);
    }
}