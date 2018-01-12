package br.com.enextmarketads.learninggooglecontentapi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Component	
@Path("products")
public class ProductsResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
        return "Got it!";
    }
}
