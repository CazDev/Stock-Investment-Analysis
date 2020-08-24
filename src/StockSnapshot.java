public class StockSnapshot {
	private String date;
	private Float value;
	private String stock;
	
	public StockSnapshot(String date, Float value, String stock) {
		this.date = date;
		this.value = value;
		this.stock = stock;
	}
	
	//Getters

	public String getDate() {
		return date;
	}

	public Float getValue() {
		return value;
	}

	public String getStock() {
		return stock;
	}
	
}
