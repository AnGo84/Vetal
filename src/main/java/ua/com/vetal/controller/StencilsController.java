package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.acpect.LogExecutionTime;
import ua.com.vetal.email.EmailMessage;
import ua.com.vetal.entity.State;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.StencilJasperReportData;
import ua.com.vetal.service.StateServiceImpl;
import ua.com.vetal.service.StencilServiceImpl;
import ua.com.vetal.service.mail.MailServiceImp;
import ua.com.vetal.service.reports.JasperReportService;
import ua.com.vetal.utils.LoggerUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/stencils")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
@Slf4j
public class StencilsController extends BaseController {
	static final Set<Long> emailingStates = new HashSet<>(Arrays.asList(2l, 4l));

	private String title = "Stencils";
	private String personName = "Stencils";
	private String pageName = "/stencils";

	@Autowired
	private StencilServiceImpl stencilService;
	@Autowired
	private StencilJasperReportData reportData;
	@Autowired
	private JasperReportService jasperReportService;
	@Autowired
	private StateServiceImpl stateService;
	@Autowired
	private MailServiceImp mailService;

	public StencilsController(Map<String, ViewFilter> viewFilters) {
		super("StencilsController", viewFilters, new OrderViewFilter());
	}

	@LogExecutionTime
	@RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
	public String stencilList(Model model) {
		model.addAttribute("title", title);
		return "stencilsPage";
	}

	@LogExecutionTime
	@RequestMapping(value = {"/add"}, method = RequestMethod.GET)
	public String showAddStencilPage(Model model) {
		log.info("Add new {} record", title);
		Stencil stencil = new Stencil();
		stencil.setNumber((int) (stencilService.getMaxID() + 1));
		model.addAttribute("readOnly", false);
		model.addAttribute("stencil", stencil);
		return "stencilPage";
	}

	@LogExecutionTime
	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editStencil(@PathVariable Long id, Model model) {
		log.info("Edit {} with ID= {}", title, id);

		Stencil stencil = stencilService.findById(id);
		model.addAttribute("readOnly", false);
		model.addAttribute("stencil", stencil);
		return "stencilPage";
	}

	@LogExecutionTime
	@RequestMapping(value = "/copy-{id}", method = RequestMethod.GET)
	public String copyStencil(@PathVariable Long id, Model model) {
		log.info("Copy {} with ID= ", title, id);

		Stencil stencil = (stencilService.findById(id)).getCopy();
		stencil.setNumber((int) (stencilService.getMaxID() + 1));
		model.addAttribute("readOnly", false);
		model.addAttribute("stencil", stencil);
		return "stencilPage";
	}

	@LogExecutionTime
	@RequestMapping(value = "/view-{id}", method = RequestMethod.GET)
	public String viewStencil(@PathVariable Long id, Model model) {
		log.info("View {} with ID= {}", title, id);

		Stencil stencil = stencilService.findById(id);
		model.addAttribute("readOnly", true);
		model.addAttribute("stencil", stencilService.findById(id));
		return "stencilPage";
	}

	@RequestMapping(value = "/make_ready-{id}", method = RequestMethod.GET)
	public String makeReadyStencil(@PathVariable Long id) {
		log.info("Make ready {} with ID= {}", title, id);

		Stencil stencil = stencilService.findById(id);
		if (stencil.getState().getId().equals(2l)) {
			State readyState = stateService.findById(4l);
			stencil.setState(readyState);
			stencilService.updateObject(stencil);
			sendEmailToManager(stencil);
		}
		return "redirect:/stencils";
	}

	private void sendEmailToManager(Stencil stencil) {
		try {
			EmailMessage emailMessage = stencilService.getEmailMessage(stencil, mailService.getDefaultEmail());
			mailService.sendEmail(emailMessage);
		} catch (MessagingException e) {
			log.error("Error on sending email to manager: {}", e.getMessage(), e);
		}
	}

	@LogExecutionTime
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateStencil(@Valid @ModelAttribute("stencil") Stencil stencil, BindingResult bindingResult, Model model) {
		log.info("Update {}: ", stencil);
		if (bindingResult.hasErrors()) {
			LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
			return "stencilPage";
		}

		boolean needSend = hasNewStateForEmailing(stencil);

		stencilService.updateObject(stencil);

		if (needSend) {
			sendEmailToManager(stencil);
		}

		return "redirect:/stencils";
	}

	private boolean hasNewStateForEmailing(Stencil stencil) {
		if (stencil != null && stencil.getId() != null) {
			Stencil oldStencil = stencilService.findById(stencil.getId());
			if (oldStencil.getState().getId() != stencil.getState().getId() &&
					emailingStates.contains(stencil.getState().getId())) {
				return true;
			}
		}
		return false;
	}

	@LogExecutionTime
	@RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
	public String deleteStencil(@PathVariable Long id) {
		log.info("Delete {} with ID= {}", title, id);
		stencilService.deleteById(id);
		return "redirect:/stencils";
	}

	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public String filterStencils(@ModelAttribute("stencilFilterData") OrderViewFilter orderViewFilter, BindingResult bindingResult,
								 Model model) {
		updateViewFilter(orderViewFilter);
		return "redirect:/stencils";
	}

	@RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
	public String clearStencilsViewFilter() {
		updateViewFilter(new OrderViewFilter());
		return "redirect:/stencils";
	}

	@RequestMapping(value = {"/pdfReport-{id}"}, method = RequestMethod.GET)
	@ResponseBody
	public void pdfReportStencil(@PathVariable Long id, HttpServletResponse response) throws JRException, IOException {
		log.info("Get PDF for {} with ID= {}", title, id);
		Stencil stencil = stencilService.findById(id);
		jasperReportService.exportToResponseStream(JasperReportExporterType.X_PDF,
				reportData.getReportData(stencil), title + "_" + stencil.getFullNumber(), response);
	}


	@RequestMapping(value = {"/excelExport"}, method = RequestMethod.GET)
	@ResponseBody
	public void exportToExcelReportTask(HttpServletResponse response) throws JRException, IOException {
		log.info("Export {} to Excel", title);
		JasperReportData jasperReportData = reportData.getReportData(stencilService.findByFilterData(getStencilViewFilterData()), getStencilViewFilterData());
		jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX,
				jasperReportData, title, response);
	}

	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}

	@ModelAttribute("personName")
	public String initializePersonName() {
		return this.personName;
	}

	@ModelAttribute("pageName")
	public String initializePageName() {
		return this.pageName;
	}

	@ModelAttribute("stencilFilterData")
	public OrderViewFilter getStencilViewFilterData() {
		log.info("Get ViewFilter: {}", getViewFilter());
		return (OrderViewFilter) getViewFilter();
	}

	@ModelAttribute("hasFilterData")
	public boolean isViewFilterHasData() {
		log.debug("Get Filter: {}", getViewFilter());
		return getViewFilter().hasData();
	}

	@ModelAttribute("stencilsList")
	public List<Stencil> getStencilsListData() {
		return stencilService.findByFilterData(getStencilViewFilterData());
	}

	@ModelAttribute("kraskoottiskAmount")
	public double getKraskoottiskAmount() {
		double amount = stencilService.getKraskoottiskAmount();

		log.debug("Kraskoottisk amount: {}", amount);
		return amount;
	}

}
