package ua.com.vetal.controller;

import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.ClientFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.ClientJasperReportData;
import ua.com.vetal.service.ClientServiceImpl;
import ua.com.vetal.service.ManagerServiceImpl;
import ua.com.vetal.service.reports.JasperReportService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/clients")
// @SessionAttributes({ "title", "directoryName", "pageName" })

public class ClientController {
	static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	@Autowired
	MessageSource messageSource;
	private String title = "Clients";
	private List<Client> clientList;
	@Autowired
	private ManagerServiceImpl managerService;

	@Autowired
	private ClientServiceImpl clientService;

	@Autowired
	private ClientJasperReportData reportData;
	@Autowired
	private JasperReportService jasperReportService;

	@Autowired
	private ClientFilter clientFilter;

	@RequestMapping(value = {"", "/list"}, method = RequestMethod.GET)
	public String clientsList(Model model) {
		//model.addAttribute("clientsList", clientService.findAllObjects());
		return "clientsPage";
	}

	@RequestMapping(value = {"/add"}, method = RequestMethod.GET)
	public String showAddClientPage(Model model) {
		logger.info("Add new client record");
		Client client = new Client();

		model.addAttribute("edit", false);
		model.addAttribute("client", client);
		return "clientPage";

	}

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editClient(@PathVariable Long id, Model model) {
		logger.info("Edit Client with ID= " + id);
		model.addAttribute("edit", true);
		model.addAttribute("client", clientService.findById(id));
		return "clientPage";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateClient(@Valid @ModelAttribute("client") Client client,
							   BindingResult bindingResult, Model model) {
		logger.info("Update Client: " + client);
		if (bindingResult.hasErrors()) {
			return "clientPage";
		}
		//client.setFullName(client.getFullName().trim());
		//if (client.getId()!=null && clientService.findByName(client.getFullName()) clientService.isObjectExist(client)) {
		Client checkClient = clientService.findByName(client.getFullName());
		logger.info("Checked client: " + checkClient);
		//if ((client.getId() == null && checkClient != null) || (client.getId() != null && checkClient.getId() != null && client.getId() != checkClient.getId())) {
		if (checkClient != null && (client.getId() == null || !checkClient.getId().equals(client.getId()))) {
			FieldError fieldError = new FieldError("client", "fullName", messageSource.getMessage("non.unique.field",
					new String[]{"Название", client.getFullName()}, new Locale("ru")));

			bindingResult.addError(fieldError);
			return "clientPage";
		}
		clientService.saveObject(client);
		/*try {
			clientService.saveObject(client);
		}
		catch (Exception e){
		}*/
		return "redirect:/clients";
	}

	@RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
	public String deletePerson(@PathVariable Long id) {
		logger.info("Delete Client with ID= " + id);
		clientService.deleteById(id);
		return "redirect:/clients";
	}

	@ModelAttribute("managerList")
	public List<Manager> getManagersList() {
		List<Manager> resultList = managerService.findAllObjects();
		Collections.sort(resultList, new Comparator<Manager>() {
			@Override
			public int compare(Manager m1, Manager m2) {
				return m1.getFullName().compareTo(m2.getFullName());
			}
		});

		return resultList;
	}

	//Filter
	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public String filterClients(@ModelAttribute("clientFilterData") ClientFilter filterData, BindingResult bindingResult,
								Model model) {
		logger.info("Filter: " + filterData);
		this.clientFilter = filterData;
		return "redirect:/clients";
	}

	@RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
	public String clearFilterClients() {
		this.clientFilter = new ClientFilter();
		return "redirect:/clients";
	}

	@RequestMapping(value = {"/excelExport"}, method = RequestMethod.GET)
	@ResponseBody
	public void exportToExcelReportClients(HttpServletResponse response) throws JRException, IOException {
		//exporterService.export(ReportType.XLSX, jasperService.clientsTable(clientFilter), title, response);
		logger.info("Export " + title + " to Excel");
		JasperReportData jasperReportData = reportData.getReportData(clientService.findByFilterData(clientFilter), clientFilter);
		jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX, jasperReportData, title, response);
	}

	@ModelAttribute("clientFilterData")
	public ClientFilter getFilterData() {
		//logger.info("Get Filter: " + filterData);
		if (clientFilter == null) {
			clientFilter = new ClientFilter();
		}
		return clientFilter;
	}

	@ModelAttribute("hasFilterData")
	public boolean hasFilterData() {
		//logger.info("Get Filter: " + filterData);
		return getFilterData().hasData();
	}

	@ModelAttribute("clientsList")
	public List<Client> getViewClientsListData() {
		//logger.info("Get Filter: " + filterData);
		clientList = clientService.findByFilterData(getFilterData());
		// logger.info("Get TaskList : " + tasksList.size());

		return clientList;
	}
}
