package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.LaminateDirectory;
import ua.com.vetal.repositories.LaminateDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("laminateDirectoryService")
@Transactional
public class LaminateDirectoryServiceImpl extends AbstractDirectoryService<LaminateDirectory, LaminateDirectoryRepository> {

    public LaminateDirectoryServiceImpl(LaminateDirectoryRepository repository) {
        super(repository);
    }
}