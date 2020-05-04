package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.vetal.acpect.LogExecutionTime;
import ua.com.vetal.email.EmailAttachment;
import ua.com.vetal.email.EmailMessage;
import ua.com.vetal.entity.*;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.TaskJasperReportData;
import ua.com.vetal.service.*;
import ua.com.vetal.service.mail.MailServiceImp;
import ua.com.vetal.service.reports.JasperReportService;
import ua.com.vetal.utils.LoggerUtils;
import ua.com.vetal.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
@Slf4j
public class TasksController extends BaseController {
    private final String title = "Tasks";
    private final String personName = "Tasks";
    private final String pageName = "/tasks";
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ApplicationContext appContext;
    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private ViewTaskServiceImpl viewTaskService;

    //private List<Task> tasksList;
    private List<ViewTask> tasksList;

    @Autowired
    private StateServiceImpl stateService;
    @Autowired
    private PaymentServiceImpl paymentService;
    @Autowired
    private ManagerServiceImpl managerService;
    @Autowired
    private ContractorServiceImpl contractorService;
    @Autowired
    private ProductionDirectoryServiceImpl productionService;
    @Autowired
    private ProductionTypeDirectoryServiceImpl productionTypeService;
    @Autowired
    private ClientServiceImpl clientService;
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
    private DBFileStorageService dbFileStorageService;

    @Autowired
    private TaskJasperReportData reportData;
    @Autowired
    private JasperReportService jasperReportService;
    @Autowired
    private MailServiceImp mailServiceImp;

    public TasksController(Map<String, ViewFilter> viewFilters) {
        super("TasksController", viewFilters, new OrderViewFilter());
    }

    @LogExecutionTime
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String taskList(Model model) {
        model.addAttribute("title", title);
        return "tasksPage";
    }

    @LogExecutionTime
    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddTaskPage(Model model) {
        log.info("Add new {} record", title);
        Task task = new Task();
        task.setNumber((int) (taskService.getMaxID() + 1));
        model.addAttribute("readOnly", false);
        model.addAttribute("task", task);
        return "taskPage";
    }

    @LogExecutionTime
    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editTask(@PathVariable Long id, Model model) {
        log.info("Edit {} with ID= {}", title, id);
        Task task = taskService.findById(id);
        model.addAttribute("readOnly", false);
        task.setFileName(task.getDBFileName());
        model.addAttribute("task", task);
        return "taskPage";
    }

    @RequestMapping(value = "/copy-{id}", method = RequestMethod.GET)
    public String copyTask(@PathVariable Long id, Model model) {
        log.info("Copy {} with ID= {}", title, id);

        Task task = taskService.findById(id);
        Task taskCopy = task.getCopy();
        int taskCopyId = (int) (taskService.getMaxID() + 1);
        taskCopy.setNumber(taskCopyId);
        model.addAttribute("readOnly", false);
        model.addAttribute("task", taskCopy);
        return "taskPage";
    }

    @LogExecutionTime
    @RequestMapping(value = "/view-{id}", method = RequestMethod.GET)
    public String viewTask(@PathVariable Long id, Model model) {
        log.info("View {} with ID= {}", title, id);

        Task task = taskService.findById(id);
        model.addAttribute("readOnly", true);
        task.setFileName(task.getDBFileName());
        model.addAttribute("task", task);
        return "taskPage";
    }

