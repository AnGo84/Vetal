package ua.com.vetal.controller;

import net.sf.jasperreports.engine.JRException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.vetal.acpect.LogExecutionTime;
import ua.com.vetal.email.EmailAttachment;
import ua.com.vetal.entity.*;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.service.*;
import ua.com.vetal.service.mail.MailServiceImp;
import ua.com.vetal.service.reports.ExporterService;
import ua.com.vetal.service.reports.JasperService;
import ua.com.vetal.utils.DateUtils;
import ua.com.vetal.utils.StringUtils;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
// @SessionAttributes({ "managersList", "pageName" })
// @SessionAttributes({"taskFilterData"})

@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class TasksController {
    static final Logger logger = LoggerFactory.getLogger(TasksController.class);
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
    @Autowired
    private ViewTaskServiceImpl viewTaskService;

    @Autowired
    private FilterData filterData;

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
    private JasperService jasperService;
    @Autowired
    private ExporterService exporterService;
    @Autowired
    private MailServiceImp mailServiceImp;

    @LogExecutionTime
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String taskList(Model model) {
        logger.info("Get Filter: " + filterData);
        model.addAttribute("title", title);
        //model.addAttribute("tasksList", taskService.findByFilterData(filterData));

        return "tasksPage";
    }

    @LogExecutionTime
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

    @LogExecutionTime
    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editTask(@PathVariable Long id, Model model) {
        logger.info("Edit " + title + " with ID= " + id);

        Task task = taskService.findById(id);
        // logger.info(task.toString());

        // model.addAttribute("title", "Edit user");
        // model.addAttribute("edit", true);
        model.addAttribute("readOnly", false);
        task.setFileName(task.getDBFileName());
        model.addAttribute("task", task);
        return "taskPage";
    }

    @RequestMapping(value = "/copy-{id}", method = RequestMethod.GET)
    public String copyTask(@PathVariable Long id, Model model) {
        logger.info("Copy " + title + " with ID= " + id);

        Task task = taskService.findById(id);
        logger.info("Get task:" + task.toString());
        Task taskCopy = task.getCopy();
        int taskCopyId = (int) (taskService.getMaxID() + 1);
        taskCopy.setNumber(taskCopyId);
        logger.info("Copy task:" + taskCopy.toString());

        model.addAttribute("readOnly", false);
        model.addAttribute("task", taskCopy);
        return "taskPage";
    }

    @LogExecutionTime
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
        task.setFileName(task.getDBFileName());
        model.addAttribute("task", task);
        return "taskPage";
    }

    /*
     * @RequestMapping(value = "/edit-{id}", method = RequestMethod.POST) public
     * String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
     * userService.saveObject(user); return "redirect:/usersPage"; }
     */

    @LogExecutionTime
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateTask(@Valid @ModelAttribute("task") Task task, BindingResult bindingResult,
                             @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) {
        logger.info("Update " + title + ": " + task);
        if (bindingResult.hasErrors()) {
            // model.addAttribute("title", title);
            /*
             * logger.info("BINDING RESULT ERROR"); logger.info("Date Begin: " +
             * task.getDateBegin()); logger.info("Date End: " +
             * task.getDateEnd());
             */

            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
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
        Long fileId = null;
        if (uploadFile != null && !uploadFile.isEmpty()) {
            logger.info("uploadFile is not NULL");
            try {
                DBFile dbFile = dbFileStorageService.storeMultipartFile(uploadFile);
                logger.info("Saved dbFile: " + dbFile);

                if (task.getDbFile() != null) {
                    fileId = task.getDbFile().getId();
                    //dbFileStorageService.deleteFile(task.getDbFile().getId());
                }
                task.setDbFile(dbFile);
            } catch (FileUploadException | FileNotFoundException e) {
                logger.error(e.getMessage());
                return "taskPage";
            }
        } else {
            logger.info("uploadFile is NULL");
            //logger.info("Is DBFile null: " + task.getDbFile() ) ;
            //logger.info("Is FILENAME null: " + (task.getFileName() == null || task.getFileName().equals(""))) ;
            if (task.getDbFile() != null && (task.getFileName() == null || task.getFileName().equals(""))) {

                fileId = task.getDbFile().getId();
                task.setDbFile(null);
            }
        }
        taskService.saveObject(task);
        if (fileId != null) {
            logger.info("Delete dbFile by ID= " + fileId);
            dbFileStorageService.deleteById(fileId);
        }

        return "redirect:/tasks";
    }

    @LogExecutionTime
    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deleteTask(@PathVariable Long id) {
        logger.info("Delete " + title + " with ID= " + id);
        taskService.deleteById(id);
        return "redirect:/tasks";
    }

    @RequestMapping(value = {"/pdfReport-{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public void pdfReportTask(@PathVariable Long id, HttpServletResponse response) throws JRException, IOException {
        //logger.info("Get PDF for " + title + " with ID= " + id);
        //JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        //response.setContentType("application/x-pdf");
        //response.setHeader("Content-disposition", "inline; filename=" + title + "_" + id + ".pdf");

        //final OutputStream outStream = response.getOutputStream();
        //JasperExportManager.exportReportToPdfStream(jasperService.taskReport(id), outStream);
        //exporterService.export(ReportType.PDF, jasperService.taskReport(id),title + "_" + id,response,outStream);
        exporterService.export(ReportType.PDF, jasperService.taskReport(id), title + "_" + id, response);
    }

    @RequestMapping(value = {"/excelExport"}, method = RequestMethod.GET)
    @ResponseBody
    public void exportToExcelReportTask(HttpServletResponse response) throws JRException, IOException {
        /*logger.info("Export " + title + " to Excel");
        File pdfFile = File.createTempFile("my-invoice", ".pdf");

        JasperPrint jasperPrint = jasperService.tasksTable(filterData);

        //response.setContentType("application/x-pdf");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "inline; filename=" + title + ".xlsx");


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
        */
        exporterService.export(ReportType.XLSX, jasperService.tasksTable(filterData), title, response);

    }

    @RequestMapping(value = {"/sendEmail-{id}"}, method = RequestMethod.GET)
    public String sendEmail(Model model, @PathVariable Long id, HttpServletResponse response) throws JRException, IOException {
        logger.info("Send " + title + " with ID= " + id);

        model.addAttribute("title", "email");
        boolean result = false;
        String taskMailingDeclineReason = "";

        Task task = taskService.findById(id);

        //logger.info(task.toString());
        if (task != null) {
            taskMailingDeclineReason = taskService.taskMailingDeclineReason(task);
            if (taskMailingDeclineReason.equals("")) {
                try {
                    //mailServiceImp.sendEmail(mailFrom, mailTo, "Test letter", "Test letter");
                    DataSource attachment = exporterService.getDataSource(jasperService.taskReport(task.getId()));
                    //mailServiceImp.sendEmail(mailFrom, mailTo, "Test letter", "Test letter");
                    String subject = messageSource.getMessage("email.task", null, new Locale("ru"));
                    String text = messageSource.getMessage("email.new_task", null, new Locale("ru"));

                    List<EmailAttachment> attachments = new ArrayList<>();

                    if (attachment != null) {
                        attachments.add(new EmailAttachment("Task.pdf", attachment));
                    }
                    if (task.getDbFile() != null) {
                        //DataSource source = new ByteArrayDataSource(task.getDbFile().getData(), "application/octet-stream");
                        DataSource source = new ByteArrayDataSource(task.getDbFile().getData(), task.getDbFile().getFileType());
                        attachments.add(new EmailAttachment(task.getDbFile().getFileName(), source));
                    }

                    mailServiceImp.sendMessageWithAttachment(
                            task.getManager().getEmail(), task.getContractor().getEmail(), subject + task.getNumber(),
                            text, attachments);


                    result = true;
                    logger.info("Sent " + title + " with ID= " + id + " from " + task.getManager().getEmail() + " to " + task.getContractor().getEmail());
                    taskMailingDeclineReason = messageSource.getMessage("message.email.sent_to",
                            new String[]{task.getContractor().getFullName(), task.getContractor().getEmail()}, new Locale("ru"));
                } catch (Exception e) {
                    // handle your exception when it fails to send email
                    logger.info(e.getMessage());
                    //e.printStackTrace();
                    taskMailingDeclineReason = messageSource.getMessage("message.email.service_error",
                            null, new Locale("ru")) + ": " + e.getMessage();
                }
            }
        } else {
            taskMailingDeclineReason = messageSource.getMessage("message.email.miss_task_by_id",
                    new String[]{String.valueOf(id)}, new Locale("ru"));
        }
        //return "redirect:/tasks";

        model.addAttribute("resultSuccess", result);
        model.addAttribute("message", taskMailingDeclineReason);

        return "emailResultPage";
    }

    @GetMapping("/downloadFile-{taskId}")
    //https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/
    public ResponseEntity<Resource> downloadFile(@PathVariable Long taskId) {
        // Load file from database
        Task task = taskService.findById(taskId);
        DBFile dbFile = task.getDbFile();
        //if (dbFile!=null && dbFile.getData()!=null) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
        //}
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

	/*
	@ModelAttribute("tasksList")
	public List<Task> getTasksListData() {
		//logger.info("Get Filter: " + filterData);
		tasksList = taskService.findByFilterData(getFilterData());
		// logger.info("Get TaskList : " + tasksList.size());

		return tasksList;
	}
	*/

    @ModelAttribute("tasksList")
    public List<ViewTask> getViewTasksListData() {
        //logger.info("Get Filter: " + filterData);
        tasksList = viewTaskService.findByFilterData(getFilterData());
        // logger.info("Get TaskList : " + tasksList.size());

        return tasksList;
    }

}
