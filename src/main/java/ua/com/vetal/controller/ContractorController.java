package ua.com.vetal.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.com.vetal.entity.Contractor;
import ua.com.vetal.service.ContractorServiceImpl;

@Controller
@RequestMapping("/contractor")
// @SessionAttributes({ "title", "personName", "pageName" })

public class ContractorController {
	static final Logger logger = LoggerFactory.getLogger(ContractorController.class);

	private String title = "Contractor";
	private String personName = "Contractor";
	private String pageName = "/contractor";

	@Autowired
	MessageSource messageSource;

	@Autowired
	private ContractorServiceImpl personService;

	@RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
	public String personList(Model model) {
		model.addAttribute("personList", personService.findAllObjects());
		return "personsPage";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddPersonPage(Model model) {
		logger.info("Add new " + title + " record");
		Contractor person = new Contractor();

		model.addAttribute("edit", false);
		model.addAttribute("person", person);
		return "personRecordPage";

	}

	/*
	 * @RequestMapping(value = "/add", method = RequestMethod.POST) public
	 * String saveNewUser(Model model, @ModelAttribute("user") User user) {
	 * 
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editPerson(@PathVariable Long id, Model model) {
		logger.info("Edit " + title + " with ID= " + id);
		// model.addAttribute("title", "Edit user");
		// model.addAttribute("userRolesList",
		// userRoleService.findAllObjects());
		model.addAttribute("edit", true);
		model.addAttribute("person", personService.findById(id));
		return "personRecordPage";
	}

	/*
	 * @RequestMapping(value = "/edit-{id}", method = RequestMethod.POST) public
	 * String saveUpdateUser(Model model, @ModelAttribute("user") User user) {
	 * userService.saveObject(user); return "redirect:/usersPage"; }
	 */

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updatePerson(@Valid @ModelAttribute("person") Contractor person, BindingResult bindingResult,
			Model model) {
		logger.info("Update " + title + ": " + person);
		if (bindingResult.hasErrors()) {
			// model.addAttribute("title", title);
			// logger.info("BINDING RESULT ERROR");
			return "personRecordPage";
		}

		/*
		 * if (personService.isObjectExist(person)) { FieldError fieldError =
		 * new FieldError("person", "name",
		 * messageSource.getMessage("non.unique.name", new String[] {
		 * person.getName() }, Locale.getDefault()));
		 * bindingResult.addError(fieldError); return "personRecordPage"; }
		 */

		personService.saveObject(person);
		return "redirect:" + pageName;
	}

	@RequestMapping(value = { "/delete-{id}" }, method = RequestMethod.GET)
	public String deletePerson(@PathVariable Long id) {
		logger.info("Delete " + title + " with ID= " + id);
		personService.deleteById(id);
		return "redirect:" + pageName;
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
}