    @LogExecutionTime
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateTask(@Valid @ModelAttribute("task") Task task, BindingResult bindingResult,
                             @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) {
        log.info("Update " + title + ": " + task);
        if (bindingResult.hasErrors()) {
            LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
            return "taskPage";
        }
        Long fileId = null;
        if (uploadFile != null && !uploadFile.isEmpty()) {
            log.info("uploadFile is not NULL");
            try {
                DBFile dbFile = dbFileStorageService.storeMultipartFile(uploadFile);
                log.info("Saved dbFile: " + dbFile);
                if (task.getDbFile() != null) {
                    fileId = task.getDbFile().getId();
                    //dbFileStorageService.deleteFile(task.getDbFile().getId());
                }
                task.setDbFile(dbFile);
            } catch (FileUploadException | FileNotFoundException e) {
                log.error(e.getMessage());
                return "taskPage";
            }
        } else {
            log.info("uploadFile is NULL");
            if (task.getDbFile() != null && (task.getFileName() == null || task.getFileName().equals(""))) {
                fileId = task.getDbFile().getId();
                task.setDbFile(null);
            }
        }
        taskService.updateObject(task);
        if (fileId != null) {
            log.info("Delete dbFile by ID= " + fileId);
            dbFileStorageService.deleteById(fileId);
        }
        return "redirect:/tasks";
    }

