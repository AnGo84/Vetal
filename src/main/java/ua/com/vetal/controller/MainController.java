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

//import org.springframework.security.core.userdetails.User;

@Controller
// @SessionAttributes({"managersFilterList", "clientFilterList", "contractorFilterList"})
public class MainController {
    static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private LinkServiceImpl linkService;

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

        logger.info("User Name: " + userName);

        User loggedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loggedUser);
        model.addAttribute("userInfo", userInfo);

        return "userInfoPage";
    }
/*
    @RequestMapping(value = "/openFolder", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void openFolder(Model model, Principal principal) {
        // logger.info("Try open folder");
        String url = "/home/mbrunarskiy/Desktop";
        logger.info("Try open folder " + url + " for OS: " + PlatformUtils.osName());
        try {
            File file = new File(url);
            if (file.exists()) {
                FileUtils.openDirectory(url);
            } else {
                url = "D:";
                //logger.info("Try open folder " + url + " for OS: " + PlatformUtils.osName());
                FileUtils.openDirectory(url);
            }
        } catch (IOException e) {
            logger.info("Error on open: " + url);
            e.printStackTrace();
        }
        // return "redirect/main";
    }*/

    @RequestMapping(value = "/folder-{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void openFolder(HttpServletResponse response, @PathVariable Long id) throws IOException {
        Link link = linkService.findById(id);
        if (link == null) {
            return;
        }
        String url = link.getPath();
        logger.info("Try open folder '" + url + "' for OS: " + PlatformUtils.osName() + "(isWindows: " + PlatformUtils.isWindows() + ")");
        try {
            if (PlatformUtils.isWindows()) {
                String command = "explorer.exe " + url;
                logger.info("Called command: " + command);
                Runtime.getRuntime().exec(command);
            }
            //FileUtils.openDirectory(url);
            /*File file = new File(url);
            if (file.exists()) {
                FileUtils.openDirectory(url);
            } else {
                return;
            }*/
        } catch (IOException e) {
            logger.info("Error on open: " + url);
            e.printStackTrace();
            String errorMessage = "Error on open URL '" + url + "': " + e.getMessage();
            /*OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();*/
            WebUtils.setTextToResponse(errorMessage, response);
            return;
        }
    }

    //http://websystique.com/springmvc/spring-mvc-4-file-download-example/
    @RequestMapping(value = "/file-{id}", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @PathVariable Long id) throws IOException {
        Link link = linkService.findById(id);
        if (link == null) {
            String errorMessage = messageSource.getMessage("message.file.file_not_found",
                    new String[]{String.valueOf(link)}, new Locale("ru"));
            WebUtils.setTextToResponse(errorMessage, response);
            return;
        }
        logger.info("Try download link " + link);

        File file = new File(link.getPath());

        if (!file.exists()) {
            String errorMessage = messageSource.getMessage("message.file.file_not_found",
                    new String[]{String.valueOf(link)}, new Locale("ru"));
            //logger.info(errorMessage);
            WebUtils.setTextToResponse(errorMessage, response);
            return;
        }
        /*
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            //logger.info("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
        //System.out.println("mimetype : "+mimeType);
        */

        response.setContentType(FileUtils.getMimeType(file));

        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        //Copy bytes from source to destination(outputstream in this example), closes both streams.
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
