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
import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.service.ProductionDirectoryServiceImpl;
import ua.com.vetal.service.ProductionTypeDirectoryServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/productions")
// @SessionAttributes({ "title", "directoryName", "pageName" })

public class ProductionDirectoryController {
    static final Logger logger = LoggerFactory.getLogger(ProductionDirectoryController.class);
    @Autowired
    MessageSource messageSource;
    private String title = "Production";
    private String directoryName = "Production";
    private String pageName = "/productions";
    @Autowired
    private ProductionDirectoryServiceImpl directoryService;
    @Autowired
    private ProductionTypeDirectoryServiceImpl productionTypeService;

    /*
     * @Autowired public UserController(UserServiceImpl userService) {
     * this.userService = userService; }
     */

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String directoryList(Model model) {
        model.addAttribute("productionsList", directoryService.findAllObjects());
        return "productionsPage";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddRecordPage(Model model) {
        logger.info("Add new production record");
        ProductionDirectory production = new ProductionDirectory();

        model.addAttribute("edit", false);
        model.addAttribute("production", production);
        return "productionPage";

    }

    /*
     * @RequestMapping(value = "/add", method = RequestMethod.POST) public
     * String saveNewUser(Model model, @ModelAttribute("user") User user) {
     *
     * userService.saveObject(user); return "redirect:/usersPage"; }
     */

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editRecord(@PathVariable Long id, Model model) {
        logger.info("Edit production with ID= " + id);
        // model.addAttribute("title", "Edit user");
        // model.addAttribute("userRolesList",
        // userRoleService.findAllObjects());
        model.addAttribute("edit", true);
        model.addAttribute("production", directoryService.findById(id));
        return "productionPage";
    }

    /*
     * @RequestMapping(value = "/edit-{id}", method = RequestMethod.POST) public
     * String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
     * userService.saveObject(user); return "redirect:/usersPage"; }
     */

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateRecord(@Valid @ModelAttribute("production") ProductionDirectory directory,
                               BindingResult bindingResult, Model model) {
        logger.info("Update Production: " + directory);
        if (bindingResult.hasErrors()) {
            // model.addAttribute("title", title);
            // logger.info("BINDING RESULT ERROR");
            return "productionPage";
        }
        /*
		if (directoryService.isObjectExist(directory)) {
			FieldError fieldError = new FieldError("production", "fullName", messageSource.getMessage("non.unique.name",
					new String[] { "Название", directory.getFullName() }, new Locale("ru")));
			bindingResult.addError(fieldError);
			return "productionPage";
		}*/

        directoryService.saveObject(directory);
        return "redirect:" + pageName;
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deleteRecord(@PathVariable Long id) {
        logger.info("Delete Production with ID= " + id);
        directoryService.deleteById(id);
        return "redirect:" + pageName;
    }

    /**
     * This method will provide Title to views
     */
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
        // return this.directoryName;
    }

    @ModelAttribute("pageName")
    public String initializePageName() {
        return this.pageName;
    }

    /**
     * This method will provide LinkType list to views
     */
    @ModelAttribute("productionTypesList")
    public List<ProductionTypeDirectory> initializeProductionTypes() {
        return productionTypeService.findAllObjects();
    }
}
