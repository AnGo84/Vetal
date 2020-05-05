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
import ua.com.vetal.entity.StatisticOrder;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/statistic")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
@Slf4j
public class StatisticController extends BaseController {

    private final String title = "Statistic";
    private final String pageName = "/statistic";
    @Autowired
    private OrderServiceImpl orderService;

    private List<StatisticOrder> statisticOrderList;

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
    }

    @LogExecutionTime
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String ordersList(Model model) {
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
        return getOrderViewFilter().hasData();
    }

    @RequestMapping(value = {"/crossReport"}, method = RequestMethod.GET)
    @ResponseBody
    public void exportToExcelCrossReport(HttpServletResponse response) throws JRException, IOException {
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
    public List<StatisticOrder> getOrdersListData() {
        statisticOrderList = orderService.findByFilterData(getOrderViewFilter());
        log.info("Get OrdersList: " + statisticOrderList.size());
        return statisticOrderList;
    }
}
