package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.service.ChromaticityDirectoryServiceImpl;

@Controller
@RequestMapping("/chromaticity")
@Slf4j
public class ChromaticityDirectoryController extends AbstractDirectoryController<ChromaticityDirectory, ChromaticityDirectoryServiceImpl> {
    public ChromaticityDirectoryController(ChromaticityDirectoryServiceImpl chromaticityDirectoryService) {
        super(ChromaticityDirectory.class, ControllerType.CHROMATICITY, chromaticityDirectoryService);
    }
}
