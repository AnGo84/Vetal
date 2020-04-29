package ua.com.vetal.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.vetal.entity.PasswordResetToken;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.dto.PasswordResetDto;
import ua.com.vetal.repositories.PasswordResetTokenRepository;
import ua.com.vetal.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/passwordReset")
public class PasswordResetController {
    static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("passwordResetForm")
    public PasswordResetDto passwordReset() {
        return new PasswordResetDto();
    }

    @GetMapping
    public String displayResetPasswordPage(@RequestParam(required = false) String token, Model model) {
        //logger.info("Change password for token: " + token);
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            model.addAttribute("error", messageSource.getMessage("non.found.token", null, new Locale("ru")));
        } else if (resetToken.isExpired()) {
            model.addAttribute("error", messageSource.getMessage("non.token.hasExpired", null, new Locale("ru")));
        } else {
            model.addAttribute("token", resetToken.getToken());
        }

        return "passwordResetPage";
    }

    @PostMapping
    @Transactional
    public String handlePasswordReset(@ModelAttribute("passwordResetForm") @Valid PasswordResetDto passwordResetDto,
                                      BindingResult result, RedirectAttributes redirectAttributes) {
        //logger.info("Update password: " + passwordResetDto);
        //logger.info("Post User Edit: " + editUser);
        System.out.println("PasswordResetDto: " + passwordResetDto);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".passwordResetForm", result);
            redirectAttributes.addFlashAttribute("passwordResetForm", passwordResetDto);
            return "redirect:/passwordReset?token=" + passwordResetDto.getToken();
        }
        //if (passwordResetDto.getPassword() == null && passwordResetDto.getConfirmPassword() == null || passwordResetDto.getPassword() != null && passwordResetDto.getPassword().equals(passwordResetDto.getConfirmPassword()))
        if (StringUtils.isBlank(passwordResetDto.getPassword())) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("required.password", null, new Locale("ru")));
            return "redirect:/passwordReset?token=" + passwordResetDto.getToken();
        } else if (StringUtils.isBlank(passwordResetDto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("required.passwordConfirm", null, new Locale("ru")));
            return "redirect:/passwordReset?token=" + passwordResetDto.getToken();
        } else if (!passwordResetDto.getPassword().equals(passwordResetDto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("non.match.passwords", null, new Locale("ru")));
            return "redirect:/passwordReset?token=" + passwordResetDto.getToken();
        }

        PasswordResetToken token = tokenRepository.findByToken(passwordResetDto.getToken());
        User user = token.getUser();
        String updatedPassword = passwordEncoder.encode(passwordResetDto.getPassword());

        user.setEncryptedPassword(updatedPassword);
        // userService.updatePassword(updatedPassword, user.getId());
        tokenRepository.delete(token);

        return "redirect:/login?resetSuccess";
    }

}
