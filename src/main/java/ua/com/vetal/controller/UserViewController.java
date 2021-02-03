package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
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
@Slf4j
public class UserViewController {

	private String title = "user";

	@Value("${user.password.default}")
	private String userPasswordDefault;

	@Autowired
	private final UserServiceImpl userService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	@Autowired
	public UserViewController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@RequestMapping(value = {"/view"}, method = RequestMethod.GET)
	public String showUserViewPage(Model model) {
		log.info("View user");
		User user = userService.findByName(userService.getPrincipal());
		if (userService.isObjectExist(user)) {
			model.addAttribute("user", user);
			return "userViewPage";
		}
		return "mainPage";
	}

	@RequestMapping(value = "/changePassword-{id}", method = RequestMethod.GET)
	public String changeUserPassword(@PathVariable Long id, Model model) {
		log.info("Change Pass for user with ID= {}", id);
		User user = userService.findById(id);
		if (!userService.isObjectExist(user)) {
			log.error("Cannot change password for user id '{}'. User not exist.", id);
			return "error";
		}
		PasswordResetToken token = PasswordResetToken.newBuilder().setUser(user).build();
		tokenRepository.save(token);
		return "redirect:/passwordReset?token=" + token.getToken();
	}

}
