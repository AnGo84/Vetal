package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.PaperDirectory;
import ua.com.vetal.repositories.PaperDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("paperDirectoryService")
@Transactional
public class PaperDirectoryServiceImpl extends AbstractDirectoryService<PaperDirectory, PaperDirectoryRepository> {

    public PaperDirectoryServiceImpl(PaperDirectoryRepository repository) {
        super(repository);
    }
}