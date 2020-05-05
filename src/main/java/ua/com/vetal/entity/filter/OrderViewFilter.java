package ua.com.vetal.entity.filter;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.vetal.entity.*;
import ua.com.vetal.utils.DateUtils;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateBeginFrom;
	@Temporal(TemporalType.DATE)
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
        if (account != null && !account.equals("")) {
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
        } else if (dateBeginFrom != null) {
            return true;
        } else if (dateBeginTill != null) {
            return true;
        } else if (debtAmountFrom != null) {
            return true;
        } else return debtAmountTill != null;
    }

    @Override
    public ViewFilter getDefault() {
        OrderViewFilter orderViewFilter = new OrderViewFilter();
        orderViewFilter.setDateBeginFrom(DateUtils.firstDayOfMonth(DateUtils.addToDate(new Date(), 2, -1)));
        return orderViewFilter;
    }

    @Override
    public Predicate getPredicate(CriteriaBuilder builder, Root root) {
        Predicate predicate = builder.conjunction();
        if (this == null || !this.hasData()) {
            return predicate;
        }
        if (this.getAccount() != null && !this.getAccount().equals("")) {
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("account")),
                    ("%" + this.getAccount() + "%").toLowerCase()));
        }
        if (this.getNumber() != null && !this.getNumber().equals("")) {
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("fullNumber")),
                    ("%" + this.getNumber() + "%").toLowerCase()));
        }

        if (this.getFileName() != null && !this.getFileName().equals("")) {
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("fileName")),
                    ("%" + this.getFileName() + "%").toLowerCase()));
        }

        if (this.getClient() != null && this.getClient().getId() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("client"), this.getClient()));
        }

        if (this.getContractor() != null && this.getContractor().getId() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("contractor"), this.getContractor()));
        }

        if (this.getManager() != null && this.getManager().getId() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("manager"), this.getManager()));
        }

        if (this.getPrinter() != null && this.getPrinter().getId() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("printer"), this.getPrinter()));
        }

        if (this.getPaper() != null && this.getPaper().getId() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("paper"), this.getPaper()));
        }

        if (this.getProduction() != null && this.getProduction().getId() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("production"), this.getProduction()));
        }
        if (this.getDateBeginFrom() != null) {
            predicate = builder.and(predicate,
                    builder.greaterThanOrEqualTo(root.get("dateBegin"), this.getDateBeginFrom()));
        }
        if (this.getDateBeginTill() != null) {
            predicate = builder.and(predicate,
                    builder.lessThanOrEqualTo(root.get("dateBegin"), this.getDateBeginTill()));
        }
        if (this.getDebtAmountFrom() != null) {
            predicate = builder.and(predicate,
                    builder.greaterThanOrEqualTo(root.get("debtAmount"), this.getDebtAmountFrom()));
        }
        if (this.getDebtAmountTill() != null) {
            predicate = builder.and(predicate,
                    builder.lessThanOrEqualTo(root.get("debtAmount"), this.getDebtAmountTill()));
        }
        return predicate;
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
