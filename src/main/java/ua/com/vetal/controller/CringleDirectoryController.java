package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.CringleDirectory;
import ua.com.vetal.service.CringleDirectoryServiceImpl;

@Controller
@RequestMapping("/cringle")
@Slf4j
public class CringleDirectoryController extends AbstractDirectoryController<CringleDirectory, CringleDirectoryServiceImpl> {

	public CringleDirectoryController(CringleDirectoryServiceImpl service) {
		super(CringleDirectory.class, ControllerType.CRINGLE, service);
	}
}