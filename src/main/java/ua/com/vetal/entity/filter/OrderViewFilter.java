package ua.com.vetal.entity.filter;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.vetal.entity.*;
import ua.com.vetal.utils.DateUtils;

import java.util.Date;

@Data
public class OrderViewFilter implements ViewFilter {

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
    private Double debtAmount;
    private Double debtAmountFrom;
    private Double debtAmountTill;

    @Override
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
        } else if (debtAmountFrom != null) {
            return true;
        } else if (debtAmountTill != null) {
            return true;
        }
        return false;
    }

    @Override
    public ViewFilter getDefault() {
        OrderViewFilter orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(DateUtils.firstDayOfMonth(new Date()));
        return orderViewFilter;
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
        sb.append(", debtAmount=").append(debtAmount);
        sb.append(", debtAmountFrom=").append(debtAmountFrom);
        sb.append(", debtAmountTill=").append(debtAmountTill);
        sb.append('}');
        return sb.toString();
    }
}
