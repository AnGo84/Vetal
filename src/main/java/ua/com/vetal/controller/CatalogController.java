package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import ua.com.vetal.entity.file.LocalFile;
import ua.com.vetal.utils.ListFilesUtils;
import ua.com.vetal.utils.MediaTypeUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RequestMapping("/catalog")
@Controller
@SessionScope
//@SessionAttributes({"managersFilterList", "clientFilterList", "contractorFilterList"})
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class CatalogController {
    static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

    @Autowired
    private ServletContext servletContext;

    @Value("${vetal.files}")
    private String defaultParentPath;
    private String currentParentPath;
    private List<LocalFile> localFileList;
    private File parentPath;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String mainPage(Model model) {
        logger.info("Read folder default: " + defaultParentPath);

        currentParentPath = defaultParentPath;
        localFileList = ListFilesUtils.listFilesAndFoldersWithParent(currentParentPath);
        model.addAttribute("title", "Catalog");
        parentPath = new File(defaultParentPath);
        model.addAttribute("parentPath", parentPath);
        model.addAttribute("currentPath", currentParentPath);
        model.addAttribute("localFiles", localFileList);
        return "catalogPage";
    }

/*    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String readCatalogByPath(Model model, @RequestParam("path") String path) {
        logger.info("Read folder : " + path);
        if (path == null || path.isEmpty() || path.equals("..")) {
            currentParentPath = defaultParentPath;
        } else {
            currentParentPath = defaultParentPath + "/" + path;
        }
        currentParentPath = defaultParentPath;
        localFileList = ListFilesUtils.listFilesAndFoldersWithParent(currentParentPath);
        model.addAttribute("title", "Catalog");
        model.addAttribute("localFiles", localFileList);
        return "catalogPage";
    }*/


    @RequestMapping(value = {"/{path}"}, method = RequestMethod.GET)
    public String readFolder(Model model, @PathVariable String path)
    /*@RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String readFolder(Model model, @RequestParam("path") String path)*/
    {
        logger.info("Read folder: " + path);

        if (path == null || path.isEmpty() || path.equals("..")) {
            currentParentPath = defaultParentPath;
            parentPath = new File(defaultParentPath);
        } else {
            LocalFile localFile = ListFilesUtils.findLocalFileByName(localFileList, path);
            if (localFile == null) {
                logger.info("LocalFile is null. Parent name: " + parentPath.getName());
                if (parentPath.getName().equals(path)){
                    logger.info("Parent name: " + parentPath.getName() +" equal to " + path);

                    currentParentPath = parentPath.getAbsolutePath();
                    parentPath = parentPath.getParentFile();
                }
            } else {
                parentPath = localFile.getFile().getParentFile();
                currentParentPath = localFile.getFile().getAbsolutePath();
            }
        }

        logger.info("Read folder: " + currentParentPath);
        localFileList = ListFilesUtils.listFilesAndFoldersWithParent(currentParentPath);

        model.addAttribute("parentPath", parentPath);
        model.addAttribute("currentPath", currentParentPath);
        model.addAttribute("localFiles", localFileList);
        return "catalogPage";
    }

    @RequestMapping("/download/{path}")
    public ResponseEntity<InputStreamResource> downloadFile1(
            @PathVariable String path) throws IOException {

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, path);
        System.out.println("fileName: " + path);
        System.out.println("mediaType: " + mediaType);

        LocalFile localFile = ListFilesUtils.findLocalFileByName(localFileList, path);

        //File file = new File(DIRECTORY + "/" + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(localFile.getFile()));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + localFile.getFile().getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(localFile.getFile().length()) //
                .body(resource);
    }


    /**
     * This method will provide LocalFile list to views
     */
    @ModelAttribute("localFilesList")
    public List<LocalFile> localFiles() {
        return localFileList;
    }
}
