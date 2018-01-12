package br.com.enextmarketads.learninggooglecontentapi.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductsResourceTest {
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void exampleTest() {
		String body = this.restTemplate.getForObject("/products", String.class);
		assertThat(body).isEqualTo("Got it!");
	}
}
