package br.com.enextmarketads.learninggooglecontentapi.resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.content.ShoppingContentScopes;
import com.google.api.services.content.ShoppingContent;
import com.google.api.services.content.model.Price;
import com.google.api.services.content.model.Product;
import com.google.api.services.content.model.ProductShipping;
import com.google.api.services.content.model.ProductsCustomBatchRequest;
import com.google.api.services.content.model.ProductsCustomBatchRequestEntry;
import com.google.api.services.content.model.ProductsCustomBatchResponse;
import com.google.api.services.content.model.ProductsListResponse;

@Component
@Path("products")
public class ProductsResource {

	private static final BigInteger MERCHANT_ID = new BigInteger("118627540");
	private static final String WEB_SITE_URL = "http://www.enext.com.br";
	private static final String CHANNEL = "online";
	private static final String CONTENT_LANGUAGE = "en";
	private static final String TARGET_COUNTRY = "GB";

	private ShoppingContent getService() throws IOException,
			GeneralSecurityException {
		GoogleCredential credential = GoogleCredential.fromStream(
				new FileInputStream("service-account.json")).createScoped(
				Collections.singleton(ShoppingContentScopes.CONTENT));

		HttpTransport httpTransport = GoogleNetHttpTransport
				.newTrustedTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		ShoppingContent service = new ShoppingContent.Builder(httpTransport,
				jsonFactory, credential).setApplicationName(
				"YOUR_APPLICATION_NAME").build();

		return service;
	}

	private Product getProduct() {
		Product product = new Product();

		Random rand = new Random();
		String offerId = "book" + rand.nextInt(5000);

		product.setOfferId(offerId);
//		product.setTitle("A Tale of Two Cities");
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

		return product;
	}
	
	private ArrayList<Product> getProductsFromApi() throws IOException, GeneralSecurityException{
		ArrayList<Product> products = new ArrayList<Product>();
		ShoppingContent service = getService();
		ShoppingContent.Products.List productsList = service.products()
				.list(MERCHANT_ID);
		do {
			ProductsListResponse page = productsList.execute();
			if (page.getResources() == null) {
				System.out.println("No products found.");
				return products;
			}
			for (Product product : page.getResources()) {
				products.add(product);
			}
			if (page.getNextPageToken() == null) {
				break;
			}
			productsList.setPageToken(page.getNextPageToken());
		} while (true);
		
		return products;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProducts() {
		try {
			ArrayList<Product> products = getProductsFromApi();
			System.out.println("getAllProducts called");
			System.out.println("products.size(): " + products.size());			
			return Response.ok(products).build();
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			return Response.status(500).build();
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrUpdateProduct(Product product) {
		System.out.println(product.getTitle());
		System.out.println(product.getDescription());

		try {
			ShoppingContent service = getService();
			Product newProduct = service.products()
					.insert(MERCHANT_ID, product).execute();

			System.out.print("Inserting new product... ");
			Product product2 = getProduct();
			Product newProduct2 = service.products()
					.insert(MERCHANT_ID, product2).execute();
			System.out.println("done.");

			ArrayList<Product> newProducts = new ArrayList<Product>();

			newProducts.add(newProduct);
			newProducts.add(newProduct2);

			return Response.ok(newProducts).build();
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).build();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllProducts() {
		try {
			ShoppingContent service = getService();
			ArrayList<Product> products = getProductsFromApi();
			
			List<ProductsCustomBatchRequestEntry> productsBatchRequestEntries = new ArrayList<ProductsCustomBatchRequestEntry>();
			
			long i = 0;
			for (Product p : products) {
				ProductsCustomBatchRequestEntry entry = new ProductsCustomBatchRequestEntry();
				entry.setBatchId(i);
				entry.setMerchantId(MERCHANT_ID);
				entry.setProductId(p.getId());
				entry.setMethod("delete");
				productsBatchRequestEntries.add(entry);
				i++;
			}
			
			ProductsCustomBatchRequest deleteRequest = new ProductsCustomBatchRequest();
			deleteRequest.setEntries(productsBatchRequestEntries);
			
			ProductsCustomBatchResponse batchDeleteResponse = service.products()
					.custombatch(deleteRequest).execute();

			return Response.ok(batchDeleteResponse).build();
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).build();
		}	
	}
}
