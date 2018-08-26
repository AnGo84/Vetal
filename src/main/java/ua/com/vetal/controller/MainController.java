package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.service.UserServiceImpl;
import ua.com.vetal.utils.FileUtils;
import ua.com.vetal.utils.PlatformUtils;
import ua.com.vetal.utils.WebUtils;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Locale;

//import org.springframework.security.core.userdetails.User;

@Controller
@SessionAttributes({"managersFilterList", "clientFilterList", "contractorFilterList"})
public class MainController {
    static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = {"/", "/main"}, method = RequestMethod.GET)
    public String mainPage(Model model) {

        model.addAttribute("title", "main");
        // model.addAttribute("tasksList", taskService.findAllObjects());
        // model.addAttribute("filterData", filterData);
        // model.addAttribute("message", "This is welcome page!");

        return "mainPage";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "adminPage";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "loginPage";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {

        // After user login successfully.
        String userName = principal.getName();

        logger.info("User Name: " + userName);

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "userInfoPage";
    }

    @RequestMapping(value = "/openFolder", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void openFolder(Model model, Principal principal) {
        //TODO
        // logger.info("Try open folder");
        String url = "/home/mbrunarskiy/Desktop";
        logger.info("Try open folder " + url + " for OS: " + PlatformUtils.osName());
        try {
            File file = new File(url);
            if (file.exists()) {
                FileUtils.openDirectory(url);
            } else {
                url = "D:";
                logger.info("Try open folder " + url + " for OS: " + PlatformUtils.osName());
                FileUtils.openDirectory(url);
            }
        } catch (IOException e) {
            logger.info("Error on open: " + url);
            e.printStackTrace();
        }
        // return "redirect/main";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
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

    /*
     * @RequestMapping(value = "/production", method = RequestMethod.GET) public
     * String productionDirectory(Model model, Principal principal) {
     *
     * if (principal != null) { } model.addAttribute("directoryList",
     * productionDirectoryService.findAllObjects());
     *
     * return "directoryPage"; }
     */

}
