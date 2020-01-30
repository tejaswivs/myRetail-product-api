package com.target.casestudy.myretail.api.repositories;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.target.casestudy.myretail.api.config.CassandraConfig;
import com.target.casestudy.myretail.api.domain.Price;
import com.target.casestudy.myretail.api.domain.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CassandraConfig.class, ProductRepository.class})
public class ProductRepositoryIntegrationTest {

    private static final Log LOGGER = LogFactory.getLog(ProductRepositoryIntegrationTest.class);
    public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS testKeySpace WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1' };";
    public static final String KEYSPACE_ACTIVATE_QUERY = "USE testKeySpace;";
    public static final String DATA_TABLE_NAME = "products";

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @Before
    public void setUp() throws Exception {

    }

    // start the embedded cassandra server
    @BeforeClass
    public static void startCassandraEmbedded() throws InterruptedException, TTransportException, ConfigurationException, IOException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        final Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9142).build();
        LOGGER.info("Server Started at 127.0.0.1:9142... ");
        final Session session = cluster.connect();
        session.execute(KEYSPACE_CREATION_QUERY);
        session.execute(KEYSPACE_ACTIVATE_QUERY);
        LOGGER.info("KeySpace created and activated.");
        Thread.sleep(5000);
    }

    // create the table for testing purposes
    @Before
    public void createTable() throws InterruptedException, TTransportException, ConfigurationException, IOException {
        adminTemplate.createTable(true, CqlIdentifier.cqlId(DATA_TABLE_NAME), Product.class, new HashMap<String, Object>());
    }

    @Test
    public void testSaveProductPrice__whenProductInsertedInDatabase__thenProductIsAvailableUponRetrieval() {
        // given
        Product product = new Product(111, new Price(new BigDecimal(133.49).setScale(2, RoundingMode.HALF_UP), "USD"));
        productRepository.save(product);

        // when
        Product retrievedProduct = productRepository.findById(111).orElse(null);

        // then
        assertEquals(retrievedProduct.getId(), product.getId());
    }

    @Test
    public void testUpdateProductPrice__whenPriceIsUpdatedInDatabase__thenUpdatedPriceIsAvailableUponRetrieval() {
        // given
        // To update the product, save the product in the database first.
        Product product = new Product(111, new Price(new BigDecimal(133.49).setScale(2, RoundingMode.HALF_UP), "USD"));
        productRepository.save(product);

        //Update the product price
        BigDecimal updatedPrice = new BigDecimal(121);
        product.setCurrent_price(new Price(updatedPrice, "USD"));
        productRepository.save(product);

        // when
        Product retrievedProduct = productRepository.findById(111).orElse(null);

        // then
        assertEquals(updatedPrice, retrievedProduct.getCurrent_price().getValue());
    }

    // Once done testing, drop the table
    @After
    public void dropTable() {
        adminTemplate.dropTable(CqlIdentifier.cqlId(DATA_TABLE_NAME));
    }

    // Stop the embedded cassandra server once done running the test cases
    @AfterClass
    public static void stopCassandraEmbedded() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }

}