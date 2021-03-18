package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;
import ua.com.vetal.service.UserRoleServiceImpl;
import ua.com.vetal.service.UserServiceImpl;
import ua.com.vetal.utils.LoggerUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/users")
@SessionAttributes({"userRolesList"})
@Slf4j
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class UserController {
	private String title = "user";

	@Value("${user.password.default}")
	private String userPasswordDefault;

	@Autowired
	private final UserServiceImpl userService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserRoleServiceImpl userRoleService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@RequestMapping(value = {"", "/all", "/list"}, method = RequestMethod.GET)
	public String personList(Model model) {
		model.addAttribute("users", userService.findAllObjects());
		return "usersPage";
	}

	@RequestMapping(value = {"/add"}, method = RequestMethod.GET)
	public String showAddUserPage(Model model) {
		log.info("Add new user");
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("edit", false);
		model.addAttribute("user", user);
		return "userPage";
	}

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editUser(@PathVariable Long id, Model model) {
		log.info("Edit user with ID= {}", id);
		model.addAttribute("edit", true);
		model.addAttribute("user", userService.findById(id));
		return "userPage";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, Locale locale) {
		log.info("Update User: {}", user);
		if (bindingResult.hasErrors()) {
			LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
			return "userPage";
		}

		try {
			if (user.getEncryptedPassword() == null) {
				user.setEncryptedPassword(passwordEncoder.encode(userPasswordDefault));
			}
			userService.updateObject(user);

		} catch (DataIntegrityViolationException e) {
			FieldError ssoError = new FieldError("user", "name", messageSource.getMessage("non.unique.field",
					new String[]{"Login", user.getName()}, locale));
			bindingResult.addError(ssoError);
			model.addAttribute("edit", (user.getId() != null));
			return "userPage";
		}
		return "redirect:/users";
	}

	@RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
	public String deleteUser(@PathVariable Long id) {
		log.info("Delete user with ID= {}", id);
		userService.deleteById(id);
		return "redirect:/users";
	}

	@RequestMapping(value = "/resetPassword-{id}", method = RequestMethod.GET)
	public String resetUserPassword(@PathVariable Long id, Model model) {
		log.info("Reset Pass for user with ID= {}", id);
		User user = userService.findById(id);

		model.addAttribute("edit", true);
		model.addAttribute("user", user);

		if (userService.isObjectExist(user)) {
			user.setEncryptedPassword(passwordEncoder.encode(userPasswordDefault));
			userService.saveObject(user);
			return "redirect:/users/edit-" + user.getId() + "?resetSuccess";
		}
		return "redirect:/users/edit-" + user.getId();
	}

	@ModelAttribute("userRolesList")
	public List<UserRole> initializeRoles() {
		return userRoleService.findAllObjects();
	}

	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}

}
