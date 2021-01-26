package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.StockDirectory;
import ua.com.vetal.repositories.StockDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("stockDirectoryService")
@Transactional
public class StockDirectoryServiceImpl extends AbstractDirectoryService<StockDirectory, StockDirectoryRepository> {
    public StockDirectoryServiceImpl(StockDirectoryRepository repository) {
        super(repository);
    }
}