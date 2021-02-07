package ua.com.vetal.controller.common;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.controller.BaseFilteredController;
import ua.com.vetal.entity.common.AbstractContragentEntity;
import ua.com.vetal.entity.common.AbstractEntity;
import ua.com.vetal.entity.filter.ContragentViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.ClientJasperReportData;
import ua.com.vetal.service.common.AbstractContragentService;
import ua.com.vetal.service.reports.JasperReportService;
import ua.com.vetal.utils.LoggerUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
public abstract class AbstractContragentController<E extends AbstractContragentEntity, S extends AbstractContragentService>
		extends BaseFilteredController implements CommonController<E> {
	private final String CONTRAGENT_LIST_PAGE = "contragentsPage";
	private final String CONTRAGENT_RECORD_PAGE = "contragentRecordPage";
	private final Class<E> objectClass;

	private final ControllerType controllerType;
	private final S service;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ClientJasperReportData reportData;
	@Autowired
	private JasperReportService jasperReportService;

	public AbstractContragentController(Class aClass, ControllerType controllerType, S contragentService, Map<String, ViewFilter> viewFilters) {
		super(aClass.getName() + "Controller", viewFilters, new ContragentViewFilter());
		this.objectClass = aClass;
		this.controllerType = controllerType;
		this.service = contragentService;
	}

	@Override
	public String allRecords(Model model) {
		log.info("Get All records {}: ", objectClass.getName());
		return CONTRAGENT_LIST_PAGE;
	}

	@Override
	public String addRecord(Model model) {
		log.info("Add new '{}' record", controllerType.getPageName());
		model.addAttribute("readOnly", false);
		model.addAttribute("contragent", createInstance(objectClass));
		return CONTRAGENT_RECORD_PAGE;
	}

	@Override
	public String editRecord(Long id, Model model) {
		log.info("Edit '{}' with ID= {}", controllerType.getPageName(), id);
		model.addAttribute("readOnly", false);
		model.addAttribute("contragent", service.get(id));
		return CONTRAGENT_RECORD_PAGE;
	}

	@Override
	public String updateRecord(@Valid @ModelAttribute("contragent") E contragent, BindingResult bindingResult, Model model) {
		log.info("Update '{}': {}", controllerType.getPageName(), contragent);
		if (bindingResult.hasErrors()) {
			LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
			return CONTRAGENT_RECORD_PAGE;
		}
		if (service.isExist(contragent)) {
			log.error("Contragent '{}' exist", contragent.getCorpName());
			FieldError fieldError = new FieldError("contragent", "corpName", messageSource.getMessage("non.unique.field",
					new String[]{"Название", contragent.getCorpName()}, new Locale("ru")));

			bindingResult.addError(fieldError);
			return CONTRAGENT_RECORD_PAGE;
		}

		AbstractEntity updated = service.update(contragent);
		log.info("Updated: {}", updated);
		return "redirect:" + controllerType.getPageLink();
	}

	@Override
	public String deleteRecord(Long id) {
		log.info("Delete {} with ID= {}", controllerType.getPageName(), id);
		service.deleteById(id);
		return "redirect:" + controllerType.getPageLink();
	}

	@RequestMapping(value = "/view-{id}", method = RequestMethod.GET)
	public String viewTask(@PathVariable Long id, Model model) {
		log.debug("View {} with ID= {}", controllerType.getPageName(), id);

		AbstractEntity contragent = service.get(id);

		model.addAttribute("readOnly", true);
		model.addAttribute("contragent", contragent);
		return CONTRAGENT_RECORD_PAGE;
	}

	//Filter
	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public String filterContragents(@ModelAttribute("contragentFilterData") ContragentViewFilter contragentViewFilter) {
		log.debug("Filter: {}", contragentViewFilter);
		updateViewFilter(contragentViewFilter);
		return "redirect:" + controllerType.getPageLink();
	}

	@RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
	public String clearContragentsViewFilter() {
		updateViewFilter(new ContragentViewFilter());
		return "redirect:" + controllerType.getPageLink();
	}

	// /Filter
	@RequestMapping(value = {"/excelExport"}, method = RequestMethod.GET)
	@ResponseBody
	public void exportToExcelReportClients(HttpServletResponse response) throws JRException, IOException {
		log.info("Export {} to Excel", controllerType.getTitle());
		JasperReportData jasperReportData = reportData.getReportData(service.findByFilterData(getViewFilterData()), getViewFilterData());
		jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX, jasperReportData, controllerType.getTitle(), response);
	}

	// Model Attributes
	@ModelAttribute("contragentFilterData")
	public ContragentViewFilter getViewFilterData() {
		log.debug("Get View FilterData");
		return (ContragentViewFilter) getViewFilter();
	}

	@ModelAttribute("title")
	public String initializeTitle() {
		return controllerType.getTitle();
	}

	@ModelAttribute("pageName")
	public String initializePageName() {
		String name = messageSource.getMessage(controllerType.getMessageLabel(), null, new Locale("ru"));
		if (name == null || name.equals("")) {
			return controllerType.getPageName();
		}
		return name;
	}

	@ModelAttribute("pageLink")
	public String initializePageLink() {
		return controllerType.getPageLink();
	}

	@ModelAttribute("contragentList")
	public List<E> getViewClientsListData() {
		log.debug("Get View ClientsListData");
		return service.findByFilterData(getViewFilterData());
	}
}
