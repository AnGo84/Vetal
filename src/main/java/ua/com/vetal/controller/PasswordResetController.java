package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import ua.com.vetal.entity.dto.PasswordResetDto;
import ua.com.vetal.repositories.PasswordResetTokenRepository;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/passwordReset")
@Slf4j
public class PasswordResetController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("passwordResetForm")
    public PasswordResetDto passwordReset() {
        return new PasswordResetDto();
    }

    @GetMapping
    public String displayResetPasswordPage(@RequestParam(required = false) String token, Model model, Locale locale) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            model.addAttribute("error", messageSource.getMessage("non.found.token", null, locale));
        } else if (resetToken.isExpired()) {
            model.addAttribute("error", messageSource.getMessage("non.token.hasExpired", null, locale));
        } else {
            model.addAttribute("token", resetToken.getToken());
        }

        return "passwordResetPage";
    }

    @PostMapping
    @Transactional
    public String handlePasswordReset(@ModelAttribute("passwordResetForm") @Valid PasswordResetDto passwordResetDto,
                                      BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".passwordResetForm", result);
            redirectAttributes.addFlashAttribute("passwordResetForm", passwordResetDto);
            return "redirect:/passwordReset?token=" + passwordResetDto.getToken();
        }
        if (StringUtils.isBlank(passwordResetDto.getPassword())) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("required.password", null, locale));
            return "redirect:/passwordReset?token=" + passwordResetDto.getToken();
        } else if (StringUtils.isBlank(passwordResetDto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("required.passwordConfirm", null, locale));
            return "redirect:/passwordReset?token=" + passwordResetDto.getToken();
        } else if (!passwordResetDto.getPassword().equals(passwordResetDto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("non.match.passwords", null, locale));
            return "redirect:/passwordReset?token=" + passwordResetDto.getToken();
        }

        PasswordResetToken token = tokenRepository.findByToken(passwordResetDto.getToken());
        String updatedPassword = passwordEncoder.encode(passwordResetDto.getPassword());

        token.getUser().setEncryptedPassword(updatedPassword);
        tokenRepository.delete(token);

        return "redirect:/login?resetSuccess";
    }

}
