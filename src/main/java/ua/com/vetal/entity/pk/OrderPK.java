package ua.com.vetal.entity.pk;

import java.io.Serializable;

public class OrderPK implements Serializable {
	private Long id;
	private String orderType;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof OrderPK)) return false;

		OrderPK orderPK = (OrderPK) o;

		if (id != null ? !id.equals(orderPK.id) : orderPK.id != null) return false;
		return orderType != null ? orderType.equals(orderPK.orderType) : orderPK.orderType == null;
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
