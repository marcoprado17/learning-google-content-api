package br.com.enextmarketads.learninggooglecontentapi.resources.products;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

@Component	
@Path("products")
public class ProductsResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
        return "Got it!";
    }
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addOrUpdateProduct(Product product){
		System.out.println(product.title);
		return Response.status(200).build();
	}
}
