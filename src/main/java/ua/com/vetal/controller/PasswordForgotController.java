package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.vetal.entity.PasswordResetToken;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.dto.PasswordForgotDto;
import ua.com.vetal.repositories.PasswordResetTokenRepository;
import ua.com.vetal.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/forgotPassword")
@Slf4j
public class PasswordForgotController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	@ModelAttribute("forgotPasswordForm")
	public PasswordForgotDto forgotPasswordDto() {
		return new PasswordForgotDto();
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String passwordResetPage(Model model) {
		model.addAttribute("title", "Forgot Password");
		return "passwordForgotPage";
	}


	@PostMapping
	public String processForgotPasswordForm(@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
											BindingResult result, Locale locale) {
		if (result.hasErrors()) {
			return "passwordForgotPage";
		}
		User user = userService.findByName(form.getUserName());
		if (user == null) {
			result.rejectValue("userName", null, messageSource.getMessage("non.unique.field",
					new String[]{"Login", form.getUserName()}, locale));
			return "passwordForgotPage";
		}

		PasswordResetToken token = PasswordResetToken.newBuilder().setUser(user).build();
		tokenRepository.save(token);

		return "redirect:/passwordReset?token=" + token.getToken();
	}
}
