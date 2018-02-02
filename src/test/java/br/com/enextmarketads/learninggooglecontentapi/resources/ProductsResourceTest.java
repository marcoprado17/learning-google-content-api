package br.com.enextmarketads.learninggooglecontentapi.resources;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import com.google.api.services.content.model.Price;
import com.google.api.services.content.model.Product;
import com.google.api.services.content.model.ProductShipping;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductsResourceTest {
	private static final String WEB_SITE_URL = "http://vps0048.publiccloud.com.br";
	private static final String CHANNEL = "online";
	private static final String CONTENT_LANGUAGE = "en";
	private static final String TARGET_COUNTRY = "GB";
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void exampleTest() {
		String body = this.restTemplate.getForObject("/products", String.class);
		assertThat(body).isEqualTo("Got it!");
	}
	
	@Test
	public void exampleTest2() {
		Product product = new Product();
		
		product.setOfferId("book" + (new Random()).nextInt(10000));
	    product.setTitle("A Tale of Two Cities");
	    product.setDescription("A classic novel about the French Revolution");
	    product.setLink(WEB_SITE_URL + "/tale-of-two-cities.html");
	    product.setImageLink(WEB_SITE_URL + "/tale-of-two-cities.jpg");
	    product.setChannel(CHANNEL);
	    product.setContentLanguage(CONTENT_LANGUAGE);
	    product.setTargetCountry(TARGET_COUNTRY);
	    product.setAvailability("in stock");
	    product.setCondition("new");
	    product.setGoogleProductCategory("Media > Books");
	    product.setGtin("9780007350896");

	    Price price = new Price();
	    price.setValue("2.50");
	    price.setCurrency("GBP");
	    product.setPrice(price);

	    Price shippingPrice = new Price();
	    shippingPrice.setValue("0.99");
	    shippingPrice.setCurrency("GBP");

	    ProductShipping shipping = new ProductShipping();
	    shipping.setPrice(shippingPrice);
	    shipping.setCountry("GB");
	    shipping.setService("1st class post");

	    ArrayList<ProductShipping> shippingList = new ArrayList<ProductShipping>();
	    shippingList.add(shipping);
	    product.setShipping(shippingList);
		
		try{
			this.restTemplate.put("/products", product);
		}
		catch(RestClientException e){
			
		}
	}
}
