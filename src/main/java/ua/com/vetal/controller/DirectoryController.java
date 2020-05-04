package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.vetal.entity.AbstractDirectory;
import ua.com.vetal.entity.User;

import javax.validation.Valid;

//@Controller
//@RequestMapping("/directory")
@Slf4j
public class DirectoryController {

	private String title = "user";

	@Autowired
	MessageSource messageSource;

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String showAddRecordPage(Model model) {
		log.info("Add new record");
		User user = new User();
		user.setEnabled(true);
		title = "New user";
		model.addAttribute("edit", false);
		model.addAttribute("user", user);
		return "userPage";

	}

	@RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
	public String editRecord(@PathVariable Long id, Model model) {
		log.info("Edit user with ID= {}", id);
		title = "Edit user";
		model.addAttribute("edit", true);
		return "userPage";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateRecord(@Valid @ModelAttribute("user") AbstractDirectory abstractDirectory,
			BindingResult bindingResult, Model model) {
		log.info("Update User: {}", abstractDirectory);
		if (bindingResult.hasErrors()) {
			return "userPage";
		}

		return "redirect:/users";
	}

	/**
	 * This method will provide Title to views
	 */
	@ModelAttribute("title")
	public String initializeTitle() {
		return this.title;
	}
}
