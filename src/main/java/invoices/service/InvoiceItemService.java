package invoices.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import invoices.dto.InvoiceItemDto;
import invoices.ejb.InvoiceItemEJB;
import invoices.entity.InvoiceItem;

@Path("/item")
public class InvoiceItemService {

	@Inject
	InvoiceItemEJB ejb;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<InvoiceItemDto> getAll(@PathParam("id") long id){
		return ejb.findAll(id);
	}
	
	@POST
	public boolean create(InvoiceItemDto InvoiceItemDto) {
		return ejb.create(InvoiceItemDto);
	}
}
