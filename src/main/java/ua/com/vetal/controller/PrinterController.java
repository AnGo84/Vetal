package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractEmployeeController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.Printer;
import ua.com.vetal.service.PrinterServiceImpl;

@Controller
@RequestMapping("/printer")
@Slf4j
public class PrinterController extends AbstractEmployeeController<Printer, PrinterServiceImpl> {

    public PrinterController(PrinterServiceImpl service) {
        super(Printer.class, ControllerType.PRINTER, service);
    }
}