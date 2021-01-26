package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.NumberBaseDirectory;
import ua.com.vetal.service.NumberBaseDirectoryServiceImpl;

@Controller
@RequestMapping("/numberBases")
@Slf4j
public class NumberBaseDirectoryController extends AbstractDirectoryController<NumberBaseDirectory, NumberBaseDirectoryServiceImpl> {

	public NumberBaseDirectoryController(NumberBaseDirectoryServiceImpl service) {
		super(NumberBaseDirectory.class, ControllerType.NUMBER_BASE, service);
	}
}