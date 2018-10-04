package ua.com.vetal.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ua.com.vetal.entity.*;
import ua.com.vetal.service.*;
import ua.com.vetal.utils.DateUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/stencils")
// @SessionAttributes({ "managersList", "pageName" })
// @SessionAttributes({"stencilFilterData"})

@PropertySource(ignoreResourceNotFound = true, value = "classpath:vetal.properties")
public class StencilsController {
    static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
    @Autowired
    MessageSource messageSource;
    // private Collator collator = Collator.getInstance(Locale.);
    @Value("${image.logo}")
    private String imageLogo;
    private String title = "Stencils";
    private String personName = "Stencils";
    private String pageName = "/stencils";
    @Autowired
    private StencilServiceImpl stencilService;

    private FilterData filterData;
    private List<Stencil> stencilList;

    @Autowired
    private ManagerServiceImpl managerService;
    @Autowired
    private NumberBaseDirectoryServiceImpl numberBaseService;
    @Autowired
    private PrinterServiceImpl printerService;
    @Autowired
    private WorkerServiceImpl workerService;
    @Autowired
    private ProductionDirectoryServiceImpl productionService;
    @Autowired
    private ClientDirectoryServiceImpl clientService;
    @Autowired
    private StockDirectoryServiceImpl stockService;
    @Autowired
    private PaperDirectoryServiceImpl paperService;
    @Autowired
    private PrintingUnitDirectoryServiceImpl printingUnitService;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String stencilList(Model model) {

        model.addAttribute("title", title);
        //model.addAttribute("stencilsList", getStencilsListData());
        // model.addAttribute("stencilList", stencilService.findAllObjects());

        return "stencilsPage";
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.GET)
    public String showAddStencilPage(Model model) {
        logger.info("Add new " + title + " record");
        Stencil stencil = new Stencil();

        //stencil.setAccount(String.valueOf(stencilDAO.getMaxID()+1));
        stencil.setNumber((int) (stencilService.getMaxID() + 1));

        // model.addAttribute("edit", false);
        model.addAttribute("readOnly", false);
        model.addAttribute("stencil", stencil);
        return "stencilPage";

    }


    @RequestMapping(value = "/edit-{id}", method = RequestMethod.GET)
    public String editStencil(@PathVariable Long id, Model model) {
        logger.info("Edit " + title + " with ID= " + id);

        Stencil stencil = stencilService.findById(id);
        // logger.info(task.toString());

        // model.addAttribute("title", "Edit user");
        // model.addAttribute("edit", true);
        model.addAttribute("readOnly", false);
        model.addAttribute("stencil", stencil);
        return "stencilPage";
    }

    @RequestMapping(value = "/copy-{id}", method = RequestMethod.GET)
    public String copyStencil(@PathVariable Long id, Model model) {
        logger.info("Copy " + title + " with ID= " + id);

        Stencil stencil = (stencilService.findById(id)).getCopy();
        //stencil.setAccount(String.valueOf(stencilDAO.getMaxID()+1));
        stencil.setNumber((int) (stencilService.getMaxID() + 1));

        logger.info("Copy stencil:" + stencil.toString());

        model.addAttribute("readOnly", false);
        model.addAttribute("stencil", stencil);
        return "stencilPage";
    }

    @RequestMapping(value = "/view-{id}", method = RequestMethod.GET)
    public String viewStencil(@PathVariable Long id, Model model) {
        logger.info("View " + title + " with ID= " + id);

        Stencil stencil = stencilService.findById(id);
        logger.info(stencil.toString());

        // model.addAttribute("title", "Edit user");
        // model.addAttribute("userRolesList",
        // userRoleService.findAllObjects());
        // model.addAttribute("edit", true);
        model.addAttribute("readOnly", true);
        model.addAttribute("stencil", stencilService.findById(id));
        return "stencilPage";
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateStencil(@Valid @ModelAttribute("stencil") Stencil stencil, BindingResult bindingResult, Model model) {
        logger.info("Update " + title + ": " + stencil);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.info(error.getDefaultMessage());
            }
            return "stencilPage";
        }

		/*if (stencilService.isAccountValueExist(stencil)) {

			FieldError fieldError = new FieldError("stencil", "account", messageSource.getMessage("non.unique.field",
					new String[] { "Счёт", stencil.getAccount().toString() }, new Locale("ru")));
			// Locale.getDefault()
			bindingResult.addError(fieldError);
			return "stencilPage";
		}
*/
        stencilService.saveObject(stencil);

