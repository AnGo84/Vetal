package ua.com.vetal.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;
import ua.com.vetal.entity.attributeConverter.MoneyConverter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_id")
	private DBFile dbFile;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Contractor_ID")
	private Contractor contractor;

	@Column(name = "Contractor_number")
	private String contractorNumber;

	@Digits(integer = 8, fraction = 2)
	@Column(name = "Amount_for_Contractor")
	@Convert(converter = MoneyConverter.class)
	private Double amountForContractor;

	@Digits(integer = 8, fraction = 2)
	@Column(name = "Contractor_Amount")
	@Convert(converter = MoneyConverter.class)
	private Double contractorAmount;

	@Column(name = "provider")
	private String provider;

	@Digits(integer = 8, fraction = 2)
	@Column(name = "provider_cost")
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
		copyCommonFields(this, task);

		task.workName = this.workName;
		task.fileName = this.fileName;
		task.contractor = this.contractor;
		task.productionType = this.productionType;
		task.chromaticity = this.chromaticity;
		task.format = this.format;
		task.printingFormat = this.printingFormat;
		task.laminate = this.laminate;
		task.wares = this.wares;

		task.cringle = this.cringle;

		task.assembly = this.assembly;
		task.cutting = this.cutting;
		task.note = this.note;

		task.packBy = this.packBy;

		return task;
	}

	public String getDBFileName() {
		if (dbFile == null || dbFile.getFileName() == null) {
			return "";
		}
		return dbFile.getFileName();
	}

}
