package ua.com.vetal.service.reports;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.Task;
import ua.com.vetal.entity.filter.ClientFilter;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.entity.filter.PersonFilter;
import ua.com.vetal.service.*;
import ua.com.vetal.utils.DateUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class JasperService {
	static final Logger logger = LoggerFactory.getLogger(JasperService.class);

	@Value("${image.logo}")
	private String imageLogo;

	@Autowired
	private TaskServiceImpl taskService;
	@Autowired
	private StencilServiceImpl stencilService;
	@Autowired
	private ContractorServiceImpl contractorService;
	@Autowired
	private ClientServiceImpl clientService;
	@Autowired
	private OrderServiceImpl orderService;

	public JasperPrint taskReport(Long id) throws JRException {
		logger.info("Get PDF for Task with ID= " + id);
		Task task = taskService.findById(id);
		/*
		InputStream jasperStream = this.getClass().getResourceAsStream(reportNameForProductType(task.getProductionType()));

		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);

		Map<String, Object> parameters = new HashMap<>();

		parameters.put("paramLOGO", logoIS);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

		List<Task> tasks = new ArrayList<>();
		tasks.add(task);
		*/
		List<Task> tasks = Arrays.asList(task);
		//JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(tasks);
		//return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		return getJasperPrint(reportNameForProductType(task.getProductionType()), null, new JRBeanCollectionDataSource(tasks));
	}

	public JasperPrint stencilReport(Long id) throws JRException {
		logger.info("Get PDF for Task with ID= " + id);
		/*InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/StencilReport.jasper");

		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);

		Map<String, Object> parameters = new HashMap<>();

		//parameters.put("paramID", id);
		parameters.put("paramLOGO_Top_IS", logoIS);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

		List<Stencil> stencils = new ArrayList<>();
		stencils.add(stencilService.findById(id));

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stencils);*/
		List<Stencil> stencils = Arrays.asList(stencilService.findById(id));

		return getJasperPrint("/jasperReport/StencilReport.jasper",
				null, new JRBeanCollectionDataSource(stencils));

		//return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	}

	public JasperPrint tasksTable(FilterData filterData) throws JRException {
		logger.info("Export Tasks to table");
		/*InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/TaskTableReport.jasper");
		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("paramLOGO", logoIS);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(taskService.findByFilterData(filterData));

		parameters.put("tasks", dataSource);

		return getJasperPrint("/jasperReport/TaskTableReport.jasper", null, new JREmptyDataSource());
		//return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
		 */
		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(taskService.findByFilterData(filterData));
		parameters.put("tasks", dataSource);
		return getJasperPrint("/jasperReport/TaskTableReport.jasper", parameters,
				new JREmptyDataSource());
	}

	public JasperPrint stencilsTable(FilterData filterData) throws JRException {
		logger.info("Export Stencils to table");
		/*InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/StencilTableReport.jasper");
		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
		Map<String, Object> parameters = new HashMap<>();

		parameters.put("paramLOGO", logoIS);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stencilService.findByFilterData(filterData));

		parameters.put("stencils", dataSource);

		return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());*/
		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stencilService.findByFilterData(filterData));
		parameters.put("stencils", dataSource);
		return getJasperPrint("/jasperReport/StencilTableReport.jasper", parameters,
				new JREmptyDataSource());
	}


	public JasperPrint contractorsTable(PersonFilter filterData) throws JRException {
		logger.info("Export Contractors to table");
		/*InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/ContractorsTableReport.jasper");
		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
		Map<String, Object> parameters = new HashMap<>();

		parameters.put("paramLOGO", logoIS);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contractorService.findByFilterData(filterData));

		parameters.put("contractors", dataSource);

		return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());*/
		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(contractorService.findByFilterData(filterData));
		parameters.put("contractors", dataSource);
		return getJasperPrint("/jasperReport/ContractorsTableReport.jasper", parameters,
				new JREmptyDataSource());
	}

	public JasperPrint clientsTable(ClientFilter filterData) throws JRException {
		logger.info("Export Clients to table");
		/*InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/ClientsTableReport.jasper");
		logger.info("JasperStream is null: " + (jasperStream == null));
		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
		Map<String, Object> parameters = new HashMap<>();

		parameters.put("paramLOGO", logoIS);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientService.findByFilterData(filterData));

		parameters.put("clients", dataSource);

		return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());*/
		Map<String, Object> parameters = new HashMap<>();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientService.findByFilterData(filterData));
		parameters.put("clients", dataSource);
		return getJasperPrint("/jasperReport/ClientsTableReport.jasper", parameters,
				new JREmptyDataSource());
	}

	public JasperPrint ordersCrossTable(FilterData filterData) throws JRException {
		logger.info("Export Orders to Cross table");
		/*InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/OrdersCrossReport.jasper");
		logger.info("JasperStream is null: " + (jasperStream == null));
		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
		Map<String, Object> parameters = new HashMap<>();

		parameters.put("paramLOGO", logoIS);
		parameters.put("filter", filterData);

		String date = "";
		if (filterData.getDateBeginFrom() != null) {
			date = DateUtils.SIMPLE_DATE_FORMAT.format(filterData.getDateBeginFrom());
		}
		parameters.put("date_From", date);
		date = "";
		if (filterData.getDateBeginTill() != null) {
			date = DateUtils.SIMPLE_DATE_FORMAT.format(filterData.getDateBeginTill());
		}
		parameters.put("date_Till", date);

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orderService.findByFilterData(filterData));

		parameters.put("orders", dataSource);

		return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());*/
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("filter", filterData);

		String date = "";
		if (filterData.getDateBeginFrom() != null) {
			date = DateUtils.SIMPLE_DATE_FORMAT.format(filterData.getDateBeginFrom());
		}
		parameters.put("date_From", date);
		date = "";
		if (filterData.getDateBeginTill() != null) {
			date = DateUtils.SIMPLE_DATE_FORMAT.format(filterData.getDateBeginTill());
		}
		parameters.put("date_Till", date);

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orderService.findByFilterData(filterData));
		parameters.put("orders", dataSource);
		return getJasperPrint("/jasperReport/OrdersCrossReport.jasper", parameters,
				new JREmptyDataSource());
	}

	public JasperPrint getJasperPrint(String reportName, Map<String, Object> parameters, JRRewindableDataSource dataSource) throws JRException {
		logger.info("Jasper Report '{}' with params: {}", reportName, parameters);
		InputStream jasperStream = this.getClass().getResourceAsStream(reportName);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		logger.info("JasperStream is null: " + (jasperStream == null));

		if (parameters == null) {
			parameters = new HashMap<>();
		}
		InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
		parameters.put("paramLOGO", logoIS);

		return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	}

	public String reportNameForProductType(ProductionTypeDirectory productionType) {
		String reportName = "/jasperReport/TaskReport.jasper";
		if (productionType != null) {
			if (productionType.getId() == 2) {
				// широкоформатная
				reportName = "/jasperReport/Task_Large_Format_Printing_Report.jasper";
			} else if (productionType.getId() == 3 || productionType.getId() == 4) {
				// На сувенирах и на ткани
				reportName = "/jasperReport/Task_Souvenir_Report.jasper";
			} else if (productionType.getId() == 5 || productionType.getId() == 6) {
				// Оффсетная и Цифровая
				reportName = "/jasperReport/Task_Offset_and_Digital_Report.jasper";
			}
		}
		return reportName;
	}
}
