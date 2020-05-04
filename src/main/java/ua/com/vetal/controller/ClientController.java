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
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.ClientViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.ClientJasperReportData;
import ua.com.vetal.service.ClientServiceImpl;
import ua.com.vetal.service.ManagerServiceImpl;
import ua.com.vetal.service.reports.JasperReportService;
import ua.com.vetal.utils.LoggerUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/clients")
@Slf4j
public class ClientController extends BaseController {
    private final String title = "Clients";
    @Autowired
    private MessageSource messageSource;
    private List<Client> clientList;
    @Autowired
    private ManagerServiceImpl managerService;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private ClientJasperReportData reportData;
    @Autowired
    private JasperReportService jasperReportService;

    public ClientController(Map<String, ViewFilter> viewFilters) {
        super("ClientController", viewFilters, new ClientViewFilter());
    }

    @RequestMapping(value = {"", "/list"}, method = RequestMethod.GET)
    public String clientsList(Model model) {
        return "clientsPage";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddClientPage(Model model) {
        log.info("Add new client record");
        Client client = new Client();

        model.addAttribute("edit", false);
        model.addAttribute("client", client);
        return "clientPage";

    }

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editClient(@PathVariable Long id, Model model) {
        log.info("Edit Client with ID= {}", id);
        model.addAttribute("edit", true);
        model.addAttribute("client", clientService.findById(id));
        return "clientPage";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateClient(@Valid @ModelAttribute("client") Client client, BindingResult bindingResult, Model model) {
        log.info("Update Client: {}", client);
        if (bindingResult.hasErrors()) {
            LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
            return "clientPage";
        }
        Client checkClient = clientService.findByName(client.getFullName());
        log.info("Checked client: {}", checkClient);
        if (checkClient != null && (client.getId() == null || !checkClient.getId().equals(client.getId()))) {
            FieldError fieldError = new FieldError("client", "fullName", messageSource.getMessage("non.unique.field",
                    new String[]{"Название", client.getFullName()}, new Locale("ru")));

            bindingResult.addError(fieldError);
            return "clientPage";
        }
        clientService.updateObject(client);

        return "redirect:/clients";
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deletePerson(@PathVariable Long id) {
        log.info("Delete Client with ID= {}", id);
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
    public String filterClients(@ModelAttribute("clientFilterData") ClientViewFilter filterData, BindingResult bindingResult, Model model) {
        log.info("Filter: {}", filterData);
        updateViewFilter(filterData);
        return "redirect:/clients";
    }

    @RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
    public String clearClientsViewFilter() {
        updateViewFilter(new ClientViewFilter());
        return "redirect:/clients";
    }

    @RequestMapping(value = {"/excelExport"}, method = RequestMethod.GET)
    @ResponseBody
    public void exportToExcelReportClients(HttpServletResponse response) throws JRException, IOException {
        log.info("Export {} to Excel", title);
        JasperReportData jasperReportData = reportData.getReportData(clientService.findByFilterData(getViewFilterData()), getViewFilterData());
        jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX, jasperReportData, title, response);
    }

    @ModelAttribute("clientFilterData")
    public ClientViewFilter getViewFilterData() {
        return (ClientViewFilter) getViewFilter();
    }

    @ModelAttribute("hasFilterData")
    public boolean isViewFilterHasData() {
        return getViewFilter().hasData();
    }

    @ModelAttribute("clientsList")
    public List<Client> getViewClientsListData() {
        clientList = clientService.findByFilterData(getViewFilterData());
        return clientList;
    }
}
