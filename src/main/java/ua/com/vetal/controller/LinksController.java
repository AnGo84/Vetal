package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.vetal.entity.Link;
import ua.com.vetal.entity.LinkType;
import ua.com.vetal.service.LinkServiceImpl;
import ua.com.vetal.service.LinkTypeServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/links")
@Controller
//@SessionAttributes({"managersFilterList", "clientFilterList", "contractorFilterList"})

public class LinksController {
    static final Logger logger = LoggerFactory.getLogger(LinksController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LinkServiceImpl linkService;
    @Autowired
    private LinkTypeServiceImpl linkTypeService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String mainPage(Model model) {
        model.addAttribute("title", "links");
        model.addAttribute("linksList", linkService.findAllObjects());
        return "linksPage";
    }


    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddPersonPage(Model model) {
        logger.info("Add new link record");
        Link link = new Link();

        model.addAttribute("edit", false);
        model.addAttribute("link", link);
        return "linkPage";

    }

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editPerson(@PathVariable Long id, Model model) {
        logger.info("Edit link with ID= " + id);
        model.addAttribute("edit", true);
        model.addAttribute("link", linkService.findById(id));
        return "linkPage";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updatePerson(@Valid @ModelAttribute("link") Link link, BindingResult bindingResult,
                               Model model) {
        logger.info("Update link: " + link);
        if (bindingResult.hasErrors()) {
            // model.addAttribute("title", title);
            // logger.info("BINDING RESULT ERROR");
            return "linkPage";
        }

        /*
         * if (personService.isObjectExist(person)) { FieldError fieldError =
         * new FieldError("person", "name",
         * messageSource.getMessage("non.unique.name", new String[] {
         * person.getName() }, Locale.getDefault()));
         * bindingResult.addError(fieldError); return "personRecordPage"; }
         */

        linkService.saveObject(link);
        return "redirect:/links";
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deletePerson(@PathVariable Long id) {
        logger.info("Delete link with ID= " + id);
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
