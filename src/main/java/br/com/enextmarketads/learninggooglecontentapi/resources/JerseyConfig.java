package br.com.enextmarketads.learninggooglecontentapi.resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import br.com.enextmarketads.learninggooglecontentapi.resources.products.ProductsResource;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(ProductsResource.class);
    }
}
