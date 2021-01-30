package ua.com.vetal.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "tasks")
@Data
public class ViewTask {

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
	private int numberSuffix;

	@Column(name = "Full_number")
    private String fullNumber;

    @Column(name = "Account")
    @Size(max = 50)
    private String account;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Manager_ID")
    private Manager manager;

    @NotEmpty
    @Column(name = "Work_Name", nullable = false)
    private String workName;

    @NotEmpty
    @Column(name = "File_Name", nullable = false)
    private String fileName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Contractor_ID")
    private Contractor contractor;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "Production_ID")
	private ProductionDirectory production;

	@NotNull
	@Column(name = "Date_BEGIN", nullable = false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBegin;

	@NotNull
	@Column(name = "Date_END", nullable = false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateEnd;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "Client_ID", nullable = false)
	private Client client;

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
}
