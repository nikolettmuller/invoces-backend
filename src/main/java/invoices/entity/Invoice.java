package invoices.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Invoice implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long invoiceId;
	
	@NotNull
	private String customerName;

	private String issueDate;
	
	private String dueDate;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(targetEntity = Comment.class, mappedBy = "invoice")
	private List<Comment> comments;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(targetEntity = InvoiceItem.class, mappedBy = "invoice")
	private List<InvoiceItem> invoiceItems;

	@PrePersist
	protected void setDates() {
		Date currentDate = new Date();
		issueDate = new SimpleDateFormat("yyyy.MM.dd.").format(currentDate);
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, 15);
		currentDate = c.getTime();
		
		dueDate = new SimpleDateFormat("yyyy.MM.dd.").format(currentDate);
	}

	public long getinvoiceId() {
		return invoiceId;
	}

	public void setinvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

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

	public List<InvoiceItem> getInvoiceItems() {
		return invoiceItems;
	}

	public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
