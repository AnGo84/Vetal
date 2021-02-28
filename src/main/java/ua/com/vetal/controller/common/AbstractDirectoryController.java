package ua.com.vetal.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.com.vetal.entity.common.AbstractDirectoryEntity;
import ua.com.vetal.service.common.CommonService;
import ua.com.vetal.utils.LoggerUtils;

import javax.validation.Valid;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractDirectoryController<E extends AbstractDirectoryEntity, S extends CommonService<E>>
        implements CommonController<E> {
    private final String DIRECTORY_LIST_PAGE = "directoryPage";
    private final String DIRECTORY_RECORD_PAGE = "directoryRecordPage";
    private final Class<E> objectClass;

    private final ControllerType controllerType;
    private final S service;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String allRecords(Model model) {
        model.addAttribute("directoryList", service.getAll());
        return DIRECTORY_LIST_PAGE;
    }

    @Override
    public String addRecord(Model model) {
        log.info("Add new '{}' record", objectClass);
        model.addAttribute("edit", false);
        model.addAttribute("directory", createInstance(objectClass));
        return DIRECTORY_RECORD_PAGE;
    }

    @Override
    public String editRecord(Long id, Model model) {
        log.info("Edit '{}' with ID= {}", objectClass, id);
        model.addAttribute("edit", true);
        model.addAttribute("directory", service.get(id));
        return DIRECTORY_RECORD_PAGE;
    }

    @Override
    public String updateRecord(@Valid @ModelAttribute("directory") E directory, BindingResult bindingResult, Model model, Locale locale) {
        log.info("Update '{}': {}", objectClass, directory);
        if (bindingResult.hasErrors()) {
            LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
            return DIRECTORY_RECORD_PAGE;
        }
        if (service.isExist(directory)) {
            log.error("Directory '{}' exist", directory.getName());
            FieldError fieldError = new FieldError("directory", "name", messageSource.getMessage("non.unique.field",
                    new String[]{"Название", directory.getName()}, locale));
            bindingResult.addError(fieldError);
            return DIRECTORY_RECORD_PAGE;
        }

        E updated = service.update(directory);
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

    @ModelAttribute("directoryName")
    public String initializeDirectoryName(Locale locale) {
        String name = messageSource.getMessage(controllerType.getMessageLabel(), null, locale);
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
