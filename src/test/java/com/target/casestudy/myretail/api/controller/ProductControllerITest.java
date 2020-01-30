package com.target.casestudy.myretail.api.controller;

import com.target.casestudy.myretail.api.domain.Price;
import com.target.casestudy.myretail.api.domain.Product;
import com.target.casestudy.myretail.api.error.NotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    public @Rule
    ExpectedException thrown = ExpectedException.none();

    @Test
    public void getProductDetailById_returnsProductDetail() {
        // when
        Product productDetail = restTemplate.getForObject("/products/13860428", Product.class);

        // then
        assertThat(productDetail.getId(), is(equalTo(13860428)));
        assertThat(productDetail.getName(), is(equalTo("The Big Lebowski (Blu-ray)")));
        assertThat(productDetail.getCurrent_price().getValue(), is(equalTo(new BigDecimal(13.49).setScale(2, RoundingMode.HALF_UP))));
    }

    @Test
    public void updateProductPriceById_updatesProductPrice() {
        // given
        Price newPrice = new Price(new BigDecimal(15.51), "USD");
        Integer productId = 13860428;
        Product updateProduct = new Product(productId, newPrice);

        HttpEntity<Product> request = new HttpEntity<>(updateProduct);

        // when
        HttpStatus responseStatus = restTemplate.exchange("/products/"+productId, HttpMethod.PUT,request,String.class).getStatusCode();

        // then
        assertEquals(HttpStatus.OK, responseStatus);
    }


}
