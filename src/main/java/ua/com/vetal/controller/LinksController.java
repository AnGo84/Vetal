package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.vetal.entity.Link;
import ua.com.vetal.entity.LinkType;
import ua.com.vetal.service.LinkServiceImpl;
import ua.com.vetal.service.LinkTypeServiceImpl;
import ua.com.vetal.utils.LoggerUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RequestMapping("/links")
@Controller
@Slf4j

public class LinksController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LinkServiceImpl linkService;
    @Autowired
    private LinkTypeServiceImpl linkTypeService;

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String mainPage(Model model) {
        model.addAttribute("title", "links");
        model.addAttribute("linksList", linkService.findAllObjects());
        return "linksPage";
    }


    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddLinkPage(Model model) {
        log.info("Add new link record");
        Link link = new Link();

        model.addAttribute("edit", false);
        model.addAttribute("link", link);
        return "linkPage";

    }

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editLink(@PathVariable Long id, Model model) {
        log.info("Edit link with ID= " + id);
        model.addAttribute("edit", true);
        model.addAttribute("link", linkService.findById(id));
        return "linkPage";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateLink(@Valid @ModelAttribute("link") Link link, BindingResult bindingResult,
                             Model model) {
        log.info("Update link: " + link);
        if (bindingResult.hasErrors()) {
            LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
            return "linkPage";
        }

        if (linkService.isObjectExist(link)) {
            FieldError fieldError =
                    new FieldError("Link", "fullName",
                            messageSource.getMessage("non.unique.field", new String[]{
                                    link.getFullName()}, Locale.getDefault()));
            bindingResult.addError(fieldError);
            return "linkPage";
        }

        linkService.saveObject(link);
        return "redirect:/links";
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deleteLink(@PathVariable Long id) {
        log.info("Delete link with ID= " + id);
        linkService.deleteById(id);
        return "redirect:/links";
    }

    /**
     * This method will provide LinkType list to views
     */
    @ModelAttribute("linkTypesList")
    public List<LinkType> initializeLinkTypes() {
        return linkTypeService.findAllObjects();
    }
}
