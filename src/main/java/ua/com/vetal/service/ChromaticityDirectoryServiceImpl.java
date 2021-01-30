package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.repositories.ChromaticityDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("chronomaticityDirectoryService")
@Transactional
public class ChromaticityDirectoryServiceImpl extends AbstractDirectoryService<ChromaticityDirectory, ChromaticityDirectoryRepository> {
    public ChromaticityDirectoryServiceImpl(ChromaticityDirectoryRepository repository) {

        super(repository);
    }
}
