package com.target.casestudy.myretail.api;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.target.casestudy.myretail.api.config.CassandraConfig;
import com.target.casestudy.myretail.api.domain.Price;
import com.target.casestudy.myretail.api.domain.Product;
import com.target.casestudy.myretail.api.repositories.ProductRepository;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

@SpringBootApplication
public class MyRetailApplication implements CommandLineRunner{

	@Autowired
	ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MyRetailApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "8084"));
		app.run(args);
	}

	/* We need to insert some records in the data store before the application is up.
	* This way once the application is and running, all the rest api end points will start
	* responding properly to the requests. We can seed the data using a startup script as well.
	* Inserting data directly in the run method as we need just few rows to make this case study work */
	@Override
	public void run(String... arg) throws Exception {
		Product product = new Product(13860428, new Price(new BigDecimal(13.49).setScale(2, RoundingMode.HALF_UP), "USD"));
		productRepository.save(product);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
