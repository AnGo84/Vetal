package ua.com.vetal.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.entity.*;
import ua.com.vetal.service.*;
import ua.com.vetal.utils.DateUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/tasks")
// @SessionAttributes({ "managersList", "pageName" })
//@SessionAttributes({"taskFilterData"})

@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class TasksController {
    static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
    @Autowired
    MessageSource messageSource;
    // private Collator collator = Collator.getInstance(Locale.);

    @Autowired
    private ApplicationContext appContext;
    private String title = "Tasks";
    private String personName = "Tasks";
    private String pageName = "/tasks";
    @Autowired
    private TaskServiceImpl taskService;

    private FilterData filterData;
    private List<Task> tasksList;

    @Autowired
    private ManagerServiceImpl managerService;
    @Autowired
    private ContractorServiceImpl contractorService;
    @Autowired
    private ProductionDirectoryServiceImpl productionService;
    @Autowired
    private ClientDirectoryServiceImpl clientService;
    @Autowired
    private StockDirectoryServiceImpl stockService;
    @Autowired
    private ChromaticityDirectoryServiceImpl chromaticityService;
    @Autowired
    private FormatDirectoryServiceImpl formatService;
    @Autowired
    private LaminateDirectoryServiceImpl laminateService;
    @Autowired
    private PaperDirectoryServiceImpl paperService;
    @Autowired
    private CringleDirectoryServiceImpl cringleService;
    @Autowired
    private NumberBaseDirectoryServiceImpl numberBaseService;
    @Autowired
    private PrintingUnitDirectoryServiceImpl printingUnitService;

    @Autowired
    private JasperService jasperService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String taskList(Model model) {
        //logger.info("Get Filter: " + filterData);
        model.addAttribute("title", title);
        //model.addAttribute("tasksList", taskService.findByFilterData(filterData));

        return "tasksPage";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddTaskPage(Model model) {
        logger.info("Add new " + title + " record");
        Task task = new Task();
        task.setNumber((int) (taskService.getMaxID() + 1));

        // model.addAttribute("edit", false);
        model.addAttribute("readOnly", false);
        model.addAttribute("task", task);
        return "taskPage";

    }

    /*
     * @RequestMapping(value = "/add", method = RequestMethod.POST) public
     * String saveNewUser(Model model, @ModelAttribute("user") User user) {
     *
     * userService.saveObject(user); return "redirect:/usersPage"; }
     */

    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editTask(@PathVariable Long id, Model model) {
        logger.info("Edit " + title + " with ID= " + id);

        Task task = taskService.findById(id);
        // logger.info(task.toString());

        // model.addAttribute("title", "Edit user");
        // model.addAttribute("edit", true);
        model.addAttribute("readOnly", false);
        model.addAttribute("task", task);
        return "taskPage";
    }

    @RequestMapping(value = "/copy-{id}", method = RequestMethod.GET)
    public String copyTask(@PathVariable Long id, Model model) {
        logger.info("Copy " + title + " with ID= " + id);

        Task task = (taskService.findById(id)).getCopy();
        task.setNumber((int) (taskService.getMaxID() + 1));
        logger.info("Copy task:" + task.toString());

        model.addAttribute("readOnly", false);
        model.addAttribute("task", task);
        return "taskPage";
    }

    @RequestMapping(value = "/view-{id}", method = RequestMethod.GET)
    public String viewTask(@PathVariable Long id, Model model) {
        logger.info("View " + title + " with ID= " + id);

        Task task = taskService.findById(id);
        logger.info(task.toString());

        // model.addAttribute("title", "Edit user");
        // model.addAttribute("userRolesList",
        // userRoleService.findAllObjects());
        // model.addAttribute("edit", true);
        model.addAttribute("readOnly", true);
        model.addAttribute("task", taskService.findById(id));
        return "taskPage";
    }

    /*
     * @RequestMapping(value = "/edit-{id}", method = RequestMethod.POST) public
     * String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
     * userService.saveObject(user); return "redirect:/usersPage"; }
     */

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateTask(@Valid @ModelAttribute("task") Task task, BindingResult bindingResult, Model model) {
        logger.info("Update " + title + ": " + task);
        if (bindingResult.hasErrors()) {
            // model.addAttribute("title", title);
            /*
             * logger.info("BINDING RESULT ERROR"); logger.info("Date Begin: " +
             * task.getDateBegin()); logger.info("Date End: " +
             * task.getDateEnd());
             */

            for (ObjectError error : bindingResult.getAllErrors()) {

                logger.info(error.getDefaultMessage());
            }
            return "taskPage";
        }

        /*if (taskService.isAccountValueExist(task)) {

            FieldError fieldError = new FieldError("task", "account", messageSource.getMessage("non.unique.field",
                    new String[]{"Счёт", task.getAccount().toString()}, new Locale("ru")));
            // Locale.getDefault()
            bindingResult.addError(fieldError);
            return "taskPage";
        }*/

        taskService.saveObject(task);

        return "redirect:/tasks";
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deleteTask(@PathVariable Long id) {
        logger.info("Delete " + title + " with ID= " + id);
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @RequestMapping(value = {"/pdfReport-{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public void pdfReportTask(@PathVariable Long id, HttpServletResponse response) throws JRException, IOException {
        logger.info("Get PDF for " + title + " with ID= " + id);
        //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=" + title + "_" + id + ".pdf");
        //jasperViewer.setFont(new Font("AnGo_Times_New_Roman", 0, 0));
        final OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperService.taskReport(id), outStream);
    }

    @RequestMapping(value = {"/excelExport"}, method = RequestMethod.GET)
    @ResponseBody
    public void exportToExcelReportTask(HttpServletResponse response) throws JRException, IOException {
        logger.info("Export " + title + " to Excel");
        File pdfFile = File.createTempFile("my-invoice", ".pdf");

        JasperPrint jasperPrint = jasperService.tasksTable(filterData);

        //response.setContentType("application/x-pdf");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=" + title + ".xlsx");

        //response.setHeader("Content-Disposition", "attachment;filenameTasks.xlsx");
        //response.setContentType("application/octet-stream");

        final OutputStream outputStream = response.getOutputStream();
        //JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);


        //response.setContentLength(4096);//too small
        //outputStream = response.getOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        //exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        //exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
        //exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "Tasks.xlsx");


        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        exporter.setConfiguration(configuration);

        exporter.exportReport();
    }


    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String filterTask(@ModelAttribute("taskFilterData") FilterData filterData, BindingResult bindingResult,
                             Model model) {
        // logger.info("FilterData: " + filterData);
        this.filterData = filterData;

        return "redirect:/tasks";
    }

    @RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
    //public String clearFilterTask(WebRequest request) {
    public String clearFilterTask() {
        this.filterData = new FilterData();
        //request.removeAttribute("taskFilterData", WebRequest.SCOPE_SESSION);
        return "redirect:/tasks";
    }

    /**
     * This methods will provide lists and fields to views
     */
    @ModelAttribute("title")
    public String initializeTitle() {
        return this.title;
    }

    @ModelAttribute("personName")
    public String initializepersonName() {
        return this.personName;
    }

    @ModelAttribute("pageName")
    public String initializePageName() {
        return this.pageName;
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

    @ModelAttribute("contractorList")
    public List<Contractor> getContractorsList() {
        List<Contractor> resultList = contractorService.findAllObjects();
        Collections.sort(resultList, new Comparator<Contractor>() {
            @Override
            public int compare(Contractor m1, Contractor m2) {
                return m1.getFullName().compareTo(m2.getFullName());
            }
        });

        return resultList;
    }

    @ModelAttribute("productionList")
    public List<ProductionDirectory> getProductionsList() {
        List<ProductionDirectory> resultList = productionService.findAllObjects();
        Collections.sort(resultList, new Comparator<ProductionDirectory>() {
            @Override
            public int compare(ProductionDirectory m1, ProductionDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("clientList")
    public List<ClientDirectory> getClientsList() {
        List<ClientDirectory> resultList = clientService.findAllObjects();
        Collections.sort(resultList, new Comparator<ClientDirectory>() {
            @Override
            public int compare(ClientDirectory m1, ClientDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("stockList")
    public List<StockDirectory> getStocksList() {
        List<StockDirectory> resultList = stockService.findAllObjects();
        Collections.sort(resultList, new Comparator<StockDirectory>() {
            @Override
            public int compare(StockDirectory m1, StockDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("chromaticityList")
    public List<ChromaticityDirectory> getChromaticityList() {
        List<ChromaticityDirectory> resultList = chromaticityService.findAllObjects();
        Collections.sort(resultList, new Comparator<ChromaticityDirectory>() {
            @Override
            public int compare(ChromaticityDirectory m1, ChromaticityDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("formatList")
    public List<FormatDirectory> getFormatsList() {
        List<FormatDirectory> resultList = formatService.findAllObjects();
        Collections.sort(resultList, new Comparator<FormatDirectory>() {
            @Override
            public int compare(FormatDirectory m1, FormatDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("laminateList")
    public List<LaminateDirectory> getLaminatesList() {
        List<LaminateDirectory> resultList = laminateService.findAllObjects();
        Collections.sort(resultList, new Comparator<LaminateDirectory>() {
            @Override
            public int compare(LaminateDirectory m1, LaminateDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("paperList")
    public List<PaperDirectory> getPapersList() {
        List<PaperDirectory> resultList = paperService.findAllObjects();
        Collections.sort(resultList, new Comparator<PaperDirectory>() {
            @Override
            public int compare(PaperDirectory m1, PaperDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("cringleList")
    public List<CringleDirectory> getCringlesList() {
        List<CringleDirectory> resultList = cringleService.findAllObjects();
        Collections.sort(resultList, new Comparator<CringleDirectory>() {
            @Override
            public int compare(CringleDirectory m1, CringleDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }


    @ModelAttribute("printingUnitList")
    public List<PrintingUnitDirectory> getPrintingUnitList() {
        List<PrintingUnitDirectory> resultList = printingUnitService.findAllObjects();
        Collections.sort(resultList, new Comparator<PrintingUnitDirectory>() {
            @Override
            public int compare(PrintingUnitDirectory m1, PrintingUnitDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("numberBaseList")
    public List<NumberBaseDirectory> getNumberBaseList() {
        List<NumberBaseDirectory> resultList = numberBaseService.findAllObjects();
        Collections.sort(resultList, new Comparator<NumberBaseDirectory>() {
            @Override
            public int compare(NumberBaseDirectory m1, NumberBaseDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("taskFilterData")
    public FilterData getFilterData() {
        //logger.info("Get Filter: " + filterData);
        if (filterData == null) {
            filterData = new FilterData();
            filterData.setDateBeginFrom(DateUtils.firstDayOfMonth(new Date()));
        }
        return filterData;
    }

    @ModelAttribute("hasFilterData")
    public boolean hasFilterData() {
        //logger.info("Get Filter: " + filterData);
        return getFilterData().hasData();
    }


    @ModelAttribute("tasksList")
    public List<Task> getTasksListData() {
        /*
         * if (tasksList == null || tasksList.isEmpty()) { tasksList =
         * taskService.findAllObjects(); }
         */
        // tasksList = taskService.findAllObjects();
        logger.info("Get Filter: " + filterData);
        tasksList = taskService.findByFilterData(getFilterData());
        // logger.info("Get TaskList : " + tasksList.size());

        return tasksList;
    }
/*
    @ModelAttribute("clientFilterList")
    public List<ClientDirectory> getClientsFilterList() {
        List<ClientDirectory> resultList = clientService.findAllObjects();

        final ClientDirectory client = new ClientDirectory();
        client.setName("");

        resultList.add(0, client);

        Collections.sort(resultList, new Comparator<ClientDirectory>() {
            @Override
            public int compare(ClientDirectory m1, ClientDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("managerFilterList")
    public List<Manager> getManagersFilterList() {
        List<Manager> resultList = managerService.findAllObjects();

        final Manager manager = new Manager();
        resultList.add(0, manager);

        Collections.sort(resultList, new Comparator<Manager>() {
            @Override
            public int compare(Manager m1, Manager m2) {
                return m1.getFullName().compareTo(m2.getFullName());
            }
        });

        return resultList;
    }

    @ModelAttribute("contractorFilterList")
    public List<Contractor> getContractorsFilterList() {
        List<Contractor> resultList = contractorService.findAllObjects();
        final Contractor contractor = new Contractor();
        resultList.add(0, contractor);
        Collections.sort(resultList, new Comparator<Contractor>() {
            @Override
            public int compare(Contractor m1, Contractor m2) {
                return m1.getFullName().compareTo(m2.getFullName());
            }
        });

        return resultList;
    }*/
}
