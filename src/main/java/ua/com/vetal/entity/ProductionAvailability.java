package ua.com.vetal.entity;


public enum ProductionAvailability {
	IN_STOCK(1l),
	ORDERED(2l),
	NEED_ORDERED(3l),
	OTHER(4l);

	ProductionAvailability(Long dbIndex) {
		this.dbIndex = dbIndex;
	}

	private Long dbIndex;

	public Long getDbIndex() {
		return dbIndex;
	}
}
