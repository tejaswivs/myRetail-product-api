package com.target.casestudy.myretail.api.service;

import com.target.casestudy.myretail.api.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ExtensiveProductDetailServiceITest {

    @Autowired ExtensiveProductDetailService service;

    @Test
    public void getProductName__returnsProductName() {
        String productName = service.getProductName(13860427);
        assertEquals("Conan the Barbarian (dvd_video)", productName);
    }

    @Test
    public void getProductName__returnsNullWhenProductNotPresent() {
        String productName = service.getProductName(123);
        assertEquals(null, productName);
    }


}
