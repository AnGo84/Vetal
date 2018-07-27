package ua.com.vetal.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import ua.com.vetal.entity.LaminateDirectory;
import ua.com.vetal.service.LaminateDirectoryServiceImpl;

@Controller
@RequestMapping("/laminate")
// @SessionAttributes({ "title", "directoryName", "pageName" })

public class LaminateDirectoryController {
	static final Logger logger = LoggerFactory.getLogger(LaminateDirectoryController.class);

	private String title = "Laminate";
	private String directoryName = "Laminate";
	private String pageName = "/laminate";

	@Autowired
	MessageSource messageSource;

	@Autowired
	private LaminateDirectoryServiceImpl directoryService;

	/*
	 * @Autowired public UserController(UserServiceImpl userService) {
	 * this.userService = userService; }
	 */

	@RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
	public String personList(Model model) {
		model.addAttribute("directoryList", directoryService.findAllObjects());
		return "directoryPage";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddUserPage(Model model) {
		logger.info("Add new " + title + " record");
		LaminateDirectory directory = new LaminateDirectory();

		model.addAttribute("edit", false);
		model.addAttribute("directory", directory);
		return "directoryRecordPage";

	}

	/*
	 * @RequestMapping(value = "/add", method = RequestMethod.POST) public
	 * String saveNewUser(Model model, @ModelAttribute("user") User user) {
	 * 
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editUser(@PathVariable Long id, Model model) {
		logger.info("Edit " + title + " with ID= " + id);
		// model.addAttribute("title", "Edit user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		model.addAttribute("edit", true);
		model.addAttribute("directory", directoryService.findById(id));
		return "directoryRecordPage";
	}

	/*
	 * @RequestMapping(value = "/edit-{id}", method = RequestMethod.POST) public
	 * String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateUser(@Valid @ModelAttribute("directory") LaminateDirectory directory,
			BindingResult bindingResult, Model model) {
		logger.info("Update " + title + ": " + directory);
		if (bindingResult.hasErrors()) {
			// model.addAttribute("title", title);
			// logger.info("BINDING RESULT ERROR");
			return "directoryRecordPage";
		}

		if (directoryService.isObjectExist(directory)) {
			FieldError fieldError = new FieldError("directory", "name", messageSource.getMessage("non.unique.name",
					new String[] { directory.getName() }, Locale.getDefault()));
			bindingResult.addError(fieldError);
			return "directoryRecordPage";
		}

		directoryService.saveObject(directory);
		return "redirect:" + pageName;
	}

	@RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable Long id) {
		logger.info("Delete " + title + " with ID= " + id);
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
		return this.directoryName;
	}

	@ModelAttribute("pageName")
	public String initializePageName() {
		return this.pageName;
	}
}
