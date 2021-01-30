package ua.com.vetal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.repositories.ProductionTypeDirectoryRepository;
import ua.com.vetal.service.common.AbstractDirectoryService;

@Service("productionTypeDirectoryService")
@Transactional
public class ProductionTypeDirectoryServiceImpl extends AbstractDirectoryService<ProductionTypeDirectory, ProductionTypeDirectoryRepository> {

    public ProductionTypeDirectoryServiceImpl(ProductionTypeDirectoryRepository repository) {
        super(repository);
    }
}