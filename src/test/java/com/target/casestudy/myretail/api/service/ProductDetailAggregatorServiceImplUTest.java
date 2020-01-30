package com.target.casestudy.myretail.api.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.target.casestudy.myretail.api.domain.Price;
import com.target.casestudy.myretail.api.domain.Product;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailAggregatorServiceImplUTest {

    @InjectMocks ProductDetailAggregatorServiceImpl productDetailAggregatorService;

    @Mock ProductPriceService productPriceService;
    @Mock ExtensiveProductDetailService extensiveProductDetailService;

    @Test
    public void testGetProductDetail__whenProductIsPresent__returnsAggregatedProductDetail() {

        // given
        Integer productId = 1234;
        BigDecimal productValue = new BigDecimal(12.23);
        Price price = new Price(productValue, "USD");
        Product product = new Product(productId, price);
        Mockito.when(productPriceService.getProductPrice(productId)).thenReturn(product);

        String productName = "This is my first product";
        Mockito.when(extensiveProductDetailService.getProductName(productId)).thenReturn(productName);

        // when
        Product retrievedProduct = productDetailAggregatorService.getProductDetail(productId);

        // then
        assertEquals(productValue, retrievedProduct.getCurrent_price().getValue());
        assertEquals("USD", retrievedProduct.getCurrent_price().getCurrency_code());
        assertEquals("This is my first product", retrievedProduct.getName());

    }

    @Test
    public void testGetProductDetail__whenProductIsNotPresent__returnsNoProductDetail() {

        // given
        Integer productId = 1234;
        Product product = new Product();
        Mockito.when(productPriceService.getProductPrice(productId)).thenReturn(product);
        Mockito.when(extensiveProductDetailService.getProductName(productId)).thenReturn(null);

        // when
        Product retrievedProduct = productDetailAggregatorService.getProductDetail(productId);

        // then
        assertEquals(null, retrievedProduct.getName());
        assertEquals(null, retrievedProduct.getCurrent_price());
    }

    @Test
    public void testGetProductDetail__whenProductPriceIsNotPresentButNameDoes__returnsOnlyProductName() {

        // given
        Integer productId = 1234;
        Product product = new Product();
        Mockito.when(productPriceService.getProductPrice(productId)).thenReturn(product);
        Mockito.when(extensiveProductDetailService.getProductName(productId)).thenReturn("dummy product name");

        // when
        Product retrievedProduct = productDetailAggregatorService.getProductDetail(productId);

        // then
        // retrieves product name
        assertEquals("dummy product name", retrievedProduct.getName());

        // no price for the product
        assertEquals(null, retrievedProduct.getCurrent_price());
    }

    @Test
    public void testGetProductDetail__whenProductPriceIsPresentButNotTheName__returnsOnlyProductPrice() {
        // given
        Integer productId = 1234;
        BigDecimal productValue = new BigDecimal(12.23);
        Price price = new Price(productValue, "USD");
        Product product = new Product(productId, price);
        Mockito.when(productPriceService.getProductPrice(productId)).thenReturn(product);
        Mockito.when(extensiveProductDetailService.getProductName(productId)).thenReturn(null);

        // when
        Product retrievedProduct = productDetailAggregatorService.getProductDetail(productId);

        // then
        // retrieves product price
        assertEquals(productValue, retrievedProduct.getCurrent_price().getValue());
        assertEquals("USD", retrievedProduct.getCurrent_price().getCurrency_code());

        // no name for the product found
        assertEquals(null, retrievedProduct.getName());
    }
}
