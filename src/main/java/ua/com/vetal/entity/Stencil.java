package ua.com.vetal.entity;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.vetal.entity.attributeConverter.MoneyConverter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "stencils")
@Data
public class Stencil extends Order {

    /* @Id
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
	 private int numberSuffix;

	 @Column(name = "Full_number")
	 private String fullNumber;

	 @Column(name = "Account")
	 @Size(max = 50)
	 private String account;

	 @NotNull
	 @ManyToOne(optional = false)
	 @JoinColumn(name = "Client_ID")
	 private Client client;
 */
    @NotEmpty
    @Column(name = "Order_Name", nullable = false)
    private String orderName;

    /*@ManyToOne(optional = false)
    @JoinColumn(name = "Manager_ID")
    private Manager manager;*/

    /*@NotNull
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
    @JoinColumn(name = "Production_ID")
    private ProductionDirectory production;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "Stock_ID")
    private StockDirectory stock;

    @NotNull
    @Column(name = "Printing", nullable = false)
    private int printing;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "Printing_unit_id", nullable = false)
    private PrintingUnitDirectory printingUnit;

     */

    @Column(name = "Adjustment")
    private String adjustment;

	/*@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Paper_ID")
	private PaperDirectory paper;*/

    @NotNull
    @Column(name = "Paper_format", nullable = false)
    private String paperFormat;

    @Column(name = "Sheet_number", nullable = true)
    private int sheetNumber;

    @ManyToOne(optional = true)
    @JoinColumn(name = "Printer_ID")
    private Printer printer;

    @Column(name = "Date_print_BEGIN")
    @Temporal(TemporalType.DATE)
    // @DateTimeFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datePrintBegin;

    @Column(name = "Printing_Note")
    @Size(max = 1000)
    private String printingNote;

    @ManyToOne(optional = true)
    @JoinColumn(name = "Worker_print_ID")
    private Worker workerPrint;

    @ManyToOne(optional = true)
    @JoinColumn(name = "Worker_cut_ID")
    private Worker workerCut;
    /*
    @Column(name = "Fillet", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean fillet;

    @Column(name = "Popup", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean popup;

    @Column(name = "Carving", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean carving;

    @Column(name = "Stamping", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean stamping;

    @Column(name = "Embossing", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean embossing;

    @Column(name = "Bending", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean bending;

    @Column(name = "Plotter", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean plotter;

    @Column(name = "PackBox", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean packBox;

    @Column(name = "PackPellicle", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean packPellicle;

    @Column(name = "PackPaper", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean packPaper;

    @Column(name = "PackPackage", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean packPackage;

    @Column(name = "PackNP", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean packNP;

    @Column(name = "Numeration", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean numeration;

    @NotNull
    @Column(name = "Numeration_start")
    private int numerationStart;*/

