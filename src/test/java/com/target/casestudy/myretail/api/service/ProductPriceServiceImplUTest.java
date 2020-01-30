package com.target.casestudy.myretail.api.service;

import com.target.casestudy.myretail.api.domain.Price;
import com.target.casestudy.myretail.api.domain.Product;
import com.target.casestudy.myretail.api.repositories.ProductRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class ProductPriceServiceImplUTest {

    @InjectMocks
    ProductPriceServiceImpl service;

    @Mock ProductRepository productRepository;

    @Test
    public void getProductPrice__whenProductPriceIsAvailable() {
        // given
        BigDecimal value = new BigDecimal(12.45);
        String currencyCode = "USD";
        Price price = new Price(value, currencyCode);
        Product product = new Product(1423432, price);
        Mockito.when(productRepository.findByIds(anyInt())).thenReturn(product);

        // when
        Product retrievedProduct = service.getProductPrice(1423432);

        // then
        assertEquals(retrievedProduct.getPrice().getValue(), value);
        assertEquals(currencyCode, retrievedProduct.getPrice().getCurrencyCode());
    }

    @Test
    public void getProductPrice__whenProductIsNotAvailableInTheDatabase() {
        // given
        Product product = new Product();
        Mockito.when(productRepository.findByIds(anyInt())).thenReturn(product);

        // when
        Product retrievedProduct = service.getProductPrice(1423432);

        // then
        assertEquals(null, retrievedProduct.getId());
    }
}
