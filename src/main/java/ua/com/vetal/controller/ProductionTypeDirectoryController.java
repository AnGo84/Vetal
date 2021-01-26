package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.service.ProductionTypeDirectoryServiceImpl;

@Controller
@RequestMapping("/productionType")
@Slf4j
public class ProductionTypeDirectoryController extends AbstractDirectoryController<ProductionTypeDirectory, ProductionTypeDirectoryServiceImpl> {

	public ProductionTypeDirectoryController(ProductionTypeDirectoryServiceImpl service) {
		super(ProductionTypeDirectory.class, ControllerType.PRODUCTION_TYPE, service);
	}
}