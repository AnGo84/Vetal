package ua.com.vetal.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class FilterData {

	private Long id;

	private Long account;
	private Manager manager;
	private String workName;
	private String fileName;
	private Contractor contractor;
	private ProductionDirectory production;

	// @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBeginFrom;

	// @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBeginTill;

	private ClientDirectory client;

	private StockDirectory stock;

	private int printing;

	private ChromaticityDirectory chromaticity;

	private FormatDirectory format;
	private LaminateDirectory laminate;
	private PaperDirectory paper;
	private CringleDirectory cringle;
	private boolean carving;
	private boolean bending;
	private boolean assembly;
	private boolean cutting;
	private String note;
	private double amount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccount() {
		return account;
	}

	public void setAccount(Long account) {
		this.account = account;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Contractor getContractor() {
		return contractor;
	}

	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}

	public ProductionDirectory getProduction() {
		return production;
	}

	public void setProduction(ProductionDirectory production) {
		this.production = production;
	}

	/*
	 * @Temporal(TemporalType.DATE)
	 *
	 * @DateTimeFormat(pattern = "yyyy-MM-dd")
	 */
	public Date getDateBeginFrom() {
		return dateBeginFrom;
	}

	public void setDateBeginFrom(Date dateBeginFrom) {
		this.dateBeginFrom = dateBeginFrom;
	}

	public Date getDateBeginTill() {
		return dateBeginTill;
	}

	public void setDateBeginTill(Date dateBeginTill) {
		this.dateBeginTill = dateBeginTill;
	}

	public ClientDirectory getClient() {
		return client;
	}

	public void setClient(ClientDirectory client) {
		this.client = client;
	}

	public StockDirectory getStock() {
		return stock;
	}

	public void setStock(StockDirectory stock) {
		this.stock = stock;
	}

	public int getPrinting() {
		return printing;
	}

	public void setPrinting(int printing) {
		this.printing = printing;
	}

	public ChromaticityDirectory getChromaticity() {
		return chromaticity;
	}

	public void setChromaticity(ChromaticityDirectory chromaticity) {
		this.chromaticity = chromaticity;
	}

	public FormatDirectory getFormat() {
		return format;
	}

	public void setFormat(FormatDirectory format) {
		this.format = format;
	}

	public LaminateDirectory getLaminate() {
		return laminate;
	}

	public void setLaminate(LaminateDirectory laminate) {
		this.laminate = laminate;
	}

	public PaperDirectory getPaper() {
		return paper;
	}

	public void setPaper(PaperDirectory paper) {
		this.paper = paper;
	}

	public CringleDirectory getCringle() {
		return cringle;
	}

	public void setCringle(CringleDirectory cringle) {
		this.cringle = cringle;
	}

	public boolean isCarving() {
		return carving;
	}

	public void setCarving(boolean carving) {
		this.carving = carving;
	}

	public boolean isBending() {
		return bending;
	}

	public void setBending(boolean bending) {
		this.bending = bending;
	}

	public boolean isAssembly() {
		return assembly;
	}

	public void setAssembly(boolean assembly) {
		this.assembly = assembly;
	}

	public boolean isCutting() {
		return cutting;
	}

	public void setCutting(boolean cutting) {
		this.cutting = cutting;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FilterData [id=");
		builder.append(id);
		builder.append(", account=");
		builder.append(account);
		builder.append(", manager=");
		builder.append(manager);
		builder.append(", workName=");
		builder.append(workName);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", contractor=");
		builder.append(contractor);
		builder.append(", production=");
		builder.append(production);
		builder.append(", dateBeginFrom=");
		builder.append(dateBeginFrom);
		builder.append(", dateBeginTill=");
		builder.append(dateBeginTill);
		builder.append(", client=");
		builder.append(client);
		builder.append(", stock=");
		builder.append(stock);
		builder.append(", printing=");
		builder.append(printing);
		builder.append(", chromaticity=");
		builder.append(chromaticity);
		builder.append(", format=");
		builder.append(format);
		builder.append(", laminate=");
		builder.append(laminate);
		builder.append(", paper=");
		builder.append(paper);
		builder.append(", cringle=");
		builder.append(cringle);
		builder.append(", carving=");
		builder.append(carving);
		builder.append(", bending=");
		builder.append(bending);
		builder.append(", assembly=");
		builder.append(assembly);
		builder.append(", cutting=");
		builder.append(cutting);
		builder.append(", note=");
		builder.append(note);
		builder.append(", amount=");
		builder.append(amount);
		builder.append("]");
		return builder.toString();
	}

}
