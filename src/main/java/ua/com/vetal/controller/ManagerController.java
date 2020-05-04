package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.service.ManagerServiceImpl;
import ua.com.vetal.utils.LoggerUtils;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/manager")
@Slf4j
public class ManagerController {
    @Autowired
    private MessageSource messageSource;
    private String title = "Manager";
    private String personName = "Manager";
    private String pageName = "/manager";
    @Autowired
    private ManagerServiceImpl personService;

    @RequestMapping(value = {"", "/list"}, method = RequestMethod.GET)
    public String personList(Model model) {
        model.addAttribute("personList", personService.findAllObjects());
        return "personsPage";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddPersonPage(Model model) {
        log.info("Add new " + title + " record");
        Manager person = new Manager();

        model.addAttribute("edit", false);
        model.addAttribute("person", person);
        return "personRecordPage";

    }

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editPerson(@PathVariable Long id, Model model) {
        log.info("Edit " + title + " with ID= " + id);
        model.addAttribute("edit", true);
        model.addAttribute("person", personService.findById(id));
        return "personRecordPage";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updatePerson(@Valid @ModelAttribute("person") Manager person, BindingResult bindingResult,
                               Model model) {
        log.info("Update " + title + ": " + person);
        if (bindingResult.hasErrors()) {
            LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
            return "personRecordPage";
        }

        personService.saveObject(person);
        return "redirect:" + pageName;
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deletePerson(@PathVariable Long id) {
        log.info("Delete " + title + " with ID= " + id);
        personService.deleteById(id);
        return "redirect:" + pageName;
    }

    /**
     * This method will provide Title to views
     */
    @ModelAttribute("title")
    public String initializeTitle() {
        return this.title;
    }

    @ModelAttribute("personName")
    public String initializePersonName() {
        String name = messageSource.getMessage("label.manager", null, new Locale("ru"));
        if (name == null || name.equals("")) {
            return personName;
        }
        return name;
    }

    @ModelAttribute("pageName")
    public String initializePageName() {
        return this.pageName;
    }
}
