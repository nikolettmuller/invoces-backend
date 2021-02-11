package invoices.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import invoices.dto.InvoiceDto;
import invoices.ejb.InvoiceEJB;

@Path("/invoice")
public class InvoiceService {

	@Inject
	InvoiceEJB ejb;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<InvoiceDto> getAll(){
		return ejb.findAll();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public InvoiceDto getOne(@PathParam("id") long id) {
		return ejb.findOne(id);
	}
	
	@POST
	public boolean create(InvoiceDto invoice) {
		return ejb.create(invoice);
	}
}
