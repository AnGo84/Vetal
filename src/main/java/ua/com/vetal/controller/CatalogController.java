package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import ua.com.vetal.entity.file.LocalFile;
import ua.com.vetal.utils.ListFilesUtils;

import java.util.List;

@RequestMapping("/catalog")
@Controller
@SessionScope
//@SessionAttributes({"managersFilterList", "clientFilterList", "contractorFilterList"})
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class CatalogController {
    static final Logger logger = LoggerFactory.getLogger(CatalogController.class);
    @Value("${vetal.files}")
    private String defaultParentPath;
    private String currentParentPath;
    private List<LocalFile> localFileList;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String mainPage(Model model) {
        logger.info("Read folder default: " + defaultParentPath);

        currentParentPath = defaultParentPath;
        localFileList = ListFilesUtils.listFilesAndFoldersWithParent(currentParentPath);
        model.addAttribute("title", "Catalog");
        model.addAttribute("localFiles", localFileList);
        return "catalogPage";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String readCayalogByPath(Model model,  @RequestParam("path") String path) {
        logger.info("Read folder : " + path);
        if (path == null || path.isEmpty()|| path.equals("..")) {
            currentParentPath = defaultParentPath;
        } else {
            currentParentPath =defaultParentPath+"/"+ path;
        }
        currentParentPath = defaultParentPath;
        localFileList = ListFilesUtils.listFilesAndFoldersWithParent(currentParentPath);
        model.addAttribute("title", "Catalog");
        model.addAttribute("localFiles", localFileList);
        return "catalogPage";
    }


    @RequestMapping(value = {"/{path}"}, method = RequestMethod.GET)
    public String readFolder(Model model,@PathVariable String path) {
        logger.info("Read folder: " + path);
        if (path == null || path.isEmpty()|| path.equals("..")) {
            currentParentPath = defaultParentPath;
        } else {
            currentParentPath =defaultParentPath+"/"+ path;
        }
        logger.info("Read folder: " + currentParentPath);
        localFileList = ListFilesUtils.listFilesAndFoldersWithParent(currentParentPath);
        model.addAttribute("localFiles", localFileList);
        return "catalogPage";
    }

    /**
     * This method will provide LocalFile list to views
     */
    @ModelAttribute("localFilesList")
    public List<LocalFile> localFiles() {
        return localFileList;
    }
}
