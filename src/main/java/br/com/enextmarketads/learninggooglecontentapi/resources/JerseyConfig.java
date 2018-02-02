package br.com.enextmarketads.learninggooglecontentapi.resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(ProductsResource.class);
    }
}
