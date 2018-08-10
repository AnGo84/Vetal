package ua.com.vetal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "tasks")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@Column(name = "Account", nullable = false, unique = true)
	private Long account;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Manager_ID")
	private Manager manager;

	@NotNull
	@Column(name = "Work_Name", nullable = false)
	private String workName;

	@NotNull
	@Column(name = "File_Name", nullable = false)
	private String fileName;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Contractor_ID")
	private Contractor contractor;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Production_ID")
	private ProductionDirectory production;

	@NotNull
	@Column(name = "Date_BEGIN", nullable = false)
	@Temporal(TemporalType.DATE)
	// @DateTimeFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBegin;

	@NotNull
	@Column(name = "Date_END", nullable = false)
	@Temporal(TemporalType.DATE)
	// @DateTimeFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateEnd;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Client_ID")
	private ClientDirectory client;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Stock_ID")
	private StockDirectory stock;

	@NotNull
	@Column(name = "Printing", nullable = false)
	private int printing;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Chromaticity_ID")
	private ChromaticityDirectory chromaticity;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Format_ID")
	private FormatDirectory format;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Laminate_ID")
	private LaminateDirectory laminate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Paper_ID")
	private PaperDirectory paper;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Cringle_ID")
	private CringleDirectory cringle;

	@Column(name = "Carving", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean carving;

	@Column(name = "Bending", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean bending;

	@Column(name = "assembly", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean assembly;

	@Column(name = "Cutting", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean cutting;

	@Column(name = "Note")
	@Size(max = 1000)
	private String note;

	@NotNull
	@Digits(integer = 8, fraction = 2)
	@Column(name = "Amount", nullable = false)
	private double amount;

	/*
	 * public Task() {
	 * 
	 * }
	 * 
	 * public Task(Task fromTask) { this. = copy(fromTask); }
	 */

	public Task getCopy() {

		System.out.println("Copy From task:" + this);
		Task task = new Task();

		task.manager = this.manager;
		task.workName = this.workName;
		task.fileName = this.fileName;
		task.contractor = this.contractor;
		task.production = this.production;
		task.dateBegin = this.dateBegin;
		task.dateEnd = this.dateEnd;
		task.client = this.client;
		task.stock = this.stock;
		task.printing = this.printing;
		task.chromaticity = this.chromaticity;
		task.format = this.format;
		task.laminate = this.laminate;
		task.paper = this.paper;
		task.cringle = this.cringle;
		task.carving = this.carving;
		task.bending = this.bending;
		task.assembly = this.assembly;
		task.cutting = this.cutting;
		task.note = this.note;
		task.amount = this.amount;

		System.out.println("COpy Return task:" + task);
		return task;
	}

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

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
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
		builder.append("Task [id=");
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
		builder.append(", dateBegin=");
		builder.append(dateBegin);
		builder.append(", dateEnd=");
		builder.append(dateEnd);
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
