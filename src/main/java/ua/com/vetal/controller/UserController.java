package ua.com.vetal.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;
import ua.com.vetal.service.UserRoleServiceImpl;
import ua.com.vetal.service.UserServiceImpl;
import ua.com.vetal.utils.EncrytedPasswordUtils;

@Controller
@RequestMapping("/users")
@SessionAttributes({ "userRolesList" })

public class UserController {
	static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private String title = "user";

	@Autowired
	MessageSource messageSource;

	@Autowired
	private UserRoleServiceImpl userRoleService;

	private final UserServiceImpl userService;

	@Autowired
	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@RequestMapping(value = { "", "all", "list" }, method = RequestMethod.GET)
	public String personList(Model model) {
		model.addAttribute("users", userService.findAllObjects());
		return "usersPage";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddUserPage(Model model) {
		logger.info("Add new user");
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
	public String editUser(@PathVariable Long id, Model model) {
		logger.info("Edit user with ID= " + id);
		title = "Edit user";
		// model.addAttribute("title", "Edit user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		model.addAttribute("edit", true);
		model.addAttribute("user", userService.findById(id));
		return "userPage";
	}

	/*
	 * @RequestMapping(value = "/edit-{id}", method = RequestMethod.POST) public
	 * String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
		logger.info("Update User: " + user);
		if (bindingResult.hasErrors()) {
			// model.addAttribute("title", title);
			// logger.info("BINDING RESULT ERROR");
			return "userPage";
		}

		if (userService.isObjectExist(user)) {
			FieldError ssoError = new FieldError("user", "name", messageSource.getMessage("non.unique.name",
					new String[] { "Login", user.getName() }, new Locale("ru")));
			bindingResult.addError(ssoError);
			return "userPage";
		}

		if (user.getEncryptedPassword() == null) {
			user.setEncryptedPassword("123");
			user.setEncryptedPassword(EncrytedPasswordUtils.encrytePassword(user.getEncryptedPassword()));
		}

		userService.saveObject(user);
		return "redirect:/users";
	}

	@RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable Long id) {
		logger.info("Delete user with ID= " + id);
		userService.deleteById(id);
		return "redirect:/users";
	}

	/**
	 * This method will provide UserRose list to views
	 */
	@ModelAttribute("userRolesList")
	public List<UserRole> initializeRoles() {
		return userRoleService.findAllObjects();
	}

	/**
	 * This method will provide Title to views
	 */
	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}
}
