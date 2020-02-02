package com.target.casestudy.myretail.api;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.target.casestudy.myretail.api.domain.Price;
import com.target.casestudy.myretail.api.domain.Product;
import com.target.casestudy.myretail.api.repositories.ProductRepository;

@SpringBootApplication
public class MyRetailApplication implements CommandLineRunner{

	@Autowired
	ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MyRetailApplication.class);
		app.run(args);
	}

	/* We need to insert some records in the data store before the application is up.
	* This way once the application is up and running, all the rest api end points will start
	* responding properly to the requests. We can seed the data using a startup script as well.
	* Inserting data directly in the run method as we need just few rows to make this case study work */
	@Override
	public void run(String... arg) throws Exception {
		Product product1 = new Product(13860428, new Price(new BigDecimal(13.49).setScale(2, RoundingMode.HALF_UP), "USD"));
		Product product2 = new Product(13860427, new Price(new BigDecimal(11.19).setScale(2, RoundingMode.HALF_UP), "USD"));
		productRepository.save(product1);
	
		productRepository.save(product2);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
