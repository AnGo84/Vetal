package ua.com.vetal.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ua.com.vetal.entity.DBFile;
import ua.com.vetal.repositories.DBFileRepository;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class DBFileStorageService {

    @Autowired
    private DBFileRepository dbFileRepository;

    public DBFile storeMultipartFile(MultipartFile file) throws FileUploadException, FileNotFoundException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            throw new FileNotFoundException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        try {
            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileUploadException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DBFile save(DBFile dbFile) {
        return dbFileRepository.save(dbFile);
    }

    public DBFile findById(Long fileId) throws FileNotFoundException {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }

    public void deleteById(Long fileId) {
        dbFileRepository.deleteById(fileId);
    }
}
