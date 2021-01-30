package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.PrintingUnitDirectory;
import ua.com.vetal.repositories.PrintingUnitDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("printingUnitDirectoryService")
@Transactional
public class PrintingUnitDirectoryServiceImpl extends AbstractDirectoryService<PrintingUnitDirectory, PrintingUnitDirectoryRepository> {

    public PrintingUnitDirectoryServiceImpl(PrintingUnitDirectoryRepository repository) {
        super(repository);
    }
}