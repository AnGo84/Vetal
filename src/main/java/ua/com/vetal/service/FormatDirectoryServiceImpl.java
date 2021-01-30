package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.repositories.FormatDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("formatDirectoryService")
@Transactional
public class FormatDirectoryServiceImpl extends AbstractDirectoryService<FormatDirectory, FormatDirectoryRepository> {

    public FormatDirectoryServiceImpl(FormatDirectoryRepository repository) {
        super(repository);
    }
}