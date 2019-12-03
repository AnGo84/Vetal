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
import ua.com.vetal.entity.FilterData;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.Task;
import ua.com.vetal.service.StencilServiceImpl;
import ua.com.vetal.service.TaskServiceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // find
    //https://stackoverflow.com/questions/27532446/how-to-use-jasperreports-with-spring-mvc
    //https://www.baeldung.com/spring-jasper
    //jasperreport pdf view into controller set connection to database
    //https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/jasper-report-view.html
    //https://stackoverflow.com/questions/7735288/how-to-pass-the-db-connection-information-and-query-parameters-from-controller-t
    //spring boot jasper reports set connection
    //http://zetcode.com/articles/jasperspringbootweb/
    //https://cashmisa.wordpress.com/2017/12/11/jasper-report-for-spring-boot-project/
    //used
    //https://www.stackextend.com/java/generate-pdf-document-using-jasperreports-and-spring-boot/
    //https://steemit.com/technology/@vickyz/exporting-using-jasper-reports-pdf-excel-csv-and-doc

    public JasperPrint taskReport(Long id) throws JRException {
        logger.info("Get PDF for Task with ID= " + id);
        Task task = taskService.findById(id);
        //InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/TaskReport.jasper");
        InputStream jasperStream = this.getClass().getResourceAsStream(reportNameForProductType(task.getProductionType()));

        InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);

        Map<String, Object> parameters = new HashMap<>();

        //parameters.put("paramID", id);
        parameters.put("paramLOGO", logoIS);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        List<Task> tasks = new ArrayList<>();
        //tasks.add(taskService.findById(id));
        tasks.add(task);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(tasks);

        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    public JasperPrint stencilReport(Long id) throws JRException {
        logger.info("Get PDF for Task with ID= " + id);
        InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/StencilReport.jasper");

        InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);

        Map<String, Object> parameters = new HashMap<>();

        //parameters.put("paramID", id);
        parameters.put("paramLOGO_Top_IS", logoIS);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        List<Stencil> stencils = new ArrayList<>();
        stencils.add(stencilService.findById(id));

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stencils);

        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    public JasperPrint tasksTable(FilterData filterData) throws JRException {
        logger.info("Export Tasks to table");
        InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/TaskTableReport.jasper");
        InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("paramLOGO", logoIS);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(taskService.findByFilterData(filterData));

        parameters.put("tasks", dataSource);

        return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
    }

    public JasperPrint stencilsTable(FilterData filterData) throws JRException {
        logger.info("Export Stencils to table");
        InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/StencilTableReport.jasper");
        InputStream logoIS = this.getClass().getResourceAsStream(imageLogo);
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("paramLOGO", logoIS);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stencilService.findByFilterData(filterData));

        parameters.put("stencils", dataSource);

        return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
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
