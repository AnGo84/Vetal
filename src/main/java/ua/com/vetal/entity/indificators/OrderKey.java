package ua.com.vetal.entity.indificators;

import java.io.Serializable;

public class OrderKey implements Serializable {
	private Long id;
	private String orderType;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof OrderKey)) return false;

		OrderKey orderKey = (OrderKey) o;

		if (id != null ? !id.equals(orderKey.id) : orderKey.id != null) return false;
		return orderType != null ? orderType.equals(orderKey.orderType) : orderKey.orderType == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (orderType != null ? orderType.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("OrderKey{");
		sb.append("id=").append(id);
		sb.append(", orderType='").append(orderType).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
