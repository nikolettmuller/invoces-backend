package invoices.ejb;

import java.util.List;

import invoices.dto.InvoiceDto;

public interface InvoiceEJB {

	public List<InvoiceDto> findAll();
	public InvoiceDto findOne(long id);
	public boolean create(InvoiceDto invoice);
}
