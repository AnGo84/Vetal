package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.StockDirectory;
import ua.com.vetal.service.StockDirectoryServiceImpl;

@Controller
@RequestMapping("/stock")
@Slf4j
public class StockDirectoryController extends AbstractDirectoryController<StockDirectory, StockDirectoryServiceImpl> {

	public StockDirectoryController(StockDirectoryServiceImpl service) {
		super(StockDirectory.class, ControllerType.STOCK, service);
	}
}