        return "redirect:/stencils";
    }

    @RequestMapping(value = {"/delete-{id}"}, method = RequestMethod.GET)
    public String deleteStencil(@PathVariable Long id) {
        logger.info("Delete " + title + " with ID= " + id);
        stencilService.deleteById(id);
        return "redirect:/stencils";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String updateStencil(@ModelAttribute("stencilFilterData") FilterData filterData, BindingResult bindingResult,
                                Model model) {
        // logger.info("FilterData: " + filterData);
        this.filterData = filterData;

        return "redirect:/stencils";
    }

    @RequestMapping(value = "/clearFilter", method = RequestMethod.GET)
    //public String clearFilterTask(WebRequest request) {
    public String clearFilterTask() {
        this.filterData = new FilterData();
        //request.removeAttribute("taskFilterData", WebRequest.SCOPE_SESSION);
        return "redirect:/stencils";
    }

    @RequestMapping(value = {"/pdfReport-{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public void pdfReportStencil(@PathVariable Long id, HttpServletResponse response) throws JRException, IOException {
        logger.info("Get PDF for " + title + " with ID= " + id);
        InputStream jasperStream = this.getClass().getResourceAsStream("/jasperReport/StencilReport.jasper");
        InputStream logoTopIS = this.getClass().getResourceAsStream(imageLogo);
        InputStream logoBottomIS = this.getClass().getResourceAsStream(imageLogo);
        //URL logoURL = this.getClass().getResource(imageLogo);

        Map<String, Object> parameters = new HashMap<>();
        //parameters.put("paramID", id);
        //parameters.put("paramLOGO_IS", logoIS);
        //logger.info("URL to LOGO: " + logoURL.getFile().);
        parameters.put("paramLOGO_Top_IS", logoTopIS);
        parameters.put("paramLOGO_Bottom_IS", logoBottomIS);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        List<Stencil> stencils = new ArrayList<>();
        stencils.add(stencilService.findById(id));
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stencils);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=" + title + "_" + id + ".pdf");
        //jasperViewer.setFont(new Font("AnGo_Times_New_Roman", 0, 0));

        final OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
    }

    /**
     * This methods will provide lists and fields to views
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

    @ModelAttribute("numberBaseList")
    public List<NumberBaseDirectory> getNumberBaseList() {
        List<NumberBaseDirectory> resultList = numberBaseService.findAllObjects();
        Collections.sort(resultList, new Comparator<NumberBaseDirectory>() {
            @Override
            public int compare(NumberBaseDirectory m1, NumberBaseDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
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

    @ModelAttribute("printerList")
    public List<Printer> getPrintersList() {
        List<Printer> resultList = printerService.findAllObjects();
        Collections.sort(resultList, new Comparator<Printer>() {
            @Override
            public int compare(Printer m1, Printer m2) {
                return m1.getFullName().compareTo(m2.getFullName());
            }
        });

        return resultList;
    }

    @ModelAttribute("workerList")
    public List<Worker> getWorkersList() {
        List<Worker> resultList = workerService.findAllObjects();
        Collections.sort(resultList, new Comparator<Worker>() {
            @Override
            public int compare(Worker m1, Worker m2) {
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
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("clientList")
    public List<ClientDirectory> getClientsList() {
        List<ClientDirectory> resultList = clientService.findAllObjects();
        Collections.sort(resultList, new Comparator<ClientDirectory>() {
            @Override
            public int compare(ClientDirectory m1, ClientDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("stockList")
    public List<StockDirectory> getStocksList() {
        List<StockDirectory> resultList = stockService.findAllObjects();
        Collections.sort(resultList, new Comparator<StockDirectory>() {
            @Override
            public int compare(StockDirectory m1, StockDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }


    @ModelAttribute("paperList")
    public List<PaperDirectory> getPapersList() {
        List<PaperDirectory> resultList = paperService.findAllObjects();
        Collections.sort(resultList, new Comparator<PaperDirectory>() {
            @Override
            public int compare(PaperDirectory m1, PaperDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }

    @ModelAttribute("printingUnitList")
    public List<PrintingUnitDirectory> getPrintingUnitList() {
        List<PrintingUnitDirectory> resultList = printingUnitService.findAllObjects();
        Collections.sort(resultList, new Comparator<PrintingUnitDirectory>() {
            @Override
            public int compare(PrintingUnitDirectory m1, PrintingUnitDirectory m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });

        return resultList;
    }


    @ModelAttribute("stencilFilterData")
    public FilterData getFilterData() {
        if (filterData == null) {
            filterData = new FilterData();
            filterData.setDateBeginFrom(DateUtils.firstDayOfMonth(new Date()));
        }
        return filterData;
    }

    @ModelAttribute("hasFilterData")
    public boolean hasFilterData() {
        //logger.info("Get Filter: " + filterData);
        return getFilterData().hasData();
    }
    @ModelAttribute("stencilsList")
    public List<Stencil> getStencilsListData() {
        /*
         * if (stencilList == null || stencilList.isEmpty()) { stencilList =
         * stencilService.findAllObjects(); }
         */
        // stencilList = stencilService.findAllObjects();
        stencilList = stencilService.findByFilterData(filterData);
        // logger.info("Get TaskList : " + stencilList.size());

        return stencilList;
    }
/*
	@ModelAttribute("clientFilterList")
	public List<ClientDirectory> getClientsFilterList() {
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
	public List<Manager> getManagersFilterList() {
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
	@ModelAttribute("printerFilterList")
	public List<Printer> getPrinterFilterList() {
		List<Printer> resultList = printerService.findAllObjects();

		final Printer printer = new Printer();
		resultList.add(0, printer);

		Collections.sort(resultList, new Comparator<Printer>() {
			@Override
			public int compare(Printer m1, Printer m2) {
				return m1.getFullName().compareTo(m2.getFullName());
			}
		});

		return resultList;
	}*/

}
