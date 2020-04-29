package ua.com.vetal.entity;

import org.hibernate.annotations.Immutable;
import org.springframework.format.annotation.DateTimeFormat;
import ua.com.vetal.entity.pk.OrderPK;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.util.Date;

/**
 * orderType='task','stencil'
 */
@Entity
@Table(name = "vorders")
@Immutable
@IdClass(OrderPK.class)
public class Order {
	@Id
	@Column(name = "order_id")
	private Long id;
	@Id
	@Column(name = "order_type")
	private String orderType;

	@Column(name = "Full_number")
	private String fullNumber;

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

	@Digits(integer = 8, fraction = 2)
	@Column(name = "Amount")
	private double amount;

	@Digits(integer = 8, fraction = 2)
	@Column(name = "Debt_amount")
	private double debtAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getFullNumber() {
		return fullNumber;
	}

	public void setFullNumber(String fullNumber) {
		this.fullNumber = fullNumber;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ProductionDirectory getProduction() {
		return production;
	}

	public void setProduction(ProductionDirectory production) {
		this.production = production;
	}

	public int getPrinting() {
		return printing;
	}

	public void setPrinting(int printing) {
		this.printing = printing;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getDebtAmount() {
		return debtAmount;
	}

	public void setDebtAmount(double debtAmount) {
		this.debtAmount = debtAmount;
	}

	public String getLinkPrefix() {
		return ("task".equals(orderType) ? "/tasks" : "/stencils");
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Order{");
		sb.append("id=").append(id);
		sb.append(", orderType='").append(orderType).append('\'');
		sb.append(", number=").append(fullNumber);
		sb.append(", dateBegin=").append(dateBegin);
		sb.append(", manager=").append(manager);
		sb.append(", client=").append(client);
		sb.append(", production=").append(production);
		sb.append(", printing=").append(printing);
		sb.append(", amount=").append(amount);
		sb.append(", debtAmount=").append(debtAmount);
		sb.append('}');
		return sb.toString();
	}
}
