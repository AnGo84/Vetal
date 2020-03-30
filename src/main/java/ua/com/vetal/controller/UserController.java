package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;
import ua.com.vetal.repositories.PasswordResetTokenRepository;
import ua.com.vetal.service.UserRoleServiceImpl;
import ua.com.vetal.service.UserServiceImpl;
import ua.com.vetal.utils.EncrytedPasswordUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/users")
@SessionAttributes({"userRolesList"})

@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class UserController {
    static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private MessageSource messageSource;
    private String title = "user";
    @Value("${user.password.default}")
    private String userPasswordDefault;
    @Autowired
    private UserRoleServiceImpl userRoleService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

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
        logger.info("Add new user");
        User user = new User();
        user.setEnabled(true);
        //title = "New user";
        //model.addAttribute("title", title);
        // model.addAttribute("userRolesList",
        // userRoleService.findAllObjects());
        model.addAttribute("edit", false);
        model.addAttribute("user", user);
        return "userPage";
    }
//
//    @RequestMapping(value = {"/view"}, method = RequestMethod.GET)
//    public String showUserViewPage(Model model) {
//        logger.info("View user");
//        User user = userService.findByName(getPrincipal());
//        if (userService.isObjectExist(user)) {
//            model.addAttribute("user", user);
//            return "userViewPage";
//        }
//        return "mainPage";
//    }

    /*
     * @RequestMapping(value = "/add", method = RequestMethod.POST) public
     * String saveNewUser(Model model, @ModelAttribute("user") User user) {
     *
     * userService.saveObject(user); return "redirect:/usersPage"; }
     */

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editUser(@PathVariable Long id, Model model) {
        logger.info("Edit user with ID= " + id);
        //title = "Edit user";
        //model.addAttribute("title", title);
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
    //@Transactional
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        logger.info("Update User: " + user);
        if (bindingResult.hasErrors()) {
            // model.addAttribute("title", title);
            // logger.info("BINDING RESULT ERROR");
            return "userPage";
        }

        //if (userService.isObjectExist(user)) {
        /*if (!userService.isCurrentObject(user)) {
            FieldError ssoError = new FieldError("user", "name", messageSource.getMessage("non.unique.field",
                    new String[]{"Login", user.getName()}, new Locale("ru")));
            bindingResult.addError(ssoError);
            return "userPage";
        }*/
        try {
            if (user.getEncryptedPassword() == null) {
                user.setEncryptedPassword(userPasswordDefault);
                user.setEncryptedPassword(EncrytedPasswordUtils.encrytePassword(user.getEncryptedPassword()));
            }

            userService.saveObject(user);

        } catch (DataIntegrityViolationException e) {
            //System.out.println("history already exist");
            FieldError ssoError = new FieldError("user", "name", messageSource.getMessage("non.unique.field",
                    new String[]{"Login", user.getName()}, new Locale("ru")));
            bindingResult.addError(ssoError);
            model.addAttribute("edit", (user.getId() != null));
            return "userPage";
        }

        return "redirect:/users";
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deleteUser(@PathVariable Long id) {
        logger.info("Delete user with ID= " + id);
        userService.deleteById(id);
        return "redirect:/users";
    }

    @RequestMapping(value = "/resetPassword-{id}", method = RequestMethod.GET)
    public String resetUserPassword(@PathVariable Long id, Model model) {
        logger.info("Reset Pass for user with ID= " + id);
        User user = userService.findById(id);
        //logger.info("Find User= " + user);

        model.addAttribute("edit", true);
        model.addAttribute("user", user);

        if (userService.isObjectExist(user)) {
            user.setEncryptedPassword(EncrytedPasswordUtils.encrytePassword(userPasswordDefault));
            userService.saveObject(user);
            return "redirect:/users/edit-" + user.getId() + "?resetSuccess";
        }
        return "redirect:/users/edit-" + user.getId();
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
