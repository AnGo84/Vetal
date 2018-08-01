package ua.com.vetal.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.entity.ClientDirectory;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.CringleDirectory;
import ua.com.vetal.entity.FormatDirectory;
import ua.com.vetal.entity.LaminateDirectory;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.PaperDirectory;
import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.entity.StockDirectory;
import ua.com.vetal.entity.Task;
import ua.com.vetal.service.ChromaticityDirectoryServiceImpl;
import ua.com.vetal.service.ClientDirectoryServiceImpl;
import ua.com.vetal.service.ContractorServiceImpl;
import ua.com.vetal.service.CringleDirectoryServiceImpl;
import ua.com.vetal.service.FormatDirectoryServiceImpl;
import ua.com.vetal.service.LaminateDirectoryServiceImpl;
import ua.com.vetal.service.ManagerServiceImpl;
import ua.com.vetal.service.PaperDirectoryServiceImpl;
import ua.com.vetal.service.ProductionDirectoryServiceImpl;
import ua.com.vetal.service.StockDirectoryServiceImpl;
import ua.com.vetal.service.TaskServiceImpl;

@Controller
@RequestMapping("/tasks")
// @SessionAttributes({ "managersList", "pageName" })

public class TasksController {
	static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

	// private Collator collator = Collator.getInstance(Locale.);

	private String title = "Tasks";
	private String personName = "Tasks";
	private String pageName = "/tasks";

	@Autowired
	MessageSource messageSource;

	@Autowired
	private TaskServiceImpl taskService;

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

	@RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
	public String taskList(Model model) {
		model.addAttribute("title", "main");
		model.addAttribute("tasksList", taskService.findAllObjects());
		return "redirect: /main";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddTaskPage(Model model) {
		logger.info("Add new " + title + " record");
		Task task = new Task();

		model.addAttribute("edit", false);
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
		logger.info(task.toString());

		// model.addAttribute("title", "Edit user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		model.addAttribute("edit", true);
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

		/*
		 * if (personService.isObjectExist(person)) { FieldError fieldError =
		 * new FieldError("person", "name",
		 * messageSource.getMessage("non.unique.name", new String[] {
		 * person.getName() }, Locale.getDefault()));
		 * bindingResult.addError(fieldError); return "personRecordPage"; }
		 */

		taskService.saveObject(task);

		return "redirect:/main";
	}

	@RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
	public String deleteTask(@PathVariable Long id) {
		logger.info("Delete " + title + " with ID= " + id);
		taskService.deleteById(id);
		return "redirect:/main";
	}

	/**
	 * This method will provide Title to views
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

}
