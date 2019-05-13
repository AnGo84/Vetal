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
public class Task {

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

    //@NotNull
    //@Column(name = "Account", nullable = false, unique = true)
    @Column(name = "Account")
    @Size(max = 50)
    private String account;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Manager_ID")
    private Manager manager;

    @NotNull
    @Column(name = "Work_Name", nullable = false)
    private String workName;

    //@NotNull
    @Column(name = "File_Name", nullable = false)
    private String fileName;

    @OneToOne
    @JoinColumn(name = "file_id")
    private DBFile dbFile;


    @ManyToOne(optional = false)
    @JoinColumn(name = "Contractor_ID")
    private Contractor contractor;

    @Column(name = "Contractor_number")
    private String contractorNumber;

    @Digits(integer = 8, fraction = 2)
    @Column(name = "Amount_for_Contractor", nullable = true)
    private double amountForContractor;

    @Digits(integer = 8, fraction = 2)
    @Column(name = "Contractor_Amount", nullable = true)
    private double contractorAmount;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "Production_ID")
    private ProductionDirectory production;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "Production_Type_ID")
    private ProductionTypeDirectory productionType;

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
    private ClientDirectory client;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "Stock_ID", nullable = false)
    private StockDirectory stock;

    @NotNull
    @Column(name = "Printing", nullable = false)
    private int printing;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "Printing_unit_id", nullable = false)
    private PrintingUnitDirectory printingUnit;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "Chromaticity_ID", nullable = false)
    private ChromaticityDirectory chromaticity;

    //@NotNull
    @ManyToOne()
    @JoinColumn(name = "Format_ID")
    private FormatDirectory format;

    @NotNull
    @Column(name = "Printing_format")
    private String printingFormat;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Laminate_ID")
    private LaminateDirectory laminate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Paper_ID")
    private PaperDirectory paper;

    @Column(name = "Wares")
    private String wares;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Cringle_ID")
    private CringleDirectory cringle;

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

    @Column(name = "assembly", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean assembly;

    @Column(name = "Cutting", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean cutting;

    @Column(name = "Note")
    @Size(max = 1000)
    private String note;

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

    @Column(name = "PackBy")
    private String packBy;

    @Column(name = "Numeration", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean numeration;

    @NotNull
    @Column(name = "Numeration_start")
    private int numerationStart;

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

    public Task getCopy() {

        //System.out.println("Copy From task:" + this);
        Task task = new Task();

        task.manager = this.manager;
        task.workName = this.workName;
        task.fileName = this.fileName;
        task.contractor = this.contractor;
        task.production = this.production;
        task.productionType = this.productionType;
        task.dateBegin = this.dateBegin;
        task.dateEnd = this.dateEnd;
        task.client = this.client;
        task.stock = this.stock;
        task.printing = this.printing;
        task.printingUnit = this.printingUnit;
        task.chromaticity = this.chromaticity;
        task.format = this.format;
        task.printingFormat = this.printingFormat;
        task.laminate = this.laminate;
        task.paper = this.paper;
        task.wares = this.wares;

        task.cringle = this.cringle;

        task.fillet = this.fillet;
        task.popup = this.popup;
        task.carving = this.carving;
        task.stamping = this.stamping;
        task.embossing = this.embossing;
        task.bending = this.bending;
        task.plotter = this.plotter;

        task.assembly = this.assembly;
        task.cutting = this.cutting;
        task.note = this.note;

        task.packBox = this.packBox;
        task.packPellicle = this.packPellicle;
        task.packPaper = this.packPaper;
        task.packPackage = this.packPackage;
        task.packNP = this.packNP;
        task.packBy = this.packBy;

        task.numeration = this.numeration;
        task.numerationStart = this.numerationStart;

        task.amount = this.amount;

        System.out.println("Copy Return task:" + task);
        return task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    /*public String getFileName() {
        if(dbFile==null){
            return "";
        }
        return dbFile.getFileName();
    }
*/
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DBFile getDbFile() {
        return dbFile;
    }

    public void setDbFile(DBFile dbFile) {
        this.dbFile = dbFile;
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

    public ProductionTypeDirectory getProductionType() {
        return productionType;
    }

    public void setProductionType(ProductionTypeDirectory productionType) {
        this.productionType = productionType;
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

    public PrintingUnitDirectory getPrintingUnit() {
        return printingUnit;
    }

    public void setPrintingUnit(PrintingUnitDirectory printingUnit) {
        this.printingUnit = printingUnit;
    }

    public String getPrintingWithUnit() {
        return printing + " " + printingUnit.name;
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

    public String getPrintingFormat() {
        return printingFormat;
    }

    public void setPrintingFormat(String printingFormat) {
        this.printingFormat = printingFormat;
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

    public boolean isPlotter() {
        return plotter;
    }

    public void setPlotter(boolean plotter) {
        this.plotter = plotter;
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

    public String getWares() {
        return wares;
    }

    public void setWares(String wares) {
        this.wares = wares;
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

    public String getPackBy() {
        return packBy;
    }

    public void setPackBy(String packBy) {
        this.packBy = packBy;
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
        return amount / 100;
    }

    public void setAmount(double amount) {
        this.amount = amount * 100;
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

    public String getContractorNumber() {
        return contractorNumber;
    }

    public void setContractorNumber(String contractorNumber) {
        this.contractorNumber = contractorNumber;
    }

    public double getAmountForContractor() {
        return amountForContractor;
    }

    public void setAmountForContractor(double amountForContractor) {
        this.amountForContractor = amountForContractor;
    }

    public double getContractorAmount() {
        return contractorAmount;
    }

    public void setContractorAmount(double contractorAmount) {
        this.contractorAmount = contractorAmount;
    }

    public String getStringAmount() {
        return new DecimalFormat("#,##0.00").format(amount / 100) + " грн";
    }

    public String getDBFileName() {
        if (dbFile == null || dbFile.getFileName() == null) {
            return "";
        }
        //return dbFile.toShortString();
        //return dbFile.getFullFileName();
        return dbFile.getFileName();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("id=").append(id);
        sb.append(", number=").append(number);
        sb.append(", numberBase=").append(numberBase);
        sb.append(", numberSuffix=").append(numberSuffix);
        sb.append(", fullNumber='").append(fullNumber).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", manager=").append(manager);
        sb.append(", workName='").append(workName).append('\'');
        //sb.append(", dbFile='").append(dbFile.toShortString()).append('\'');
        sb.append(", dbFile='").append(getDBFileName()).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", contractor=").append(contractor);
        sb.append(", contractorNumber='").append(contractorNumber).append('\'');
        sb.append(", amountForContractor=").append(amountForContractor);
        sb.append(", сontractorAmount=").append(contractorAmount);
        sb.append(", production=").append(production);
        sb.append(", productionType=").append(productionType);
        sb.append(", dateBegin=").append(dateBegin);
        sb.append(", dateEnd=").append(dateEnd);
        sb.append(", client=").append(client);
        sb.append(", stock=").append(stock);
        sb.append(", printing=").append(printing);
        sb.append(", printingUnit=").append(printingUnit);
        sb.append(", chromaticity=").append(chromaticity);
        sb.append(", format=").append(format);
        sb.append(", printingFormat=").append(printingFormat);
        sb.append(", laminate=").append(laminate);
        sb.append(", paper=").append(paper);
        sb.append(", wares='").append(wares).append('\'');
        sb.append(", cringle=").append(cringle);
        sb.append(", fillet=").append(fillet);
        sb.append(", popup=").append(popup);
        sb.append(", carving=").append(carving);
        sb.append(", stamping=").append(stamping);
        sb.append(", embossing=").append(embossing);
        sb.append(", bending=").append(bending);
        sb.append(", plotter=").append(plotter);
        sb.append(", assembly=").append(assembly);
        sb.append(", cutting=").append(cutting);
        sb.append(", note='").append(note).append('\'');
        sb.append(", packBox=").append(packBox);
        sb.append(", packPellicle=").append(packPellicle);
        sb.append(", packPaper=").append(packPaper);
        sb.append(", packPackage=").append(packPackage);
        sb.append(", packNP=").append(packNP);
        sb.append(", packBy='").append(packBy).append('\'');
        sb.append(", numeration=").append(numeration);
        sb.append(", numerationStart=").append(numerationStart);
        sb.append(", amount=").append(amount);
        sb.append(", state=").append(state);
        sb.append(", payment=").append(payment);
        sb.append(", debtAmount=").append(debtAmount);
        sb.append('}');
        return sb.toString();
    }
}
