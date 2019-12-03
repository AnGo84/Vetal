package ua.com.vetal.entity;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DecimalFormat;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class ViewTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@Column(name = "Number", nullable = false)
	private int number;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Number_Base_ID")
	private NumberBaseDirectory numberBase;

	@NotNull
	@Column(name = "Number_Suffix", nullable = false)
	//@Size(max = 5)
	private int numberSuffix;

	@Column(name = "Full_number")
	private String fullNumber;

	@Column(name = "Account")
	@Size(max = 50)
	private String account;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Manager_ID")
	private Manager manager;

	@NotNull
	@Column(name = "Work_Name", nullable = false)
	private String workName;

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
	@JoinColumn(name = "Client_ID", nullable = false)
	private Client client;

	@NotNull
	@Digits(integer = 8, fraction = 2)
	@Column(name = "Amount", nullable = false)
	private double amount;

	@ManyToOne(optional = true)
	@JoinColumn(name = "State_ID")
	private State state;

	@ManyToOne(optional = true)
	@JoinColumn(name = "Payment_ID")
	private Payment payment;

	@Digits(integer = 8, fraction = 2)
	@Column(name = "Debt_amount", nullable = true)
	private double debtAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public NumberBaseDirectory getNumberBase() {
		return numberBase;
	}

	public void setNumberBase(NumberBaseDirectory numberBase) {
		this.numberBase = numberBase;
	}

	public int getNumberSuffix() {
		return numberSuffix;
	}

	public void setNumberSuffix(int numberSuffix) {
		this.numberSuffix = numberSuffix;
	}

	public String getFullNumber() {
		return fullNumber;
	}

	public void setFullNumber(String fullNumber) {
		this.fullNumber = fullNumber;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public double getDebtAmount() {
		return debtAmount;
	}

	public void setDebtAmount(double debtAmount) {
		this.debtAmount = debtAmount;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ViewTask{");
		sb.append("id=").append(id);
		sb.append(", number=").append(number);
		sb.append(", numberBase=").append(numberBase);
		sb.append(", numberSuffix=").append(numberSuffix);
		sb.append(", fullNumber='").append(fullNumber).append('\'');
		sb.append(", account='").append(account).append('\'');
		sb.append(", manager=").append(manager);
		sb.append(", workName='").append(workName).append('\'');
		sb.append(", fileName='").append(fileName).append('\'');
		sb.append(", contractor=").append(contractor);
		sb.append(", production=").append(production);
		sb.append(", dateBegin=").append(dateBegin);
		sb.append(", dateEnd=").append(dateEnd);
		sb.append(", client=").append(client);
		sb.append(", amount=").append(amount);
		sb.append(", state=").append(state);
		sb.append(", payment=").append(payment);
		sb.append(", debtAmount=").append(debtAmount);
		sb.append('}');
		return sb.toString();
	}
}
