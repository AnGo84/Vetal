package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;
import ua.com.vetal.entity.file.LocalFile;
import ua.com.vetal.utils.ListFilesUtils;
import ua.com.vetal.web.FileMediaType;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RequestMapping("/catalog")
@Controller
@SessionScope
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
@Slf4j
public class CatalogController {
    @Autowired
    private ServletContext servletContext;

    @Value("${vetal.files}")
    private String defaultParentPath;
    private String currentParentPath;
    private List<LocalFile> localFileList;
    private File parentPath;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String mainPage(Model model) {
        log.info("Read folder default: {}", defaultParentPath);

        currentParentPath = defaultParentPath;
        localFileList = ListFilesUtils.listFilesAndFoldersWithParent(currentParentPath);
        model.addAttribute("title", "Catalog");
        parentPath = new File(defaultParentPath);
        model.addAttribute("parentPath", parentPath);
        model.addAttribute("currentPath", currentParentPath);
        model.addAttribute("localFiles", localFileList);
        return "catalogPage";
    }

    /*@RequestMapping(value = {"/"}, method = RequestMethod.GET)
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
    public String readFolder(Model model, @PathVariable String path) {
        log.info("Read folder: {}", path);

        if (path == null || path.isEmpty() || path.equals("..")) {
            currentParentPath = defaultParentPath;
            parentPath = new File(defaultParentPath);
        } else {
            LocalFile localFile = ListFilesUtils.findLocalFileByName(localFileList, path);
            if (localFile == null) {
                log.info("LocalFile is null. Parent name: {}", parentPath.getName());
                if (parentPath.getName().equals(path)) {
                    log.info("Parent name: {} equal to {}", parentPath.getName(), path);

                    currentParentPath = parentPath.getAbsolutePath();
                    parentPath = parentPath.getParentFile();
                }
            } else {
                parentPath = localFile.getFile().getParentFile();
                currentParentPath = localFile.getFile().getAbsolutePath();
            }
        }

        log.info("Read folder: {}", currentParentPath);
        localFileList = ListFilesUtils.listFilesAndFoldersWithParent(currentParentPath);

        model.addAttribute("parentPath", parentPath);
        model.addAttribute("currentPath", currentParentPath);
        model.addAttribute("localFiles", localFileList);
        return "catalogPage";
    }

    @RequestMapping("/download/{path}")
    public ResponseEntity<InputStreamResource> downloadFile1(
            @PathVariable String path) throws IOException {
        FileMediaType fileMediaType = new FileMediaType(servletContext);
        MediaType mediaType = fileMediaType.getMediaTypeForFileName(path);

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

    @ModelAttribute("localFilesList")
    public List<LocalFile> localFiles() {
        return localFileList;
    }
}
