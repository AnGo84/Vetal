package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.LaminateDirectory;
import ua.com.vetal.service.LaminateDirectoryServiceImpl;

@Controller
@RequestMapping("/laminate")
@Slf4j
public class LaminateDirectoryController extends AbstractDirectoryController<LaminateDirectory, LaminateDirectoryServiceImpl> {

	public LaminateDirectoryController(LaminateDirectoryServiceImpl service) {
		super(LaminateDirectory.class, ControllerType.LAMINATE, service);
	}
}