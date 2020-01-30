package com.target.casestudy.myretail.api.controller;

import com.target.casestudy.myretail.api.controllers.ProductController;
import com.target.casestudy.myretail.api.domain.Price;
import com.target.casestudy.myretail.api.domain.Product;
import com.target.casestudy.myretail.api.error.NotFoundException;
import com.target.casestudy.myretail.api.repositories.ProductRepository;
import com.target.casestudy.myretail.api.service.ProductDetailAggregatorService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerUTest {

    @InjectMocks
    ProductController controller;

    @Mock
    ProductDetailAggregatorService productDetailAggregatorService;

    @Mock
    ProductRepository productRepository;

    public @Rule
    ExpectedException thrown = ExpectedException.none();

    @Test
    public void getProduct__whenProductAvailable__returnsProductDetail() {
        // given
        BigDecimal productValue = new BigDecimal(12.74);
        Price price = new Price(productValue, "USD");
        Integer productId = 12345;
        Product product = new Product(productId, price);
        product.setProductName("This is my first product");
        Mockito.when(productDetailAggregatorService.getProductDetail(productId)).thenReturn(product);

        // when
        Product retrievedProduct = controller.getProduct(productId);

        // then
        assertEquals(productValue, retrievedProduct.getPrice().getValue());
        assertEquals("USD", retrievedProduct.getPrice().getCurrencyCode());
        assertEquals("This is my first product", retrievedProduct.getProductName());
    }

    @Test
    public void getProduct__productNotAvailable__throwsProductNotFoundException() {
        // expect the "not found exception along with the message"
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("no product with ID 12345");

        // when
        Mockito.when(productDetailAggregatorService.getProductDetail(12345)).thenReturn(null);
        Product retrievedProduct = controller.getProduct(12345);
    }

    @Test
    public void updateProductPrice__whenProductIsPresent__updatesThePrice() {
        // given
        BigDecimal productCurrentValue = new BigDecimal(10.99);
        Price currentPrice = new Price(productCurrentValue, "USD");
        Product currentProduct = new Product(12345, currentPrice);
        Mockito.when(productRepository.findByIds(anyInt())).thenReturn(currentProduct);

        BigDecimal updatedPriceValue = new BigDecimal(102.99);
        Price updatedPrice = new Price(updatedPriceValue, "USD");
        Product updatedProduct = new Product(12345, updatedPrice);

        // when
        HttpStatus status = controller.updateProduct(updatedProduct, 12345).getStatusCode();

        // then
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    public void updateProductPrice__whenProductIsNotPresent__throwsNotFoundException() {

        // expect the "not found exception along with the message"
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("no product with ID 12345");
        // given
        BigDecimal updatedPriceValue = new BigDecimal(102.99);
        Price updatedPrice = new Price(updatedPriceValue, "USD");
        Product updatedProduct = new Product(12345, updatedPrice);

        // when
        controller.updateProduct(updatedProduct, 12345);
    }


}
