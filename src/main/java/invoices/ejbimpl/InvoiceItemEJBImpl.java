package invoices.ejbimpl;

import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import invoices.dto.InvoiceItemDto;
import invoices.ejb.InvoiceItemEJB;
import invoices.entity.Invoice;
import invoices.entity.InvoiceItem;
import invoices.generated.MNBArfolyamServiceSoap;
import invoices.generated.MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage;
import invoices.generated.MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage;
import invoices.generated.MNBArfolyamServiceSoapImpl;

@Stateless
public class InvoiceItemEJBImpl implements InvoiceItemEJB{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public List<InvoiceItemDto> findAll(long id){
		Query q = em.createQuery("SELECT i FROM InvoiceItem i");
		List<InvoiceItem> invoiceItems = q.getResultList();
		List<InvoiceItemDto> filteredItems = new ArrayList<InvoiceItemDto>();
		for(InvoiceItem invoiceItem : invoiceItems) {
			long sum = 0;
			if(invoiceItem.getInvoice().getinvoiceId() == id) {
				InvoiceItemDto dto = new InvoiceItemDto();
				dto.setInvoiceId(invoiceItem.getInvoice().getinvoiceId());
				dto.setProductName(invoiceItem.getProductName());
				dto.setUnitPrice(invoiceItem.getUnitPrice());
				dto.setQuantity(invoiceItem.getQuantity());
				sum = dto.getUnitPrice() * dto.getQuantity();
				dto.setTotalInHuf(sum);
				try {
					dto.setTotalInEur(getPriceInEUR(sum));
				} catch (MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage e) {
					e.printStackTrace();
				}
				filteredItems.add(dto);
			}
		}
		return filteredItems;
	}
	
	@Override
	public boolean create(InvoiceItemDto invoiceItemDto) {
		InvoiceItem invoiceItem = new InvoiceItem();
		invoiceItem.setProductName(invoiceItemDto.getProductName());
		invoiceItem.setQuantity(invoiceItemDto.getQuantity());
		invoiceItem.setUnitPrice(invoiceItemDto.getUnitPrice());
		invoiceItem.setInvoice(getInvoiceById(invoiceItemDto.getInvoiceId()));
		em.persist(invoiceItem);
		return true;
	}
	
	private Invoice getInvoiceById(long id) {
		return em.find(Invoice.class, id);
	}
	
	public float getPriceInEUR(long priceInHuf) throws MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage {

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
