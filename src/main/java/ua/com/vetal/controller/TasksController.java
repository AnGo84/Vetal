package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.vetal.acpect.LogExecutionTime;
import ua.com.vetal.email.EmailAttachment;
import ua.com.vetal.email.EmailMessage;
import ua.com.vetal.entity.DBFile;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.ViewTask;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.TaskJasperReportData;
import ua.com.vetal.service.DBFileStorageService;
import ua.com.vetal.service.TaskServiceImpl;
import ua.com.vetal.service.ViewTaskServiceImpl;
import ua.com.vetal.service.mail.MailServiceImp;
import ua.com.vetal.service.reports.JasperReportService;
import ua.com.vetal.utils.LoggerUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/tasks")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
@Slf4j
public class TasksController extends BaseFilteredController {
	private final String title = "Tasks";
	private final String pageName = "/tasks";

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private TaskServiceImpl taskService;
	@Autowired
	private ViewTaskServiceImpl viewTaskService;
	@Autowired
	private TaskJasperReportData reportData;
	@Autowired
	private JasperReportService jasperReportService;
	@Autowired
	private MailServiceImp mailServiceImp;
	@Autowired
	private DBFileStorageService dbFileStorageService;

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
							 @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile, Locale locale) {
		log.info("Update {}: {}", title, task);

		if (bindingResult.hasErrors()) {
			LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
			return "taskPage";
		}
		DBFile dbFile = null;
		try {
			if (uploadFile != null && !uploadFile.isEmpty()) {
				dbFile = dbFileStorageService.storeMultipartFile(uploadFile);
			}

		} catch (IOException e) {
			log.error("Error on task {} update: {}", task, e.getMessage(), e);

			FieldError fieldError = new FieldError("task", "dbFile",
					messageSource.getMessage("file.upload.error", new String[]{e.getMessage()}, locale));
			bindingResult.addError(fieldError);
			return "taskPage";
		}

		taskService.updateObject(task, dbFile);
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
		final List<Task> taskList = taskService.findByFilterData(getTaskViewFilter());
		taskList.stream().forEach(t -> t.setDbFile(null));
		JasperReportData jasperReportData = reportData.getReportData(taskList, getTaskViewFilter());
		jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX,
				jasperReportData, title, response);

	}

	@RequestMapping(value = {"/sendEmail-{id}"}, method = RequestMethod.GET)
	public String sendEmail(Model model, @PathVariable Long id, HttpServletResponse response, Locale locale) throws JRException, IOException {
		log.info("Send {} with ID= {}", title, id);

		model.addAttribute("title", "email");
		boolean result = false;
		String taskMailingDeclineReason = "";

		Task task = taskService.findById(id);

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
					taskMailingDeclineReason = getLocalizedMessage("message.email.sent_to",
							new String[]{task.getContractor().getFullName(), task.getContractor().getEmail()}, locale);
				} catch (Exception e) {
					log.error("Email sanding error: {}", e.getMessage(), e);
					taskMailingDeclineReason = getLocalizedMessage("message.email.service_error", null, locale) + ": " + e.getMessage();
				}
			}
		} else {
			taskMailingDeclineReason = getLocalizedMessage("message.email.miss_task_by_id", new String[]{String.valueOf(id)}, locale);
		}
		model.addAttribute("resultSuccess", result);
		model.addAttribute("message", taskMailingDeclineReason);

		return "emailResultPage";
	}

	private String getLocalizedMessage(String label, Object[] labelParams, Locale locale) {
		return messageSource.getMessage(label, labelParams, locale);
	}

	@GetMapping("/downloadFile-{taskId}")
	public ResponseEntity downloadFile(@PathVariable Long taskId) {
		Task task = taskService.findById(taskId);
		if (task == null || task.getDbFile() == null) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body("File downloading error");
		}

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

	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}

	@ModelAttribute("pageName")
	public String initializePageName() {
		return this.pageName;
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
		return viewTaskService.findByFilterData(getTaskViewFilter());
	}

}
