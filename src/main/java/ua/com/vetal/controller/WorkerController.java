package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractEmployeeController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.Worker;
import ua.com.vetal.service.WorkerServiceImpl;

@Controller
@RequestMapping("/worker")
@Slf4j
public class WorkerController extends AbstractEmployeeController<Worker, WorkerServiceImpl> {

    public WorkerController(WorkerServiceImpl service) {
        super(Worker.class, ControllerType.WORKER, service);
    }
}