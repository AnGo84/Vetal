package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.entity.*;
import ua.com.vetal.service.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/stencils")
// @SessionAttributes({ "managersList", "pageName" })
@SessionAttributes({ "filterData" })

public class StencilsController {
	static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	// private Collator collator = Collator.getInstance(Locale.);

	private String title = "Stencils";
	private String personName = "Stencils";
	private String pageName = "/stencils";

	@Autowired
	MessageSource messageSource;

	@Autowired
	private StencilServiceImpl stencilService;

	private FilterData filterData = new FilterData();
	private List<Stencil> stencilList;

	@Autowired
	private ManagerServiceImpl managerService;
	@Autowired
	private PrinterServiceImpl printerService;
	@Autowired
	private WorkerServiceImpl workerService;
	@Autowired
	private ProductionDirectoryServiceImpl productionService;
	@Autowired
	private ClientDirectoryServiceImpl clientService;
	@Autowired
	private StockDirectoryServiceImpl stockService;
	@Autowired
	private PaperDirectoryServiceImpl paperService;

	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public String stencilList(Model model) {

		model.addAttribute("title", title);
		// model.addAttribute("stencilList", stencilService.findAllObjects());

		return "stencilsPage";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddStencilPage(Model model) {
		logger.info("Add new " + title + " record");
		Stencil stencil = new Stencil();

		// model.addAttribute("edit", false);
		model.addAttribute("readOnly", false);
		model.addAttribute("stencil", stencil);
		return "stencilPage";

	}


	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editStencil(@PathVariable Long id, Model model) {
		logger.info("Edit " + title + " with ID= " + id);

		Stencil stencil = stencilService.findById(id);
		// logger.info(task.toString());

		// model.addAttribute("title", "Edit user");
		// model.addAttribute("edit", true);
		model.addAttribute("readOnly", false);
		model.addAttribute("stencil", stencil);
		return "stencilPage";
	}

	@RequestMapping(value = "/copy-{id}", method = RequestMethod.GET)
	public String copyStencil(@PathVariable Long id, Model model) {
		logger.info("Copy " + title + " with ID= " + id);

		Stencil stencil = (stencilService.findById(id)).getCopy();
		logger.info("Copy stencil:" + stencil.toString());

		model.addAttribute("readOnly", false);
		model.addAttribute("stencil", stencil);
		return "stencilPage";
	}

	@RequestMapping(value = "/view-{id}", method = RequestMethod.GET)
	public String viewStencil(@PathVariable Long id, Model model) {
		logger.info("View " + title + " with ID= " + id);

		Stencil stencil = stencilService.findById(id);
		logger.info(stencil.toString());

		// model.addAttribute("title", "Edit user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		// model.addAttribute("edit", true);
		model.addAttribute("readOnly", true);
		model.addAttribute("stencil", stencilService.findById(id));
		return "stencilPage";
	}


	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateStencil(@Valid @ModelAttribute("stencil") Stencil stencil, BindingResult bindingResult, Model model) {
		logger.info("Update " + title + ": " + stencil);
		if (bindingResult.hasErrors()) {
			for (ObjectError error : bindingResult.getAllErrors()) {
				logger.info(error.getDefaultMessage());
			}
			return "stencilPage";
		}

		if (stencilService.isAccountValueExist(stencil)) {

			FieldError fieldError = new FieldError("stencil", "account", messageSource.getMessage("non.unique.field",
					new String[] { "Счёт", stencil.getAccount().toString() }, new Locale("ru")));
			// Locale.getDefault()
			bindingResult.addError(fieldError);
			return "stencilPage";
		}

		stencilService.saveObject(stencil);

		return "redirect:/stencils";
	}

	@RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
	public String deleteStencil(@PathVariable Long id) {
		logger.info("Delete " + title + " with ID= " + id);
		stencilService.deleteById(id);
		return "redirect:/stencils";
	}

	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public String updateStencil(@ModelAttribute("filterData") FilterData filterData, BindingResult bindingResult,
			Model model) {
		// logger.info("FilterData: " + filterData);
		this.filterData = filterData;

		return "redirect:/stencils";
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


	@ModelAttribute("filterData")
	public FilterData getFilterData() {
		return filterData;
	}

	@ModelAttribute("stencilsList")
	public List<Stencil> getStencilsListData() {
		/*
		 * if (stencilList == null || stencilList.isEmpty()) { stencilList =
		 * stencilService.findAllObjects(); }
		 */
		// stencilList = stencilService.findAllObjects();
		stencilList = stencilService.findByFilterData(filterData);
		// logger.info("Get TaskList : " + stencilList.size());

		return stencilList;
	}

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
	@ModelAttribute("printerFilterList")
	public List<Printer> getPrinterFilterList() {
		List<Printer> resultList = printerService.findAllObjects();

		final Printer printer = new Printer();
		resultList.add(0, printer);

		Collections.sort(resultList, new Comparator<Printer>() {
			@Override
			public int compare(Printer m1, Printer m2) {
				return m1.getFullName().compareTo(m2.getFullName());
			}
		});

		return resultList;
	}

}
