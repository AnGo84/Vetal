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
import ua.com.vetal.entity.*;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.StencilJasperReportData;
import ua.com.vetal.service.*;
import ua.com.vetal.service.reports.JasperReportService;
import ua.com.vetal.utils.LoggerUtils;
import ua.com.vetal.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stencils")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
@Slf4j
public class StencilsController extends BaseController {

	private String title = "Stencils";
	private String personName = "Stencils";
	private String pageName = "/stencils";
	@Autowired
	private StencilServiceImpl stencilService;

	private List<Stencil> stencilList;

	@Autowired
	private PaymentServiceImpl paymentService;
	@Autowired
	private ManagerServiceImpl managerService;
	@Autowired
	private NumberBaseDirectoryServiceImpl numberBaseService;
	@Autowired
	private PrinterServiceImpl printerService;
	@Autowired
	private StateServiceImpl stateService;
	@Autowired
	private WorkerServiceImpl workerService;
	@Autowired
	private ProductionDirectoryServiceImpl productionService;
	@Autowired
	private ClientServiceImpl clientService;
	@Autowired
	private StockDirectoryServiceImpl stockService;
	@Autowired
	private PaperDirectoryServiceImpl paperService;
	@Autowired
	private PrintingUnitDirectoryServiceImpl printingUnitService;

	@Autowired
	private StencilJasperReportData reportData;
	@Autowired
	private JasperReportService jasperReportService;

	@Autowired
	private KraskoottiskService kraskoottiskService;

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

	@LogExecutionTime
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateStencil(@Valid @ModelAttribute("stencil") Stencil stencil, BindingResult bindingResult, Model model) {
		log.info("Update {}: ", stencil);
		if (bindingResult.hasErrors()) {
			LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
			return "stencilPage";
		}
		stencilService.updateObject(stencil);

		return "redirect:/stencils";
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

	/**
	 * This methods will provide lists and fields to views
	 */
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

	@ModelAttribute("stateList")
	public List<State> getStatesList() {
		List<State> resultList = stateService.findAllObjects();
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

	@ModelAttribute("printerList")
	public List<Printer> getPrintersList() {
		List<Printer> resultList = printerService.findAllObjects();
		Collections.sort(resultList, new Comparator<Printer>() {
			@Override
			public int compare(Printer m1, Printer m2) {
				return m1.getFullName().compareTo(m2.getFullName());
			}
		});

		return resultList;
	}

	@ModelAttribute("workerList")
	public List<Worker> getWorkersList() {
		List<Worker> resultList = workerService.findAllObjects();
		Collections.sort(resultList, new Comparator<Worker>() {
			@Override
			public int compare(Worker m1, Worker m2) {
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
				return m1.getFullName().compareTo(m2.getFullName());
			}
		});

		return resultList;
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

	@ModelAttribute("stencilFilterData")
	public OrderViewFilter getStencilViewFilterData() {
		log.info("Get ViewFilter: {}", getViewFilter());
		return (OrderViewFilter) getViewFilter();
	}

	@ModelAttribute("hasFilterData")
	public boolean isViewFilterHasData() {
		//log.info("Get Filter: {}", filterData);
		return getViewFilter().hasData();
	}

	@ModelAttribute("stencilsList")
	public List<Stencil> getStencilsListData() {
		stencilList = stencilService.findByFilterData(getStencilViewFilterData());
		return stencilList;
	}

	@ModelAttribute("kraskoottiskAmount")
	public double getKraskoottiskAmount() {
		List<Kraskoottisk> list = kraskoottiskService.findAllObjects();
		double amount = 0;
		if (list != null && !list.isEmpty()) {
			amount = list.get(0).getAmount();
		}
		log.info("Kraskoottisk amount: {}", amount);
		return amount;
	}

}
