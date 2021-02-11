package invoices.ejb;

import java.util.List;

import invoices.dto.InvoiceItemDto;
import invoices.entity.InvoiceItem;

public interface InvoiceItemEJB {

	public List<InvoiceItemDto> findAll(long id);
	public boolean create(InvoiceItemDto invoice);
}
