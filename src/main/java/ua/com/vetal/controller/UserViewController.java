package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.vetal.entity.PasswordResetToken;
import ua.com.vetal.entity.User;
import ua.com.vetal.repositories.PasswordResetTokenRepository;
import ua.com.vetal.service.UserServiceImpl;

@Controller
@RequestMapping("/user")

@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class UserViewController {
	static final Logger logger = LoggerFactory.getLogger(UserViewController.class);
	@Autowired
	private final UserServiceImpl userService;
	@Autowired
	private MessageSource messageSource;

	private String title = "user";
	@Value("${user.password.default}")
	private String userPasswordDefault;
	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	@Autowired
	public UserViewController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@RequestMapping(value = {"/view"}, method = RequestMethod.GET)
	public String showUserViewPage(Model model) {
		logger.info("View user");
		User user = userService.findByName(getPrincipal());
		if (userService.isObjectExist(user)) {
			model.addAttribute("user", user);
			return "userViewPage";
		}
		return "mainPage";
	}

	@RequestMapping(value = "/changePassword-{id}", method = RequestMethod.GET)
	public String changeUserPassword(@PathVariable Long id, Model model) {
		logger.info("Change Pass for user with ID= " + id);
		User user = userService.findById(id);
		if (!userService.isObjectExist(user)) {
			logger.error("Cannot change password for user id '{}'. User not exist.", id);
			return "error";
		}
		PasswordResetToken token = PasswordResetToken.newBuilder().setUser(user).build();
		tokenRepository.save(token);

		logger.error("Test token: " + token.getToken());
		return "redirect:/passwordReset?token=" + token.getToken();
	}


	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
}
