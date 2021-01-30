package ua.com.vetal.controller.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.com.vetal.entity.*;
import ua.com.vetal.service.*;
import ua.com.vetal.utils.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ModelAttributeControllerAdvice {
	@Autowired
	private StateServiceImpl stateService;
	@Autowired
	private PaymentServiceImpl paymentService;
	@Autowired
	private ManagerServiceImpl managerService;
	@Autowired
	private ContractorServiceImpl contractorService;
	@Autowired
	private ProductionDirectoryServiceImpl productionService;
	@Autowired
	private ProductionTypeDirectoryServiceImpl productionTypeService;
	@Autowired
	private ClientServiceImpl clientService;
	@Autowired
	private PrinterServiceImpl printerService;
	@Autowired
	private WorkerServiceImpl workerService;
	@Autowired
	private StockDirectoryServiceImpl stockService;
	@Autowired
	private ChromaticityDirectoryServiceImpl chromaticityService;
	@Autowired
	private FormatDirectoryServiceImpl formatService;
	@Autowired
	private LaminateDirectoryServiceImpl laminateService;
	@Autowired
	private PaperDirectoryServiceImpl paperService;
	@Autowired
	private CringleDirectoryServiceImpl cringleService;
	@Autowired
	private NumberBaseDirectoryServiceImpl numberBaseService;
	@Autowired
	private PrintingUnitDirectoryServiceImpl printingUnitService;

	@ModelAttribute("stateList")
	public List<State> getStatesList() {
		List<State> resultList = stateService.findAllObjects();
		return resultList;
	}

	@ModelAttribute("paymentList")
	public List<Payment> getPaymentsList() {
		List<Payment> resultList = paymentService.findAllObjects();
		return resultList;
	}

	@ModelAttribute("managerList")
	public List<Manager> getManagersList() {
		List<Manager> resultList = managerService.getAll();
		return resultList.stream().sorted(Comparator.comparing(Manager::getFullName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("contractorList")
	public List<Contractor> getContractorsList() {
		List<Contractor> resultList = contractorService.findAllObjects();
		List<Contractor> result = resultList.stream()
				.filter(contractor -> !StringUtils.isEmpty(contractor.getCorpName())
						&& contractor.getManager() != null && !StringUtils.isEmpty(contractor.getLastName())
						&& !StringUtils.isEmpty(contractor.getFirstName()) && !StringUtils.isEmpty(contractor.getEmail())
						&& !StringUtils.isEmpty(contractor.getPhone()) && !StringUtils.isEmpty(contractor.getAddress())
				)
				.sorted(Comparator.comparing(Contractor::getFullName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());

		return result;
	}

	@ModelAttribute("productionList")
	public List<ProductionDirectory> getProductionsList() {
		List<ProductionDirectory> resultList = productionService.findAllObjects();
		return resultList.stream().sorted(Comparator.comparing(ProductionDirectory::getFullName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("productionTypesList")
	public List<ProductionTypeDirectory> initializeProductionTypes() {
		return productionTypeService.getAll();
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
				.sorted(Comparator.comparing(Client::getFullName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
		return result;
	}

	@ModelAttribute("stockList")
	public List<StockDirectory> getStocksList() {
		List<StockDirectory> resultList = stockService.getAll();
		return resultList.stream().sorted(Comparator.comparing(StockDirectory::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("chromaticityList")
	public List<ChromaticityDirectory> getChromaticityList() {
		List<ChromaticityDirectory> resultList = chromaticityService.getAll();
		return resultList.stream().sorted(Comparator.comparing(ChromaticityDirectory::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("formatList")
	public List<FormatDirectory> getFormatsList() {
		List<FormatDirectory> resultList = formatService.getAll();
		return resultList.stream().sorted(Comparator.comparing(FormatDirectory::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("laminateList")
	public List<LaminateDirectory> getLaminatesList() {
		List<LaminateDirectory> resultList = laminateService.getAll();
		return resultList.stream().sorted(Comparator.comparing(LaminateDirectory::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("paperList")
	public List<PaperDirectory> getPapersList() {
		List<PaperDirectory> resultList = paperService.getAll();
		return resultList.stream().sorted(Comparator.comparing(PaperDirectory::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("cringleList")
	public List<CringleDirectory> getCringlesList() {
		List<CringleDirectory> resultList = cringleService.getAll();
		return resultList.stream().sorted(Comparator.comparing(CringleDirectory::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("printingUnitList")
	public List<PrintingUnitDirectory> getPrintingUnitList() {
		List<PrintingUnitDirectory> resultList = printingUnitService.getAll();
		return resultList.stream().sorted(Comparator.comparing(PrintingUnitDirectory::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("numberBaseList")
	public List<NumberBaseDirectory> getNumberBaseList() {
		List<NumberBaseDirectory> resultList = numberBaseService.getAll();
		return resultList.stream().sorted(Comparator.comparing(NumberBaseDirectory::getName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("printerList")
	public List<Printer> getPrintersList() {
		List<Printer> resultList = printerService.getAll();
		return resultList.stream().sorted(Comparator.comparing(Printer::getFullName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

	@ModelAttribute("workerList")
	public List<Worker> getWorkersList() {
		List<Worker> resultList = workerService.getAll();
		return resultList.stream().sorted(Comparator.comparing(Worker::getFullName, String.CASE_INSENSITIVE_ORDER))
				.collect(Collectors.toList());
	}

}
