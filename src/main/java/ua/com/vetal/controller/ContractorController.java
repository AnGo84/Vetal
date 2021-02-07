package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractContragentController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.service.ContractorServiceImpl;

import java.util.Map;

@Controller
@RequestMapping("/contractor")
@Slf4j
public class ContractorController extends AbstractContragentController<Contractor, ContractorServiceImpl> {
    public ContractorController(ContractorServiceImpl service, Map<String, ViewFilter> viewFilterMap) {
        super(Contractor.class, ControllerType.CONTRACTOR, service, viewFilterMap);
    }
}
