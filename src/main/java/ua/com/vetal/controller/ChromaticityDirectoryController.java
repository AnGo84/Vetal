package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.service.ChromaticityDirectoryServiceImpl;

@Controller
@RequestMapping("/chromaticity")
@Slf4j
public class ChromaticityDirectoryController extends AbstractDirectoryController<ChromaticityDirectory, ChromaticityDirectoryServiceImpl> {
    public ChromaticityDirectoryController(ChromaticityDirectoryServiceImpl chromaticityDirectoryService) {
        super(ChromaticityDirectory.class, ControllerType.CHROMATICITY, chromaticityDirectoryService);
    }

	/*private String title = "Chromaticity";

	private String pageName = "/chromaticity";

	@Autowired
	private MessageSource messageSource;

	private String directoryName = "Chromaticity";

	@Autowired
	private ChromaticityDirectoryServiceImpl directoryService;

	@RequestMapping(value = { "", "/list" }, method = RequestMethod.GET)
	public String directoryList(Model model) {
		model.addAttribute("directoryList", directoryService.findAllObjects());
		return "directoryPage";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddRecordPage(Model model) {
		log.info("Add new Chronomaticity record");
		ChromaticityDirectory chronomaticity = new ChromaticityDirectory();

		model.addAttribute("edit", false);
		model.addAttribute("directory", chronomaticity);
		return "directoryRecordPage";

	}

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editRecord(@PathVariable Long id, Model model) {
		log.info("Edit Chromaticity with ID= {}", id);
		// model.addAttribute("title", "Edit user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		model.addAttribute("edit", true);
		model.addAttribute("directory", directoryService.findById(id));
		return "directoryRecordPage";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateRecord(@Valid @ModelAttribute("directory") ChromaticityDirectory directory,
			BindingResult bindingResult, Model model) {
		log.info("Update Chromaticity: {}", directory);
		if (bindingResult.hasErrors()) {
			LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
			return "directoryRecordPage";
		}

		if (directoryService.isObjectExist(directory)) {
			System.out.println("error");
			FieldError fieldError = new FieldError("directory", "name", messageSource.getMessage("non.unique.field",
					new String[]{"Название", directory.getName()}, new Locale("ru")));
			bindingResult.addError(fieldError);
			return "directoryRecordPage";
		}

		directoryService.saveObject(directory);
		return "redirect:" + pageName;
	}

	@RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
	public String deleteRecord(@PathVariable Long id) {
		log.info("Delete Chromaticity with ID= {}", id);
		directoryService.deleteById(id);
		return "redirect:" + pageName;
	}

	*//**
     * This method will provide Title to views
     *//*
	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}

	@ModelAttribute("directoryName")
	public String initializeDirectoryName() {
		String name = messageSource.getMessage("label.chromaticity", null, new Locale("ru"));
		if (name == null || name.equals("")) {
			return directoryName;
		}
		return name;
	}

	@ModelAttribute("pageName")
	public String initializePageName() {
		return this.pageName;
	}*/
}
