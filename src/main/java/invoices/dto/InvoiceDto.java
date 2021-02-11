package invoices.dto;

import java.util.List;

public class InvoiceDto {

	private long invoiceId;
	private String customerName;
	private String issueDate;
	private String dueDate;
	private List<String> comment;
	private long totalInHuf;
	private float totalInEur;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public List<String> getComment() {
		return comment;
	}
	public void setComment(List<String> comment) {
		this.comment = comment;
	}
	public long getTotalInHuf() {
		return totalInHuf;
	}
	public void setTotalInHuf(long totalInHuf) {
		this.totalInHuf = totalInHuf;
	}
	public float getTotalInEur() {
		return totalInEur;
	}
	public void setTotalInEur(float f) {
		this.totalInEur = f;
	}
	public long getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	
}
