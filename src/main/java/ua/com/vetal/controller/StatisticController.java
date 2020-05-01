package ua.com.vetal.controller;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.com.vetal.acpect.LogExecutionTime;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.Order;
import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;
import ua.com.vetal.report.jasperReport.JasperReportData;
import ua.com.vetal.report.jasperReport.exporter.JasperReportExporterType;
import ua.com.vetal.report.jasperReport.reportdata.OrderJasperReportData;
import ua.com.vetal.service.ClientServiceImpl;
import ua.com.vetal.service.ManagerServiceImpl;
import ua.com.vetal.service.OrderServiceImpl;
import ua.com.vetal.service.ProductionDirectoryServiceImpl;
import ua.com.vetal.service.reports.JasperReportService;
import ua.com.vetal.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/statistic")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
@Slf4j
public class StatisticController extends BaseController {

    private final String title = "Statistic";
    private final String pageName = "/statistic";
    @Autowired
    private OrderServiceImpl orderService;

    private List<Order> orderList;

    @Autowired
    private ManagerServiceImpl managerService;
    @Autowired
    private ProductionDirectoryServiceImpl productionService;
    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private OrderJasperReportData reportData;
    @Autowired
    private JasperReportService jasperReportService;

    public StatisticController(Map<String, ViewFilter> viewFilters) {
        super("StatisticController", viewFilters, new OrderViewFilter());
        //initViewFilter(new OrderViewFilter());
    }

    @LogExecutionTime
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String ordersList(Model model) {
        //log.info("Get Filter: {}", orderViewFilter);
        model.addAttribute("title", title);
        model.addAttribute("ordersList", getOrdersListData());

        return "statisticPage";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String filterOrders(@ModelAttribute("statisticFilterData") OrderViewFilter orderViewFilter, BindingResult bindingResult,
                               Model model) {
        updateViewFilter(orderViewFilter);
        return "redirect:/statistic";
    }

    @RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
    public String clearOrdersViewFilter() {
        updateViewFilter(new OrderViewFilter());
        return "redirect:/statistic";
    }

    @ModelAttribute("statisticFilterData")
    public OrderViewFilter getOrderViewFilter() {
        log.info("Get FilterDate: {}", getViewFilter());
        return (OrderViewFilter) getViewFilter();
    }

    @ModelAttribute("hasFilterData")
    public boolean isViewFilterHasData() {
        //logger.info("Get Filter: " + filterData);
        return getOrderViewFilter().hasData();
    }

    @RequestMapping(value = {"/crossReport"}, method = RequestMethod.GET)
    @ResponseBody
    public void exportToExcelCrossReport(HttpServletResponse response) throws JRException, IOException {
        //log.info("Export " + title + " to Excel");

        log.info("Export {} to Excel", title);
        JasperReportData jasperReportData = reportData.getReportData(
                orderService.findByFilterData(getOrderViewFilter()), getOrderViewFilter());
        jasperReportService.exportToResponseStream(JasperReportExporterType.XLSX,
                jasperReportData, title, response);
    }

    /**
     * This methods will provide lists and fields to views
     */

    @ModelAttribute("ordersList")
    public List<Order> getOrdersListData() {
        orderList = orderService.findByFilterData(getOrderViewFilter());
        log.info("Get OrdersList: " + orderList.size());
        return orderList;
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

        List<Client> result = resultList.stream()
                .filter(client -> !StringUtils.isEmpty(client.getFullName())
                        && client.getManager() != null && !StringUtils.isEmpty(client.getLastName())
                        && !StringUtils.isEmpty(client.getFirstName()) && !StringUtils.isEmpty(client.getEmail())
                        && !StringUtils.isEmpty(client.getPhone()) && !StringUtils.isEmpty(client.getAddress())
                )
                .sorted(Comparator.comparing(Client::getFullName))
                .collect(Collectors.toList());
        return result;
    }

}
