package invoices.dto;

public class InvoiceItemDto {

	private String productName;
	private int unitPrice;
	private int quantity;
	private long invoiceId;
	private long totalInHuf;
	private float totalInEur;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public float getTotalInEur() {
		return totalInEur;
	}

	public void setTotalInEur(float totalInEur) {
		this.totalInEur = totalInEur;
	}

	public long getTotalInHuf() {
		return totalInHuf;
	}

	public void setTotalInHuf(long totalInHuf) {
		this.totalInHuf = totalInHuf;
	}
}
