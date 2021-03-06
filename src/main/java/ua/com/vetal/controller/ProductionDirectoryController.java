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
import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.service.ProductionDirectoryServiceImpl;
import ua.com.vetal.service.ProductionTypeDirectoryServiceImpl;
import ua.com.vetal.utils.LoggerUtils;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/productions")
@Slf4j
public class ProductionDirectoryController {
    private String title = "Production";
    private String directoryName = "Production";
    private String pageName = "/productions";

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ProductionDirectoryServiceImpl directoryService;
    @Autowired
    private ProductionTypeDirectoryServiceImpl productionTypeService;

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String directoryList(Model model) {
        model.addAttribute("productionsList", directoryService.findAllObjects());
        return "productionsPage";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddRecordPage(Model model) {
        log.info("Add new production record");
        ProductionDirectory production = new ProductionDirectory();

        model.addAttribute("edit", false);
        model.addAttribute("production", production);
        return "productionPage";
    }

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editRecord(@PathVariable Long id, Model model) {
        log.info("Edit production with ID= {}", id);
        model.addAttribute("edit", true);
        model.addAttribute("production", directoryService.findById(id));
        return "productionPage";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateRecord(@Valid @ModelAttribute("production") ProductionDirectory directory,
                               BindingResult bindingResult, Model model) {
        log.info("Update Production: {}", directory);
        if (bindingResult.hasErrors()) {
            LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
            return "productionPage";
        }

        directoryService.updateObject(directory);
        return "redirect:" + pageName;
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deleteRecord(@PathVariable Long id) {
        log.info("Delete Production with ID= " + id);
        directoryService.deleteById(id);
        return "redirect:" + pageName;
    }

    @ModelAttribute("title")
    public String initializeTitle() {
        return this.title;
    }

    @ModelAttribute("directoryName")
    public String initializeDirectoryName() {
        String name = messageSource.getMessage("label.production", null, new Locale("ru"));
        if (name == null || name.equals("")) {
            return directoryName;
        }
        return name;
    }

    @ModelAttribute("pageName")
    public String initializePageName() {
        return this.pageName;
    }

}
