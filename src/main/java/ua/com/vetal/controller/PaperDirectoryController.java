package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.PaperDirectory;
import ua.com.vetal.service.PaperDirectoryServiceImpl;

@Controller
@RequestMapping("/paper")
@Slf4j
public class PaperDirectoryController extends AbstractDirectoryController<PaperDirectory, PaperDirectoryServiceImpl> {

	public PaperDirectoryController(PaperDirectoryServiceImpl service) {
		super(PaperDirectory.class, ControllerType.PAPER, service);
	}
}