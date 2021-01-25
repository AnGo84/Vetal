package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.vetal.controller.common.AbstractDirectoryController;
import ua.com.vetal.controller.common.ControllerType;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.service.ProductionTypeDirectoryServiceImpl;

@Controller
@RequestMapping("/productionType")
@Slf4j
public class ProductionTypeDirectoryController extends AbstractDirectoryController<ProductionTypeDirectory, ProductionTypeDirectoryServiceImpl> {

	public ProductionTypeDirectoryController(ProductionTypeDirectoryServiceImpl service) {
		super(ProductionTypeDirectory.class, ControllerType.PRODUCTION_TYPE, service);
	}
}
/*
public class ProductionTypeDirectoryController {

	private String title = "Production Type";
	private String directoryName = "Production Type";
	private String pageName = "/productionType";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ProductionTypeDirectoryServiceImpl directoryService;

	@RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
	public String directoryList(Model model) {
		model.addAttribute("directoryList", directoryService.findAllObjects());
		return "directoryPage";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddRecordPage(Model model) {
		log.info("Add new productionType record");
		ProductionTypeDirectory production = new ProductionTypeDirectory();

		model.addAttribute("edit", false);
		model.addAttribute("directory", production);
		return "directoryRecordPage";

	}

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editRecord(@PathVariable Long id, Model model) {
		log.info("Edit productionType with ID= " + id);
		model.addAttribute("edit", true);
		model.addAttribute("directory", directoryService.findById(id));
		return "directoryRecordPage";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateRecord(@Valid @ModelAttribute("directory") ProductionTypeDirectory directory,
			BindingResult bindingResult, Model model) {
		log.info("Update ProductionType: " + directory);
		if (bindingResult.hasErrors()) {
			LoggerUtils.loggingBindingResultsErrors(bindingResult, log);
			return "directoryRecordPage";
		}

		if (directoryService.isObjectExist(directory)) {
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
		log.info("Delete ProductionType with ID= " + id);
		directoryService.deleteById(id);
		return "redirect:" + pageName;
	}

	*/
/**
 * This method will provide Title to views
 *//*

	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}

	@ModelAttribute("directoryName")
	public String initializeDirectoryName() {
		String name = messageSource.getMessage("label.printingMethods", null, new Locale("ru"));
		if (name == null || name.equals("")) {
			return directoryName;
		}
		return name;
	}

	@ModelAttribute("pageName")
	public String initializePageName() {
		return this.pageName;
	}
}
*/
