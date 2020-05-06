package ua.com.vetal.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.vetal.entity.attributeConverter.MoneyConverter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DecimalFormat;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	protected Long id;

	@NotNull
	@Column(name = "Number", nullable = false)
	protected int number;


	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Number_Base_ID")
	protected NumberBaseDirectory numberBase;

	@NotNull
	@Column(name = "Number_Suffix", nullable = false)
	protected int numberSuffix;

	@Column(name = "Full_number")
	protected String fullNumber;

	@Column(name = "Account")
	@Size(max = 50)
	protected String account;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Client_ID")
	protected Client client;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Manager_ID")
	protected Manager manager;

	@NotNull
	@Column(name = "Date_BEGIN", nullable = false)
	@Temporal(TemporalType.DATE)
	// @DateTimeFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected Date dateBegin;

	@NotNull
	@Column(name = "Date_END", nullable = false)
	@Temporal(TemporalType.DATE)
	// @DateTimeFormat(pattern = "dd-MM-yyyy")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected Date dateEnd;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Production_ID")
	protected ProductionDirectory production;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Stock_ID")
	protected StockDirectory stock;

	@NotNull
	@Column(name = "Printing", nullable = false)
	protected int printing;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Printing_unit_id", nullable = false)
	protected PrintingUnitDirectory printingUnit;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Paper_ID")
	protected PaperDirectory paper;

	@Column(name = "Fillet", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean fillet;

	@Column(name = "Popup", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean popup;

	@Column(name = "Carving", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean carving;

	@Column(name = "Stamping", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean stamping;

	@Column(name = "Embossing", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean embossing;

	@Column(name = "Bending", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean bending;

	@Column(name = "Plotter", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean plotter;

	@Column(name = "PackBox", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean packBox;

	@Column(name = "PackPellicle", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean packPellicle;

	@Column(name = "PackPaper", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean packPaper;

	@Column(name = "PackPackage", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean packPackage;

	@Column(name = "PackNP", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean packNP;

	@Column(name = "Numeration", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	protected boolean numeration;

	@NotNull
	@Column(name = "Numeration_start")
	protected int numerationStart;

	//@NotNull
	//@Digits(integer = 8, fraction = 2)
	@Column(name = "Amount", nullable = true)
	@Convert(converter = MoneyConverter.class)
	protected Double amount;

	@ManyToOne(optional = true)
	@JoinColumn(name = "State_ID")
	protected State state;

	@ManyToOne(optional = true)
	@JoinColumn(name = "Payment_ID")
	protected Payment payment;

	@Digits(integer = 8, fraction = 2)
	@Column(name = "Debt_amount", nullable = true)
	@Convert(converter = MoneyConverter.class)
	protected Double debtAmount;
	
	@Digits(integer = 8, fraction = 2)
	@Column(name = "other_expenses", nullable = true)
	@Convert(converter = MoneyConverter.class)
	protected Double otherExpenses;

	/*public double getAmount() {
		return amount / 100;
	}

	public void setAmount(double amount) {
		this.amount = amount * 100;
	}*/

	public String getStringAmount() {
		return new DecimalFormat("#,##0.00").format(amount) + " грн";
		//return new DecimalFormat("#,##0.00").format(amount / 100) + " грн";
	}

	public String getNumberBaseWithSuffix() {
		return numberBase.name + " " + numberSuffix;
	}

	public String getPrintingWithUnit() {
		return printing + " " + printingUnit.name;
	}
}
