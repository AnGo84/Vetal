package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.CringleDirectory;
import ua.com.vetal.repositories.CringleDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("cringleDirectoryService")
@Transactional
public class CringleDirectoryServiceImpl extends AbstractDirectoryService<CringleDirectory, CringleDirectoryRepository> {

    public CringleDirectoryServiceImpl(CringleDirectoryRepository repository) {
        super(repository);
    }
}
