package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.service.FormatDirectoryServiceImpl;

@Controller
@RequestMapping("/format")
@Slf4j
public class FormatDirectoryController extends AbstractDirectoryController<FormatDirectory, FormatDirectoryServiceImpl> {

	public FormatDirectoryController(FormatDirectoryServiceImpl service) {
		super(FormatDirectory.class, ControllerType.FORMAT, service);
	}
}