    @Column(name = "Cut_ribbon", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean cutRibbon;

    @Column(name = "Ribbon_length", nullable = true)
    private int ribbonLength;

    @NotNull
    @Column(name = "Sticker", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean sticker;

    /*@NotNull
    @Digits(integer = 8, fraction = 2)
    @Column(name = "Amount", nullable = true)
    private double amount;

    @ManyToOne(optional = true)
    @JoinColumn(name = "State_ID")
    private State state;

    @ManyToOne(optional = true)
    @JoinColumn(name = "Payment_ID")
    private Payment payment;

    @Digits(integer = 8, fraction = 2)
    @Column(name = "Debt_amount", nullable = true)
    private double debtAmount;*/

    @Column(name = "kraskoottisk")
    private int kraskoottisk;

    //@Digits(integer = 8, fraction = 2)
    @Column(name = "cost_of_materials", nullable = true)
    @Convert(converter = MoneyConverter.class)
    private Double costOfMaterials;

    /*@Digits(integer = 8, fraction = 2)
    @Column(name = "other_expenses", nullable = true)
    private double otherExpenses;*/

    //@Digits(integer = 8, fraction = 2)
    @Column(name = "cost_of_printing", nullable = true)
    @Convert(converter = MoneyConverter.class)
    private Double costOfPrinting;

    public Stencil getCopy() {

        System.out.println("Copy From task:" + this);
        Stencil task = new Stencil();

        task.numberBase = this.numberBase;

        task.client = this.client;
        task.orderName = this.orderName;

        task.manager = this.manager;
        task.production = this.production;
        task.dateBegin = this.dateBegin;
        task.dateEnd = this.dateEnd;
        task.printingNote = this.printingNote;

        task.stock = this.stock;
        task.printing = this.printing;
        task.paper = this.paper;
        task.paperFormat = this.paperFormat;
        task.sheetNumber = this.sheetNumber;
        task.printer = this.printer;
        task.printingUnit = this.printingUnit;
        task.adjustment = this.adjustment;
        task.datePrintBegin = this.datePrintBegin;
        task.printingNote = this.printingNote;
        task.workerPrint = this.workerPrint;
        task.workerCut = this.workerCut;

        task.fillet = this.fillet;
        task.popup = this.popup;
        task.carving = this.carving;
        task.stamping = this.stamping;
        task.embossing = this.embossing;
        task.bending = this.bending;
        task.plotter = this.plotter;

        task.packBox = this.packBox;
        task.packPellicle = this.packPellicle;
        task.packPaper = this.packPaper;
        task.packPackage = this.packPackage;
        task.packNP = this.packNP;

        task.numeration = this.numeration;
        task.numerationStart = this.numerationStart;

        task.cutRibbon = this.cutRibbon;
        task.ribbonLength = this.ribbonLength;

        task.sticker = this.sticker;

        task.amount = this.amount;

        task.kraskoottisk = this.kraskoottisk;
        //System.out.println("Copy Return task:" + task);
        return task;
    }

    /*public Long getId() {
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

    public String getNumberBaseWithSuffix() {
        return numberBase.name + " " + numberSuffix;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
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

    public ProductionDirectory getProduction() {
        return production;
    }

    public void setProduction(ProductionDirectory production) {
        this.production = production;
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

    public PrintingUnitDirectory getPrintingUnit() {
        return printingUnit;
    }

    public void setPrintingUnit(PrintingUnitDirectory printingUnit) {
        this.printingUnit = printingUnit;
    }

    public String getPrintingWithUnit() {
        return printing + " " + printingUnit.name;
    }

    public String getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

    public PaperDirectory getPaper() {
        return paper;
    }

    public void setPaper(PaperDirectory paper) {
        this.paper = paper;
    }

    public String getPaperFormat() {
        return paperFormat;
    }

    public void setPaperFormat(String paperFormat) {
        this.paperFormat = paperFormat;
    }

    public int getSheetNumber() {
        return sheetNumber;
    }

    public void setSheetNumber(int sheetNumber) {
        this.sheetNumber = sheetNumber;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public Date getDatePrintBegin() {
        return datePrintBegin;
    }

    public void setDatePrintBegin(Date datePrintBegin) {
        this.datePrintBegin = datePrintBegin;
    }

    public String getPrintingNote() {
        return printingNote;
    }

    public void setPrintingNote(String printingNote) {
        this.printingNote = printingNote;
    }

    public Worker getWorkerPrint() {
        return workerPrint;
    }

    public void setWorkerPrint(Worker workerPrint) {
        this.workerPrint = workerPrint;
    }

    public Worker getWorkerCut() {
        return workerCut;
    }

    public void setWorkerCut(Worker workerCut) {
        this.workerCut = workerCut;
    }

    public boolean isFillet() {
        return fillet;
    }

    public void setFillet(boolean fillet) {
        this.fillet = fillet;
    }

    public boolean isPopup() {
        return popup;
    }

    public void setPopup(boolean popup) {
        this.popup = popup;
    }

    public boolean isCarving() {
        return carving;
    }

    public void setCarving(boolean carving) {
        this.carving = carving;
    }

    public boolean isStamping() {
        return stamping;
    }

    public void setStamping(boolean stamping) {
        this.stamping = stamping;
    }

    public boolean isEmbossing() {
        return embossing;
    }

    public void setEmbossing(boolean embossing) {
        this.embossing = embossing;
    }

    public boolean isBending() {
        return bending;
    }

    public void setBending(boolean bending) {
        this.bending = bending;
    }

    public boolean isPlotter() {
        return plotter;
    }

    public void setPlotter(boolean plotter) {
        this.plotter = plotter;
    }

    public boolean isPackBox() {
        return packBox;
    }

    public void setPackBox(boolean packBox) {
        this.packBox = packBox;
    }

    public boolean isPackPellicle() {
        return packPellicle;
    }

    public void setPackPellicle(boolean packPellicle) {
        this.packPellicle = packPellicle;
    }

    public boolean isPackPaper() {
        return packPaper;
    }

    public void setPackPaper(boolean packPaper) {
        this.packPaper = packPaper;
    }

    public boolean isPackPackage() {
        return packPackage;
    }

    public void setPackPackage(boolean packPackage) {
        this.packPackage = packPackage;
    }

    public boolean isPackNP() {
        return packNP;
    }

    public void setPackNP(boolean packNP) {
        this.packNP = packNP;
    }

    public boolean isNumeration() {
        return numeration;
    }

    public void setNumeration(boolean numeration) {
        this.numeration = numeration;
    }

    public int getNumerationStart() {
        return numerationStart;
    }

    public void setNumerationStart(int numerationStart) {
        this.numerationStart = numerationStart;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isSticker() {
        return sticker;
    }

    public void setSticker(boolean sticker) {
        this.sticker = sticker;
    }

    public boolean isCutRibbon() {
        return cutRibbon;
    }

    public void setCutRibbon(boolean cutRibbon) {
        this.cutRibbon = cutRibbon;
    }

    public int getRibbonLength() {
        return ribbonLength;
    }

    public void setRibbonLength(int ribbonLength) {
        this.ribbonLength = ribbonLength;
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

    public int getKraskoottisk() {
        return kraskoottisk;
    }

    public void setKraskoottisk(int kraskoottisk) {
        this.kraskoottisk = kraskoottisk;
    }

    public double getCostOfMaterials() {
        return costOfMaterials;
    }

    public void setCostOfMaterials(double costOfMaterials) {
        this.costOfMaterials = costOfMaterials;
    }

    public double getOtherExpenses() {
        return otherExpenses;
    }

    public void setOtherExpenses(double otherExpenses) {
        this.otherExpenses = otherExpenses;
    }

    public double getCostOfPrinting() {
        return costOfPrinting;
    }

    public void setCostOfPrinting(double costOfPrinting) {
        this.costOfPrinting = costOfPrinting;
    }*/

    /*public String getStringAmount() {
        return new DecimalFormat("#,##0.00").format(amount / 100) + " грн";
    }*/

}
