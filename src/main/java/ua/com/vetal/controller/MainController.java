package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.entity.Link;
import ua.com.vetal.service.LinkServiceImpl;
import ua.com.vetal.service.UserServiceImpl;
import ua.com.vetal.utils.FileUtils;
import ua.com.vetal.utils.PlatformUtils;
import ua.com.vetal.utils.WebUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Controller
@Slf4j
public class MainController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private LinkServiceImpl linkService;

    @RequestMapping(value = {"/", "/main"}, method = RequestMethod.GET)
    public String mainPage(Model model) {
        model.addAttribute("title", "main");
        return "mainPage";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
        User loggedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loggedUser);
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

        log.info("Show UserInfo form User Name: {}", userName);

        User loggedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loggedUser);
        model.addAttribute("userInfo", userInfo);

        return "userInfoPage";
    }

    @RequestMapping(value = "/folder-{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void openFolder(HttpServletResponse response, @PathVariable Long id) throws IOException {
        Link link = linkService.findById(id);
        if (link == null) {
            return;
        }
        String url = link.getPath();
        log.info("Try open folder '{}' for OS: {}(isWindows: {})", url, PlatformUtils.osName(), PlatformUtils.isWindows());
        try {
            if (PlatformUtils.isWindows()) {
                String command = "explorer.exe " + url;
                log.info("Called command: {}", command);
                Runtime.getRuntime().exec(command);
            }
        } catch (IOException e) {
            log.info("Error on open: {}", url);
            e.printStackTrace();
            String errorMessage = "Error on open URL '" + url + "': " + e.getMessage();
            WebUtils.setTextToResponse(errorMessage, response);
            return;
        }
    }

    @RequestMapping(value = "/file-{id}", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @PathVariable Long id) throws IOException {
        Link link = linkService.findById(id);
        if (link == null) {
            String errorMessage = messageSource.getMessage("message.file.file_not_found",
                    new String[]{String.valueOf(link)}, new Locale("ru"));
            WebUtils.setTextToResponse(errorMessage, response);
            return;
        }
        log.info("Try download by link: {}", link);

        File file = new File(link.getPath());

        if (!file.exists()) {
            String errorMessage = messageSource.getMessage("message.file.file_not_found",
                    new String[]{String.valueOf(link)}, new Locale("ru"));
            WebUtils.setTextToResponse(errorMessage, response);
            return;
        }

        response.setContentType(FileUtils.getMimeType(file));
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        FileCopyUtils.copy(inputStream, response.getOutputStream());
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

    @ModelAttribute("linksList")
    public List<Link> initializeLinks() {
        return linkService.findByLinkTypeId((long) 1);
    }

    @ModelAttribute("filesList")
    public List<Link> initializeFiles() {
        return linkService.findByLinkTypeId((long) 2);
    }

    @ModelAttribute("foldersList")
    public List<Link> initializeFolders() {
        return linkService.findByLinkTypeId((long) 3);
    }

}
