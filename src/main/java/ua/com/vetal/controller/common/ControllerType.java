package ua.com.vetal.controller.common;

public enum ControllerType {
    CHROMATICITY("Chromaticity", "/chromaticity", "Chromaticity", "chromaticity");

    private String title;
    private String pageName;
    private String directoryName;
    private String label;

    ControllerType(String title, String pageName, String directoryName, String label) {
        this.title = title;
        this.pageName = pageName;
        this.directoryName = directoryName;
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public String getPageName() {
        return pageName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getLabel() {
        return label;
    }
}