    @LogExecutionTime
    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deleteTask(@PathVariable Long id) {
        log.info("Delete {} with ID= {}", title, id);
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @RequestMapping(value = {"/pdfReport-{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public void pdfReportTask(@PathVariable Long id, HttpServletResponse response) throws JRException, IOException {
        log.info("Get PDF for {} with ID= {}", title, id);
        Task task = taskService.findById(id);
        jasperReportService.exportToResponseStream(JasperReportExporterType.X_PDF,
                reportData.getReportData(task), title + "_" + task.getFullNumber(), response);
    }

    @RequestMapping(value = {"/excelExport"}, method = RequestMethod.GET)
    @ResponseBody
    public void exportToExcelReportTask(HttpServletResponse response) throws JRException, IOException {
        log.info("Export {} to Excel", title);
        JasperReportData jasperReportData = reportData.getReportData(taskService.findByFilterData(getTaskViewFilter()), getTaskViewFilter());
        jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX,
                jasperReportData, title, response);

    }

    @RequestMapping(value = {"/sendEmail-{id}"}, method = RequestMethod.GET)
    public String sendEmail(Model model, @PathVariable Long id, HttpServletResponse response) throws JRException, IOException {
        log.info("Send {} with ID= {}", title, id);

        model.addAttribute("title", "email");
        boolean result = false;
        String taskMailingDeclineReason = "";

        Task task = taskService.findById(id);

        //logger.info(task.toString());
        if (task != null) {
            taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
            if (taskMailingDeclineReason.equals("")) {
                try {
                    EmailMessage emailMessage = taskService.getEmailMessage(task);

                    EmailAttachment reportAttachment = jasperReportService.getEmailAttachment(
                            JasperReportExporterType.PDF, title + "_" + task.getFullNumber(), reportData.getReportData(task));
                    if (reportAttachment != null) {
                        emailMessage.getAttachments().add(reportAttachment);
                    }
                    mailServiceImp.sendEmail(emailMessage);

                    result = true;
                    log.info("Sent {} with ID= {} from {} to {}", title, id, task.getManager().getEmail(), task.getContractor().getEmail());
                    taskMailingDeclineReason = messageSource.getMessage("message.email.sent_to",
                            new String[]{task.getContractor().getFullName(), task.getContractor().getEmail()}, new Locale("ru"));
                } catch (Exception e) {
                    log.error("Email sanding error: {}", e.getMessage());
                    //e.printStackTrace();
                    taskMailingDeclineReason = messageSource.getMessage("message.email.service_error",
                            null, new Locale("ru")) + ": " + e.getMessage();
                }
            }
        } else {
            taskMailingDeclineReason = messageSource.getMessage("message.email.miss_task_by_id",
                    new String[]{String.valueOf(id)}, new Locale("ru"));
        }
        model.addAttribute("resultSuccess", result);
        model.addAttribute("message", taskMailingDeclineReason);

        return "emailResultPage";
    }

    @GetMapping("/downloadFile-{taskId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long taskId) {
        Task task = taskService.findById(taskId);
        DBFile dbFile = task.getDbFile();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }


    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String filterTask(@ModelAttribute("taskFilterData") OrderViewFilter orderViewFilter, BindingResult bindingResult,
                             Model model) {
        updateViewFilter(orderViewFilter);
        return "redirect:/tasks";
    }

    @RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
    public String clearFilterTask() {
        updateViewFilter(new OrderViewFilter());
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


    @ModelAttribute("stateList")
    public List<State> getStatesList() {
        List<State> resultList = stateService.findAllObjects();
        /*Collections.sort(resultList, new Comparator<Worker>() {
            @Override
            public int compare(Worker m1, Worker m2) {
                return m1.getFullName().compareTo(m2.getFullName());
            }
        });*/

        return resultList;
    }

    @ModelAttribute("paymentList")
    public List<Payment> getPaymentsList() {
        List<Payment> resultList = paymentService.findAllObjects();
        /*Collections.sort(resultList, new Comparator<Worker>() {
            @Override
            public int compare(Worker m1, Worker m2) {
                return m1.getFullName().compareTo(m2.getFullName());
            }
        });*/
        return resultList;
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
		/*Collections.sort(resultList, new Comparator<Contractor>() {
			@Override
			public int compare(Contractor m1, Contractor m2) {
				return m1.getFullName().compareTo(m2.getFullName());
			}
		});
		return resultList;
		*/

        List<Contractor> result = resultList.stream()           // convert list to stream
                .filter(contractor -> !StringUtils.isEmpty(contractor.getCorpName())
                        && contractor.getManager() != null && !StringUtils.isEmpty(contractor.getLastName())
                        && !StringUtils.isEmpty(contractor.getFirstName()) && !StringUtils.isEmpty(contractor.getEmail())
                        && !StringUtils.isEmpty(contractor.getPhone()) && !StringUtils.isEmpty(contractor.getAddress())
                )
                .sorted(Comparator.comparing(Contractor::getFullName))
                .collect(Collectors.toList());

        return result;
    }

    @ModelAttribute("productionList")
    public List<ProductionDirectory> getProductionsList() {
        List<ProductionDirectory> resultList = productionService.findAllObjects();
        Collections.sort(resultList, new Comparator<ProductionDirectory>() {
            @Override
            public int compare(ProductionDirectory m1, ProductionDirectory m2) {
                return m1.getFullName().compareTo(m2.getFullName());
            }
        });

        return resultList;
    }

    @ModelAttribute("productionTypesList")
    public List<ProductionTypeDirectory> initializeProductionTypes() {
        return productionTypeService.findAllObjects();
    }

    @ModelAttribute("clientList")
    public List<Client> getClientsList() {
        List<Client> resultList = clientService.findAllObjects();

        List<Client> result = resultList.stream()           // convert list to stream
                .filter(client -> !StringUtils.isEmpty(client.getFullName())
                        && client.getManager() != null && !StringUtils.isEmpty(client.getLastName())
                        && !StringUtils.isEmpty(client.getFirstName()) && !StringUtils.isEmpty(client.getEmail())
                        && !StringUtils.isEmpty(client.getPhone()) && !StringUtils.isEmpty(client.getAddress())
                )
                .sorted(Comparator.comparing(Client::getFullName))
                .collect(Collectors.toList());

		/*Collections.sort(resultList, new Comparator<Client>() {
			@Override
			public int compare(Client m1, Client m2) {
				return m1.getFullName().compareTo(m2.getFullName());
			}
		});
		return resultList;
		*/
        return result;
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
    public OrderViewFilter getTaskViewFilter() {
        log.info("Get Filter: {}", getViewFilter());

        return (OrderViewFilter) getViewFilter();
    }

    @ModelAttribute("hasFilterData")
    public boolean isViewFilterHasData() {
        return getViewFilter().hasData();
    }

    @ModelAttribute("tasksList")
    public List<ViewTask> getViewTasksListData() {
        tasksList = viewTaskService.findByFilterData(getTaskViewFilter());
        return tasksList;
    }

}
