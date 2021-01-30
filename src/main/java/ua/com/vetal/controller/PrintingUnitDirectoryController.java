package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.PrintingUnitDirectory;
import ua.com.vetal.service.PrintingUnitDirectoryServiceImpl;

@Controller
@RequestMapping("/printingUnit")
@Slf4j
public class PrintingUnitDirectoryController extends AbstractDirectoryController<PrintingUnitDirectory, PrintingUnitDirectoryServiceImpl> {

	public PrintingUnitDirectoryController(PrintingUnitDirectoryServiceImpl service) {
		super(PrintingUnitDirectory.class, ControllerType.PRINTING_UNIT, service);
	}
}