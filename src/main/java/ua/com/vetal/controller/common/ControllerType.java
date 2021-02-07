package ua.com.vetal.controller.common;

import lombok.Getter;

@Getter
public enum ControllerType {
	CHROMATICITY("Chromaticity", "/chromaticity", "Chromaticity", "label.chromaticity"),
	CRINGLE("Cringle", "/cringle", "Cringle", "label.cringle"),
	FORMAT("Format", "/format", "Format", "label.paper_format"),
	LAMINATE("Laminate", "/laminate", "Laminate", "label.laminate"),
	NUMBER_BASE("NumberBase", "/numberBases", "NumberBase", "label.number_bases"),
	PAPER("Paper", "/paper", "Paper", "label.paper"),
	PRINTING_UNIT("Printing Unit", "/printingUnit", "Printing Unit", "label.printing_unit"),
	PRODUCTION_TYPE("Production Type", "/productionType", "Production Type", "label.productionType"),
	STOCK("Stock", "/stock", "Stock", "label.stock"),
	/**/
	WORKER("Worker", "/worker", "Worker", "label.worker"),
	PRINTER("Printer", "/printer", "Printer", "label.printer"),
	MANAGER("Manager", "/manager", "Manager", "label.manager"),
	/**/
	CLIENT("Clients", "/clients", "Client", "label.client"),
	CONTRACTOR("Contractor", "/contractor", "Contractor", "label.contractor");

	private String title;
	private String pageLink;
	private String pageName;
	private String messageLabel;

	ControllerType(String title, String pageLink, String pageName, String messageLabel) {
		this.title = title;
		this.pageLink = pageLink;
		this.pageName = pageName;
		this.messageLabel = messageLabel;
	}

}
