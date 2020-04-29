package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/forgotPassword")
public class PasswordForgotController {
	static final Logger logger = LoggerFactory.getLogger(PasswordForgotController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private PasswordResetTokenRepository tokenRepository;
	//@Autowired private EmailService emailService;

	@ModelAttribute("forgotPasswordForm")
	public PasswordForgotDto forgotPasswordDto() {
		return new PasswordForgotDto();
	}

    /*@GetMapping
    public String displayForgotPasswordPage() {
        return "passwordForgotPage";
    }*/

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String passwordResetPage(Model model) {
        /*logger.info("Try reset Pass for User Name: " + userName);


        ua.com.vetal.entity.User user = userService.findByName(userName);
        logger.info("Find User: " + user);

        if (user == null || user.getName() == null || user.getName().equals("") || !userService.isObjectExist(user)) {
            model.addAttribute("wrongusername", messageSource.getMessage("message.wrong_login", null, new Locale("ru")));

            return "loginPage";
        }*/

		model.addAttribute("title", "Forgot Password");
		return "passwordForgotPage";
	}


	@PostMapping
	public String processForgotPasswordForm(@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
											BindingResult result,
											HttpServletRequest request) {
		if (result.hasErrors()) {
			return "passwordForgotPage";
		}
		User user = userService.findByName(form.getUserName());
		if (user == null) {
			result.rejectValue("userName", null, messageSource.getMessage("non.unique.field",
					new String[]{"Login", form.getUserName()}, new Locale("ru")));
			return "passwordForgotPage";
		}

		PasswordResetToken token = PasswordResetToken.newBuilder().setUser(user).build();
		tokenRepository.save(token);
/*

        Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Password reset request");

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("user", user);
        model.put("signature", "https://memorynotfound.com");
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        model.put("resetUrl", url + "/reset-password?token=" + token.getToken());
        mail.setModel(model);
        emailService.sendEmail(mail);

        return "redirect:/forgot-password?success";
*/
		return "redirect:/passwordReset?token=" + token.getToken();
	}

}
