package ua.com.vetal.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import ua.com.vetal.entity.ClientDirectory;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.FilterData;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.Task;
import ua.com.vetal.service.ClientDirectoryServiceImpl;
import ua.com.vetal.service.ContractorServiceImpl;
import ua.com.vetal.service.ManagerServiceImpl;
import ua.com.vetal.service.TaskServiceImpl;
import ua.com.vetal.utils.FileUtils;
import ua.com.vetal.utils.PlatformUtils;
import ua.com.vetal.utils.WebUtils;

@Controller
@SessionAttributes({ "managersFilterList", "clientFilterList", "contractorFilterList", "filterData" })
public class MainController {
	static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TaskServiceImpl taskService;

	@Autowired
	private ClientDirectoryServiceImpl clientService;
	@Autowired
	private ManagerServiceImpl managerService;
	@Autowired
	private ContractorServiceImpl contractorService;

	private FilterData filterData = new FilterData();
	private List<Task> tasksList;

	@RequestMapping(value = { "/", "/main" }, method = RequestMethod.GET)
	public String mainPage(Model model) {

		model.addAttribute("title", "main");
		// model.addAttribute("tasksList", taskService.findAllObjects());
		// model.addAttribute("filterData", filterData);
		// model.addAttribute("message", "This is welcome page!");

		return "mainPage";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "adminPage";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {

		return "loginPage";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {

		// After user login successfully.
		String userName = principal.getName();

		logger.info("User Name: " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();

		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);

		return "userInfoPage";
	}

	@RequestMapping(value = "/openFolder", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void openFolder(Model model, Principal principal) {
		// logger.info("Try open folder");
		String url = "/home/mbrunarskiy/Desktop";
		logger.info("Try open folder " + url + " for OS: " + PlatformUtils.osName());
		try {
			File file = new File(url);
			if (file.exists()) {
				FileUtils.openDirectory(url);
			} else {
				FileUtils.openDirectory("D:");
			}
		} catch (IOException e) {
			logger.info("Error on open: " + url);
			e.printStackTrace();
		}
		// return "redirect/main";
	}

	@RequestMapping(value = "tasks/filter", method = RequestMethod.GET)
	public String updateTask(@ModelAttribute("filterData") FilterData filterData, BindingResult bindingResult,
			Model model) {
		logger.info("FilterData: " + filterData);
		this.filterData = filterData;

		return "redirect:/main";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();

			String userInfo = WebUtils.toString(loginedUser);

			model.addAttribute("userInfo", userInfo);

			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);

		}

		return "403Page";
	}

	@ModelAttribute("clientFilterList")
	public List<ClientDirectory> getClientsList() {
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
	public List<Manager> getManagersList() {
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
	public List<Contractor> getContractorsList() {
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
	}

	@ModelAttribute("filterData")
	public FilterData getFilterData() {
		return filterData;
	}

	@ModelAttribute("tasksList")
	public List<Task> getTasksListData() {
		/*
		 * if (tasksList == null || tasksList.isEmpty()) { tasksList =
		 * taskService.findAllObjects(); }
		 */
		// tasksList = taskService.findAllObjects();
		tasksList = taskService.findByFilterData(filterData);
		// logger.info("Get TaskList : " + tasksList.size());

		return tasksList;
	}

	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	/*
	 * @RequestMapping(value = "/production", method = RequestMethod.GET) public
	 * String productionDirectory(Model model, Principal principal) {
	 * 
	 * if (principal != null) { } model.addAttribute("directoryList",
	 * productionDirectoryService.findAllObjects());
	 * 
	 * return "directoryPage"; }
	 */

}
