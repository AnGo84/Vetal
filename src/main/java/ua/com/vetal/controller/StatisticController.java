package ua.com.vetal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.vetal.acpect.LogExecutionTime;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.service.ClientServiceImpl;
import ua.com.vetal.service.ManagerServiceImpl;
import ua.com.vetal.service.OrderServiceImpl;
import ua.com.vetal.service.ProductionDirectoryServiceImpl;
import ua.com.vetal.service.mail.MailServiceImp;
import ua.com.vetal.service.reports.ExporterService;
import ua.com.vetal.service.reports.JasperService;
import ua.com.vetal.utils.DateUtils;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/statistic")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class StatisticController {
	static final Logger logger = LoggerFactory.getLogger(StatisticController.class);
	@Autowired
	MessageSource messageSource;

	private String title = "Statistic";
	private String pageName = "/statistic";
	@Autowired
	private OrderServiceImpl orderService;

	@Autowired
	private FilterData filterData;

	private List<Order> orderList;

	@Autowired
	private ManagerServiceImpl managerService;
	@Autowired
	private ProductionDirectoryServiceImpl productionService;
	@Autowired
	private ClientServiceImpl clientService;

	@Autowired
	private JasperService jasperService;
	@Autowired
	private ExporterService exporterService;
	@Autowired
	private MailServiceImp mailServiceImp;

	@LogExecutionTime
	@RequestMapping(value = {""}, method = RequestMethod.GET)
	public String taskList(Model model) {
		logger.info("Get Filter: " + filterData);
		model.addAttribute("title", title);
		model.addAttribute("tasksList", getOrdersListData());

		return "statisticPage";
	}

	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public String filterTask(@ModelAttribute("taskFilterData") FilterData filterData, BindingResult bindingResult,
							 Model model) {
		this.filterData = filterData;

		return "redirect:/statistic";
	}

	@RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
	public String clearFilterTask() {
		this.filterData = new FilterData();
		return "redirect:/statistic";
	}

	@ModelAttribute("statisticFilterData")
	public FilterData getFilterData() {
		if (filterData == null) {
			filterData = new FilterData();
			filterData.setDateBeginFrom(DateUtils.firstDayOfMonth(new Date()));
		}
		logger.info("Get FilterDate: " + filterData);
		return filterData;
	}

	@ModelAttribute("hasFilterData")
	public boolean hasFilterData() {
		//logger.info("Get Filter: " + filterData);
		return getFilterData().hasData();
	}

	/**
	 * This methods will provide lists and fields to views
	 */

	@ModelAttribute("ordersList")
	public List<Order> getOrdersListData() {
		//tasksList = viewTaskService.findByFilterData(getFilterData());
		orderList = orderService.findByFilterData(getFilterData());
		logger.info("Get OrdersList : " + orderList.size());
		return orderList;
	}


}
