package ua.com.vetal.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;
import ua.com.vetal.entity.attributeConverter.MoneyConverter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tasks")
@Data
@ToString(callSuper = true)
public class Task extends Order {

    @NotEmpty
    @Column(name = "Work_Name", nullable = false)
    private String workName;

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

	@Column(name = "Amount_for_Contractor", nullable = true)
	@Convert(converter = MoneyConverter.class)
	private Double amountForContractor;

	@Column(name = "Contractor_Amount", nullable = true)
	@Convert(converter = MoneyConverter.class)
	private Double contractorAmount;

	@Column(name = "provider")
	private String provider;

	@Column(name = "provider_cost", nullable = true)
	@Convert(converter = MoneyConverter.class)
	private Double providerCost;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Production_Type_ID")
	private ProductionTypeDirectory productionType;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Chromaticity_ID", nullable = false)
	private ChromaticityDirectory chromaticity;

	@ManyToOne()
	@JoinColumn(name = "Format_ID")
	private FormatDirectory format;

	@NotNull
	@Column(name = "Printing_format")
	private String printingFormat;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Laminate_ID")
	private LaminateDirectory laminate;

	@Column(name = "Wares")
	private String wares;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Cringle_ID")
	private CringleDirectory cringle;

	@Column(name = "assembly", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean assembly;

	@Column(name = "Cutting", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean cutting;

	@Column(name = "Note")
	@Size(max = 1000)
	private String note;

	@Column(name = "PackBy")
	private String packBy;

	public Task getCopy() {

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

		return task;
	}

	public String getDBFileName() {
		if (dbFile == null || dbFile.getFileName() == null) {
			return "";
		}
		return dbFile.getFileName();
	}

}
