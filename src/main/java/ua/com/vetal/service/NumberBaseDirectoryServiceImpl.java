package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.NumberBaseDirectory;
import ua.com.vetal.repositories.NumberBaseDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("numberBaseDirectoryService")
@Transactional
public class NumberBaseDirectoryServiceImpl extends AbstractDirectoryService<NumberBaseDirectory, NumberBaseDirectoryRepository> {

    public NumberBaseDirectoryServiceImpl(NumberBaseDirectoryRepository repository) {
        super(repository);
    }
}