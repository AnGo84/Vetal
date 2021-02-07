package ua.com.vetal.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.com.vetal.entity.common.AbstractEmployeeEntity;
import ua.com.vetal.service.common.CommonService;
import ua.com.vetal.utils.LoggerUtils;

import javax.validation.Valid;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEmployeeController<E extends AbstractEmployeeEntity, S extends CommonService<E>>
        implements CommonController<E> {
    private final String EMPLOYEE_LIST_PAGE = "employeesPage";
    private final String EMPLOYEE_RECORD_PAGE = "employeeRecordPage";
    private final Class<E> objectClass;

    private final ControllerType controllerType;
    private final S service;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String allRecords(Model model) {
        model.addAttribute("employeeList", service.getAll());
        return EMPLOYEE_LIST_PAGE;
    }

    @Override
    public String addRecord(Model model) {
        log.info("Add new '{}' record", objectClass);
        model.addAttribute("edit", false);
        model.addAttribute("employee", createInstance(objectClass));
        return EMPLOYEE_RECORD_PAGE;
    }

    @Override
    public String editRecord(Long id, Model model) {
        log.info("Edit '{}' with ID= {}", objectClass, id);
        model.addAttribute("edit", true);
        model.addAttribute("employee", service.get(id));
        return EMPLOYEE_RECORD_PAGE;
    }

    @Override
    public String updateRecord(@Valid @ModelAttribute("employee") E employee, BindingResult bindingResult, Model model) {
        log.info("Update '{}': {}", objectClass, employee);
        if (bindingResult.hasErrors()) {
            LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
            return EMPLOYEE_RECORD_PAGE;
        }
        E updated = service.update(employee);
        log.info("Updated: {}", updated);
        return "redirect:" + controllerType.getPageLink();
    }

    @Override
    public String deleteRecord(Long id) {
        log.info("Delete {} with ID= {}", objectClass, id);
        service.deleteById(id);
        return "redirect:" + controllerType.getPageLink();
    }

    @ModelAttribute("title")
    public String initializeTitle() {
        return controllerType.getTitle();
    }

    @ModelAttribute("employeePageName")
    public String initializePageName() {
        String name = messageSource.getMessage(controllerType.getMessageLabel(), null, new Locale("ru"));
        if (name == null || name.equals("")) {
            return controllerType.getPageName();
        }
        return name;
    }

    @ModelAttribute("pageLink")
    public String initializePageLink() {
        return controllerType.getPageLink();
    }
}
