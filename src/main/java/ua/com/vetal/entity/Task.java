package ua.com.vetal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	private Long account;

	private Manager manager;

	private String workName;

	private String fileName;

	private Contractor contractor;

	private ProductionDirectory production;

	private Date dateBegin;
	private Date dateEnd;

	private ClientDirectory client;
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

}
