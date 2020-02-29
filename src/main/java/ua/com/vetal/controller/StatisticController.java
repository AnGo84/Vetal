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
import ua.com.vetal.entity.*;
import ua.com.vetal.entity.file.FileDataSource;
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
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/statistic")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class StatisticController {
	static final Logger logger = LoggerFactory.getLogger(StatisticController.class);
	@Autowired
	MessageSource messageSource;

	private String title = "Statistic";
	private String pageName = "/statistic";
	@Autowired
	private TaskServiceImpl taskService;
	@Autowired
	private ViewTaskServiceImpl viewTaskService;

	@Autowired
	private FilterData filterData;

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
	@RequestMapping(value = {""}, method = RequestMethod.GET)
	public String taskList(Model model) {
		logger.info("Get Filter: " + filterData);
		model.addAttribute("title", title);
		//model.addAttribute("tasksList", taskService.findByFilterData(filterData));

		return "statisticPage";
	}

	/*@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public String filterTask(@ModelAttribute("taskFilterData") FilterData filterData, BindingResult bindingResult,
							 Model model) {
		this.filterData = filterData;

		return "redirect:/tasks";
	}

	@RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
	public String clearFilterTask() {
		this.filterData = new FilterData();
		return "redirect:/tasks";
	}
*/
	@ModelAttribute("statisticFilterData")
	public FilterData getFilterData() {
		if (filterData == null) {
			filterData = new FilterData();
			filterData.setDateBeginFrom(DateUtils.firstDayOfMonth(new Date()));
		}
		logger.info("Get FilterDate: " + filterData);
		return filterData;
	}

	@ModelAttribute("hasFilterData")
	public boolean hasFilterData() {
		//logger.info("Get Filter: " + filterData);
		return getFilterData().hasData();
	}

	/**
	 * This methods will provide lists and fields to views
	 */


	@ModelAttribute("ordersList")
	public List<String> getViewTasksListData() {

		//tasksList = viewTaskService.findByFilterData(getFilterData());
		// logger.info("Get TaskList : " + tasksList.size());

		return new ArrayList<>();
	}

}
