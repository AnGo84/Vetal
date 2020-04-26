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
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.PersonFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.ContractorJasperReportData;
import ua.com.vetal.service.ContractorServiceImpl;
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
@RequestMapping("/contractor")
// @SessionAttributes({ "title", "personName", "pageName" })

public class ContractorController {
	static final Logger logger = LoggerFactory.getLogger(ContractorController.class);
	@Autowired
	MessageSource messageSource;
	private String title = "Contractor";
	private String personName = "Contractor";
	private String pageName = "/contractor";

	private List<Contractor> contractorList;
	@Autowired
	private ManagerServiceImpl managerService;

	@Autowired
	private ContractorServiceImpl personService;

	@Autowired
	private ContractorJasperReportData reportData;
	@Autowired
	private JasperReportService jasperReportService;

	@Autowired
	private PersonFilter personFilter;

	@RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
	public String personList(Model model) {
		//model.addAttribute("personList", personService.findAllObjects());
		return "contractorsPage";
	}

	@RequestMapping(value = {"/add"}, method = RequestMethod.GET)
	public String showAddPersonPage(Model model) {
		logger.info("Add new " + title + " record");
		Contractor person = new Contractor();

		model.addAttribute("edit", false);
		model.addAttribute("person", person);
		return "contractorRecordPage";

	}

	/*
	 * @RequestMapping(value = "/add", method = RequestMethod.POST) public
	 * String saveNewUser(Model model, @ModelAttribute("user") User user) {
	 *
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editPerson(@PathVariable Long id, Model model) {
		logger.info("Edit " + title + " with ID= " + id);
		// model.addAttribute("title", "Edit user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		model.addAttribute("edit", true);
		model.addAttribute("person", personService.findById(id));
		return "contractorRecordPage";
	}

	/*
	 * @RequestMapping(value = "/edit-{id}", method = RequestMethod.POST) public
	 * String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updatePerson(@Valid @ModelAttribute("person") Contractor person, BindingResult bindingResult,
							   Model model) {
		logger.info("Update " + title + ": " + person);
		if (bindingResult.hasErrors()) {
			// model.addAttribute("title", title);
			// logger.info("BINDING RESULT ERROR");
			return "contractorRecordPage";
		}


		/*if (person != null && (person.getFullName() == null || person.getFullName().equals(""))) {
			FieldError fieldError =
					new FieldError("person", "corpName",
							messageSource.getMessage("NotEmpty.field", new String[]{messageSource.getMessage("label.corpName", null, Locale.getDefault())}, Locale.getDefault()));
			bindingResult.addError(fieldError);
			return "contractorRecordPage";
		}*/

		Contractor checkContractor = personService.findByName(person.getCorpName());
		logger.info("Checked contractor: " + checkContractor);
		//if ((client.getId() == null && checkContractor != null) || (client.getId() != null && checkContractor.getId() != null && client.getId() != checkContractor.getId())) {
		if (checkContractor != null && (person.getId() == null || !checkContractor.getId().equals(person.getId()))) {
			FieldError fieldError = new FieldError("person", "corpName", messageSource.getMessage("non.unique.field",
					new String[]{"Название", person.getFullName()}, new Locale("ru")));

			bindingResult.addError(fieldError);
			return "contractorRecordPage";
		}

		personService.saveObject(person);
		return "redirect:" + pageName;
	}

	@RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
	public String deletePerson(@PathVariable Long id) {
		logger.info("Delete " + title + " with ID= " + id);
		personService.deleteById(id);
		return "redirect:" + pageName;
	}

	/**
	 * This method will provide Title to views
	 */
	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}

	@ModelAttribute("personName")
	public String initializePersonName() {
		String name = messageSource.getMessage("label.contractor", null, new Locale("ru"));
		if (name == null || name.equals("")) {
			return personName;
		}
		return name;
		// return this.personName;
	}

	@ModelAttribute("pageName")
	public String initializePageName() {
		return this.pageName;
	}


	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public String filterTask(@ModelAttribute("personFilterData") PersonFilter filterData, BindingResult bindingResult,
							 Model model) {
		logger.info("Filter: " + filterData);
		this.personFilter = filterData;

		return "redirect:/contractor";
	}

	@RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
	public String clearFilterTask(Model model) {
		this.personFilter = new PersonFilter();
		return "redirect:/contractor";
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

	@RequestMapping(value = {"/excelExport"}, method = RequestMethod.GET)
	@ResponseBody
	public void exportToExcelReportTask(HttpServletResponse response) throws JRException, IOException {
		//exporterService.export(ReportType.XLSX, jasperService.contractorsTable(personFilter), title, response);
		logger.info("Export " + title + " to Excel");
		JasperReportData jasperReportData = reportData.getReportData(personService.findByFilterData(personFilter), personFilter);
		jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX, jasperReportData, title, response);
	}


	@ModelAttribute("personFilterData")
	public PersonFilter getFilterData() {
		//logger.info("Get Filter: " + filterData);
		if (personFilter == null) {
			personFilter = new PersonFilter();
		}
		return personFilter;
	}

	@ModelAttribute("hasFilterData")
	public boolean hasFilterData() {
		//logger.info("Get Filter: " + filterData);
		return getFilterData().hasData();
	}

	@ModelAttribute("personList")
	public List<Contractor> getViewTasksListData() {
		//logger.info("Get Filter: " + filterData);
		contractorList = personService.findByFilterData(getFilterData());
		// logger.info("Get TaskList : " + tasksList.size());

		return contractorList;
	}

}
