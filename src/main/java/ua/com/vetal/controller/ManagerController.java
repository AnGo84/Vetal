package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractEmployeeController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.service.ManagerServiceImpl;

@Controller
@RequestMapping("/manager")
@Slf4j
public class ManagerController extends AbstractEmployeeController<Manager, ManagerServiceImpl> {

    public ManagerController(ManagerServiceImpl service) {
        super(Manager.class, ControllerType.MANAGER, service);
    }
}