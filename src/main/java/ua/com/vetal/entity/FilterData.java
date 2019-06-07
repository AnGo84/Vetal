package ua.com.vetal.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class FilterData {

    private Long id;

    private String account;
    private String number;
    private Manager manager;
    private String workName;
    private String fileName;
    private Contractor contractor;
    private ProductionDirectory production;
    private Printer printer;

    // @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBeginFrom;

    // @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBeginTill;

    private Client client;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
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

    public boolean hasData() {
        if (account != null) {
            return true;
        } else if (number != null && !number.equals("")) {
            return true;
        } else if (manager != null) {
            return true;
        } else if (paper != null) {
            return true;
        } else if (production != null) {
            return true;
        } else if (client != null) {
            return true;
        } else if (printer != null) {
            return true;
        } else if (contractor != null) {
            return true;
        } else if (workName != null && !workName.equals("")) {
            return true;
        } else if (fileName != null && !fileName.equals("")) {
            return true;
        } else if (number != null && !number.equals("")) {
            return true;
        } else if (dateBeginFrom != null) {
            return true;
        } else if (dateBeginTill != null) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FilterData{");
        sb.append("id=").append(id);
        sb.append(", account='").append(account).append('\'');
        sb.append(", number='").append(number).append('\'');
        sb.append(", manager=").append(manager);
        sb.append(", workName='").append(workName).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", contractor=").append(contractor);
        sb.append(", production=").append(production);
        sb.append(", printer=").append(printer);
        sb.append(", dateBeginFrom=").append(dateBeginFrom);
        sb.append(", dateBeginTill=").append(dateBeginTill);
        sb.append(", client=").append(client);
        sb.append(", stock=").append(stock);
        sb.append(", printing=").append(printing);
        sb.append(", chromaticity=").append(chromaticity);
        sb.append(", format=").append(format);
        sb.append(", laminate=").append(laminate);
        sb.append(", paper=").append(paper);
        sb.append(", cringle=").append(cringle);
        sb.append(", carving=").append(carving);
        sb.append(", bending=").append(bending);
        sb.append(", assembly=").append(assembly);
        sb.append(", cutting=").append(cutting);
        sb.append(", note='").append(note).append('\'');
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
