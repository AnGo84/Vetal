package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.com.vetal.entity.User;
import ua.com.vetal.service.UserRoleServiceImpl;
import ua.com.vetal.service.UserServiceImpl;
import ua.com.vetal.utils.EncrytedPasswordUtils;

@Controller
@RequestMapping("/users")
public class UserController {
	static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRoleServiceImpl userRoleService = new UserRoleServiceImpl();

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
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("title", "New user");
		model.addAttribute("userRolesList", userRoleService.findAllObjects());
		model.addAttribute("edit", false);
		model.addAttribute("user", user);
		return "userPage";

	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveNewUser(Model model, @ModelAttribute("user") User user) {
		userService.saveObject(user);
		return "redirect:/usersPage";
	}

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editUser(@PathVariable Long id, Model model) {
		logger.info("Get ID= " + id);
		model.addAttribute("title", "Edit user");
		model.addAttribute("userRolesList", userRoleService.findAllObjects());
		model.addAttribute("edit", true);
		model.addAttribute("user", userService.findById(id));
		return "userPage";
	}

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.POST)
	public String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
		userService.saveObject(user);
		return "redirect:/usersPage";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateUser(Model model, @ModelAttribute("user") User user) {
		if (user.getEncryptedPassword() == null) {
			user.setEncryptedPassword("123");
			user.setEncryptedPassword(EncrytedPasswordUtils.encrytePassword(user.getEncryptedPassword()));
		}

		userService.saveObject(user);
		return "redirect:/users";
	}

	@RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable Long id) {
		userService.deleteById(id);
		return "redirect:/usersPage";
	}
}
