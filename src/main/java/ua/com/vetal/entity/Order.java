package ua.com.vetal.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@ToString
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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	protected Date dateBegin;

	@NotNull
	@Column(name = "Date_END", nullable = false)
	@Temporal(TemporalType.DATE)
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

	@Digits(integer = 8, fraction = 2)
	@Column(name = "Amount")
	@Convert(converter = MoneyConverter.class)
	protected Double amount;

	@ManyToOne(optional = true)
	@JoinColumn(name = "State_ID")
	protected State state;

	@ManyToOne(optional = true)
	@JoinColumn(name = "Payment_ID")
	protected Payment payment;

	@Digits(integer = 8, fraction = 2)
	@Column(name = "Debt_amount")
	@Convert(converter = MoneyConverter.class)
	protected Double debtAmount;

	@Digits(integer = 8, fraction = 2)
	@Column(name = "other_expenses")
	@Convert(converter = MoneyConverter.class)
	protected Double otherExpenses;

	public String getStringAmount() {
		return new DecimalFormat("#,##0.00").format(amount) + " грн";
	}

	public String getNumberBaseWithSuffix() {
		return numberBase.getName() + " " + numberSuffix;
	}

	public String getPrintingWithUnit() {
		return printing + " " + printingUnit.getName();
	}

	public String constructFullNumber() {
		return this.number + "/" + this.numberBase.getName() + "-" + this.numberSuffix;
	}

	protected Order copyCommonFields(Order source, Order destination) {
		destination.manager = source.manager;
		destination.production = this.production;
		destination.dateBegin = this.dateBegin;
		destination.dateEnd = this.dateEnd;
		destination.client = this.client;
		destination.stock = this.stock;
		destination.printing = this.printing;
		destination.printingUnit = this.printingUnit;
		destination.paper = this.paper;

		destination.fillet = this.fillet;
		destination.popup = this.popup;
		destination.carving = this.carving;
		destination.stamping = this.stamping;
		destination.embossing = this.embossing;
		destination.bending = this.bending;
		destination.plotter = this.plotter;

		destination.packBox = this.packBox;
		destination.packPellicle = this.packPellicle;
		destination.packPaper = this.packPaper;
		destination.packPackage = this.packPackage;
		destination.packNP = this.packNP;
		destination.numeration = this.numeration;
		destination.numerationStart = this.numerationStart;

		destination.amount = this.amount;

		return destination;
	}


}
