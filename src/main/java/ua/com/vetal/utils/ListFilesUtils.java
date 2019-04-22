package ua.com.vetal.utils;

import ua.com.vetal.entity.file.LocalFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFilesUtils {

    public static void main(String[] args) {
        ListFilesUtils listFilesUtil = new ListFilesUtils();
        //final String directoryLinuxMac ="/Users/loiane/test";
        //Windows directory example
        final String directoryWindows = "M://AnGo";
        File directory = new File(directoryWindows);
        //listFilesUtil.listFiles(directoryLinuxMac);
        listFilesUtil.listFilesAndFolders(directoryWindows);
        System.out.println("New:");
        List<LocalFile> list = listFilesUtil.listFilesAndFoldersWithParent(directoryWindows);
        for (LocalFile localFile : list) {
            System.out.println(localFile);
            System.out.println(localFile.getFile().getAbsolutePath().replace(directory.getAbsolutePath(),""));
        }
    }

    /**
     * List all the files and folders from a directory
     *
     * @param directoryName to be listed
     */
    public void listFilesAndFolders(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();

        for (File file : fList) {
            System.out.println(file.getName() + " : " + file.getParent());
        }
    }

    public static List<LocalFile> listFilesAndFoldersWithParent(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList == null) {
            return null;
        }

        List<LocalFile> resultList = new ArrayList<>();
        List<LocalFile> resultFilesList = new ArrayList<>();
        List<LocalFile> resultDirectoriesList = new ArrayList<>();

        for (File file : fList) {
            //System.out.println(file.getName() + " : " + file.getParent());
            if (!file.isHidden()) {
                if (file.isDirectory()) {
                    resultDirectoriesList.add(new LocalFile(file.getName(), file, file.isDirectory()));
                } else {
                    resultFilesList.add(new LocalFile(file.getName(), file, file.isDirectory()));
                }
            }
        }

        resultList.add(new LocalFile("..", directory, true));
        resultList.addAll(resultDirectoriesList);
        resultList.addAll(resultFilesList);
        return resultList;
    }

    /**
     * List all the files under a directory
     *
     * @param directoryName to be listed
     */
    public void listFiles(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }

    /**
     * List all the folder under a directory
     *
     * @param directoryName to be listed
     */
    public void listFolders(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isDirectory()) {
                System.out.println(file.getName());
            }
        }
    }

    /**
     * List all files from a directory and its subdirectories
     *
     * @param directoryName to be listed
     */
    public void listFilesAndFilesSubDirectories(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                listFilesAndFilesSubDirectories(file.getAbsolutePath());
            }
        }
    }
}
