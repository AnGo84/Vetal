package ua.com.vetal.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.vetal.entity.attributeConverter.MoneyConverter;
import ua.com.vetal.entity.attributeConverter.ProductionAvailabilityConverter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "stencils")
@Data
@ToString(callSuper = true)
public class Stencil extends Order {

    @NotEmpty
    @Column(name = "Order_Name", nullable = false)
    private String orderName;

    @Column(name = "Adjustment")
    private String adjustment;

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

    @Column(name = "Cut_ribbon", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean cutRibbon;

    @Column(name = "Ribbon_length")
    private int ribbonLength;

    @NotNull
    @Column(name = "Sticker", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean sticker;

    @Column(name = "kraskoottisk")
    private int kraskoottisk;

    @Digits(integer = 8, fraction = 2)
    @Column(name = "cost_of_materials")
    @Convert(converter = MoneyConverter.class)
    private Double costOfMaterials;

    @Digits(integer = 8, fraction = 2)
    @Column(name = "cost_of_printing")
    @Convert(converter = MoneyConverter.class)
    private Double costOfPrinting;

    @NotNull
    @Column(name = "Production_availability_ID")
    @Convert(converter = ProductionAvailabilityConverter.class)
    private ProductionAvailability productionAvailability;

    public Stencil getCopy() {

        Stencil stencil = new Stencil();
        copyCommonFields(this, stencil);

        stencil.numberBase = this.numberBase;

        stencil.orderName = this.orderName;

        stencil.paperFormat = this.paperFormat;
        stencil.sheetNumber = this.sheetNumber;
        stencil.printer = this.printer;
        stencil.adjustment = this.adjustment;
        stencil.datePrintBegin = this.datePrintBegin;
        stencil.printingNote = this.printingNote;
        stencil.workerPrint = this.workerPrint;
        stencil.workerCut = this.workerCut;

        stencil.cutRibbon = this.cutRibbon;
        stencil.ribbonLength = this.ribbonLength;

        stencil.sticker = this.sticker;

        stencil.kraskoottisk = this.kraskoottisk;
        return stencil;
    }

}
