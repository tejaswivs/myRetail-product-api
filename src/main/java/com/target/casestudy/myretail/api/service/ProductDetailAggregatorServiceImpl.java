package com.target.casestudy.myretail.api.service;

import com.target.casestudy.myretail.api.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailAggregatorServiceImpl implements ProductDetailAggregatorService {

    @Autowired ExtensiveProductDetailService extensiveProductDetailService;
    @Autowired ProductPriceService productPriceService;

    /*This service method will aggregate the product details from two different services
    * and return the aggregated details. It will get the price first from the data store and
    * product name later. The service calls sequence is not important. If one service fails to respond
    * with detail, aggregator will still go ahead and respond response from the other service*/
    @Override
    public Product getProductDetail(Integer productId) {
        Product product = new Product();
        // Get the product price from the data store service.
        product = productPriceService.getProductPrice(productId);
        // get the product name from the external service
        String productName = extensiveProductDetailService.getProductName(productId);
        if(productName != null) product.setName(productName);
        return product;
    }
}
