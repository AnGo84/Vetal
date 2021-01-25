package ua.com.vetal.controller.common;

public enum ControllerType {
	CHROMATICITY("Chromaticity", "/chromaticity", "Chromaticity", "label.chromaticity"),
	CRINGLE("Cringle", "/cringle", "Cringle", "label.cringle"),
	FORMAT("Format", "/format", "Format", "label.paper_format"),
	LAMINATE("Laminate", "/laminate", "Laminate", "label.laminate"),
	NUMBER_BASE("NumberBase", "/numberBases", "NumberBase", "label.number_bases"),
	PAPER("Paper", "/paper", "Paper", "label.paper"),
	PRINTING_UNIT("Printing Unit", "/printingUnit", "Printing Unit", "label.printing_unit"),
	PRODUCTION_TYPE("Production Type", "/productionType", "Production Type", "label.productionType"),
	STOCK("Stock", "/stock", "Production Type", "label.stock");

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
