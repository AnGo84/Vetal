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

    @Column(name = "Cut_ribbon", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean cutRibbon;

    @Column(name = "Ribbon_length", nullable = true)
    private int ribbonLength;

    @NotNull
    @Column(name = "Sticker", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean sticker;

    @Column(name = "kraskoottisk")
    private int kraskoottisk;

    @Column(name = "cost_of_materials", nullable = true)
    @Convert(converter = MoneyConverter.class)
    private Double costOfMaterials;

    @Column(name = "cost_of_printing", nullable = true)
    @Convert(converter = MoneyConverter.class)
    private Double costOfPrinting;

    public Stencil getCopy() {

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
        return task;
    }

}
