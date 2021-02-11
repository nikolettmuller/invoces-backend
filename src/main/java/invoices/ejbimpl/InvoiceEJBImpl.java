package invoices.ejbimpl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import invoices.dto.InvoiceDto;
import invoices.ejb.InvoiceEJB;
import invoices.entity.Comment;
import invoices.entity.Invoice;
import invoices.entity.InvoiceItem;
import invoices.generated.MNBArfolyamServiceSoap;
import invoices.generated.MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage;
import invoices.generated.MNBArfolyamServiceSoapImpl;

@Stateless
public class InvoiceEJBImpl implements InvoiceEJB {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<InvoiceDto> findAll() {
		Query q = em.createQuery("SELECT i FROM Invoice i");
		List<Invoice> invoices = q.getResultList();
		List<InvoiceDto> invoicesDto = new ArrayList<>();
		for (int i = 0; i < invoices.size(); i++) {

			InvoiceDto invoiceDto = new InvoiceDto();
			invoiceDto.setInvoiceId(invoices.get(i).getinvoiceId());
			invoiceDto.setCustomerName(invoices.get(i).getCustomerName());
			invoiceDto.setIssueDate(invoices.get(i).getIssueDate());
			invoiceDto.setDueDate(invoices.get(i).getDueDate());
			List<String> comments = new ArrayList<>();
			for (Comment comment : invoices.get(i).getComments()) {
				comments.add(comment.getComment());
			}
			invoiceDto.setComment(comments);

			List<InvoiceItem> items = invoices.get(i).getInvoiceItems();
			long sum = 0;
			for (InvoiceItem item : items) {
				sum += item.getQuantity() * item.getUnitPrice();
			}
			invoiceDto.setTotalInHuf(sum);

			try {
				invoiceDto.setTotalInEur(getPriceInEUR(sum));
			} catch (MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage e) {

				e.printStackTrace();
			}

			invoicesDto.add(invoiceDto);
		}

		return invoicesDto;
	}

	@Override
	public InvoiceDto findOne(long id) {
		Invoice invoice = em.find(Invoice.class, id);
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setInvoiceId(invoice.getinvoiceId());
		invoiceDto.setCustomerName(invoice.getCustomerName());
		invoiceDto.setIssueDate(invoice.getIssueDate());
		invoiceDto.setDueDate(invoice.getDueDate());
		List<String> comments = new ArrayList<>();
		for (Comment comment : invoice.getComments()) {
			comments.add(comment.getComment());
		}
		invoiceDto.setComment(comments);
		List<InvoiceItem> items = invoice.getInvoiceItems();
		long sum = 0;
		for (InvoiceItem item : items) {
			sum += item.getQuantity() * item.getUnitPrice();

		}
		invoiceDto.setTotalInHuf(sum);

		try {
			invoiceDto.setTotalInEur(getPriceInEUR(sum));
		} catch (MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage e) {

			e.printStackTrace();
		}

		return invoiceDto;
	}

	@Override
	public boolean create(InvoiceDto invoiceDto) {
		Invoice invoice = new Invoice();
		invoice.setCustomerName(invoiceDto.getCustomerName());
		invoice.setIssueDate(invoiceDto.getIssueDate());
		invoice.setDueDate(invoiceDto.getDueDate());
		invoice.setComments(new ArrayList<Comment>());
		invoice.setInvoiceItems(new ArrayList<InvoiceItem>());
		em.persist(invoice);
		return true;
	}

	public float getPriceInEUR(long priceInHuf)
			throws MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage {

		MNBArfolyamServiceSoapImpl mnb = new MNBArfolyamServiceSoapImpl();
		MNBArfolyamServiceSoap service = mnb.getCustomBindingMNBArfolyamServiceSoap();

		String xmlRecords = service.getCurrentExchangeRates();

		float Eur = priceInHuf / getEur(xmlRecords);
		
		return Eur;
	}

	private float getEur(String str) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(str)));
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("Rate");
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement.getAttribute("curr").equals("EUR")) {
						return Float.parseFloat(eElement.getTextContent().replaceAll(",", "."));
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
