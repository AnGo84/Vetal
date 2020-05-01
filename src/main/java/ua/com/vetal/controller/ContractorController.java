package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.PersonViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.ContractorJasperReportData;
import ua.com.vetal.service.ContractorServiceImpl;
import ua.com.vetal.service.ManagerServiceImpl;
import ua.com.vetal.service.reports.JasperReportService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/contractor")
@Slf4j
public class ContractorController extends BaseController {
    private final String title = "Contractor";
    private final String personName = "Contractor";
    private final String pageName = "/contractor";
    @Autowired
    MessageSource messageSource;
    private List<Contractor> contractorList;
    @Autowired
    private ManagerServiceImpl managerService;

    @Autowired
    private ContractorServiceImpl personService;

    @Autowired
    private ContractorJasperReportData reportData;
    @Autowired
    private JasperReportService jasperReportService;

    public ContractorController(Map<String, ViewFilter> viewFilters) {
        super("ContractorController", viewFilters);
        initViewFilter(new PersonViewFilter());
    }

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String personList(Model model) {
        //model.addAttribute("personList", personService.findAllObjects());
        return "contractorsPage";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddPersonPage(Model model) {
        log.info("Add new {} record", title);
        Contractor person = new Contractor();
        model.addAttribute("edit", false);
        model.addAttribute("person", person);
        return "contractorRecordPage";

    }

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editPerson(@PathVariable Long id, Model model) {
        log.info("Edit {} with ID= {}", title, id);
        // model.addAttribute("title", "Edit user");
        // model.addAttribute("userRolesList",
        // userRoleService.findAllObjects());
        model.addAttribute("edit", true);
        model.addAttribute("person", personService.findById(id));
        return "contractorRecordPage";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updatePerson(@Valid @ModelAttribute("person") Contractor person, BindingResult bindingResult,
                               Model model) {
        log.info("Update {}: {}", title, person);
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
        log.info("Checked contractor: {}", checkContractor);
        //if ((client.getId() == null && checkContractor != null) || (client.getId() != null && checkContractor.getId() != null && client.getId() != checkContractor.getId())) {
        if (checkContractor != null && (person.getId() == null || !checkContractor.getId().equals(person.getId()))) {
            FieldError fieldError = new FieldError("person", "corpName", messageSource.getMessage("non.unique.field",
                    new String[]{"Название", person.getFullName()}, new Locale("ru")));

            bindingResult.addError(fieldError);
            return "contractorRecordPage";
        }

        personService.updateObject(person);
        return "redirect:" + pageName;
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deletePerson(@PathVariable Long id) {
        log.info("Delete {} with ID= {}", title, id);
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
    }

    @ModelAttribute("pageName")
    public String initializePageName() {
        return this.pageName;
    }


    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String filterContractors(@ModelAttribute("personFilterData") PersonViewFilter filterData, BindingResult bindingResult,
                                    Model model) {
        log.info("Contractor Filter: {}", filterData);
        updateViewFilter(filterData);
        return "redirect:/contractor";
    }

    @RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
    public String clearContractorsViewFilter(Model model) {
        updateViewFilter(new PersonViewFilter());
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
    public void exportToExcelReportContractor(HttpServletResponse response) throws JRException, IOException {
        //exporterService.export(ReportType.XLSX, jasperService.contractorsTable(personFilter), title, response);
        log.info("Export {} to Excel", title);
        JasperReportData jasperReportData = reportData.getReportData(personService.findByFilterData(getViewFilterData()), getViewFilterData());
        jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX, jasperReportData, title, response);
    }


    @ModelAttribute("personFilterData")
    public PersonViewFilter getViewFilterData() {
        log.info("Get ViewFilter: {}", getViewFilter());
        return (PersonViewFilter) getViewFilter();
    }

    @ModelAttribute("hasFilterData")
    public boolean isViewFilterHasData() {
        //log.info("Get Filter: " + filterData);
        return getViewFilter().hasData();
    }

    @ModelAttribute("personList")
    public List<Contractor> getViewContractorsListData() {
        contractorList = personService.findByFilterData(getViewFilterData());
        // log.info("Get TaskList : " + tasksList.size());

        return contractorList;
    }

}
