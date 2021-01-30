package ua.com.vetal.entity;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.vetal.entity.attributeConverter.MoneyConverter;
import ua.com.vetal.entity.pk.OrderPK;

import javax.persistence.*;
import java.util.Date;

/**
 * orderType='task','stencil'
 */
@Entity
@Table(name = "vorders")
@Immutable
@IdClass(OrderPK.class)
@Data
public class StatisticOrder {
	@Id
	@Column(name = "order_id")
	private Long id;
	@Id
	@Column(name = "order_type")
	private String orderType;

	@Column(name = "Full_number")
	private String fullNumber;

	@Column(name = "Account")
	private String account;

	@Column(name = "Date_BEGIN")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBegin;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Manager_ID")
	private Manager manager;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Client_ID")
	private Client client;

	@ManyToOne(optional = false)
	@JoinColumn(name = "Production_ID")
	private ProductionDirectory production;

	@Column(name = "Printing")
	private int printing;

	//@Digits(integer = 8, fraction = 2)
	@Column(name = "Amount")
	@Convert(converter = MoneyConverter.class)
	private Double amount;

	//@Digits(integer = 8, fraction = 2)
	@Column(name = "Debt_amount")
	@Convert(converter = MoneyConverter.class)
	private Double debtAmount;

	public String getLinkPrefix() {
		return ("task".equals(orderType) ? "/tasks" : "/stencils");
	}
}
