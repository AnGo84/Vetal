package ua.com.vetal.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.com.vetal.entity.AbstractDirectory;
import ua.com.vetal.entity.User;

//@Controller
//@RequestMapping("/directory")
// @SessionAttributes({ "userRolesList" })

public class DirectoryController {
	static final Logger logger = LoggerFactory.getLogger(DirectoryController.class);

	private String title = "user";

	@Autowired
	MessageSource messageSource;

	@Autowired
	// private SimpleService<AbstractDirectory> simpleService;

	/*
	 * @Autowired public UserController(UserServiceImpl userService) {
	 * this.userService = userService; }
	 */

	/*
	 * @RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
	 * public String personList(Model model) {
	 * model.addAttribute("directoryList", simpleService.findAllObjects());
	 * return "directoryPage"; }
	 */

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddRecordPage(Model model) {
		logger.info("Add new record");
		User user = new User();
		user.setEnabled(true);
		title = "New user";
		// model.addAttribute("title", "New user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		model.addAttribute("edit", false);
		model.addAttribute("user", user);
		return "userPage";

	}

	/*
	 * @RequestMapping(value = "/add", method = RequestMethod.POST) public
	 * String saveNewUser(Model model, @ModelAttribute("user") User user) {
	 * 
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editRecord(@PathVariable Long id, Model model) {
		logger.info("Edit user with ID= " + id);
		title = "Edit user";
		// model.addAttribute("title", "Edit user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		model.addAttribute("edit", true);
		// model.addAttribute("directory", simpleService.findById(id));
		return "userPage";
	}

	/*
	 * @RequestMapping(value = "/edit-{id}", method = RequestMethod.POST) public
	 * String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateRecord(@Valid @ModelAttribute("user") AbstractDirectory abstractDirectory,
			BindingResult bindingResult, Model model) {
		logger.info("Update User: " + abstractDirectory);
		if (bindingResult.hasErrors()) {
			// model.addAttribute("title", title);
			// logger.info("BINDING RESULT ERROR");
			return "userPage";
		}

		/*
		 * if (simpleService.isObjectExist(abstractDirectory)) { FieldError
		 * ssoError = new FieldError("abstractDirectory", "name", messageSource
		 * .getMessage("non.unique.login", new String[] {
		 * abstractDirectory.getName() }, Locale.getDefault()));
		 * bindingResult.addError(ssoError); return "userPage"; }
		 */

		// simpleService.saveObject(abstractDirectory);
		return "redirect:/users";
	}

	/*
	 * @RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
	 * public String deleteUser(@PathVariable Long id) {
	 * logger.info("Delete user with ID= " + id); //
	 * simpleService.deleteById(id); return "redirect:/users"; }
	 */

	/**
	 * This method will provide Title to views
	 */
	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}
}
