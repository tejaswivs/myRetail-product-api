package com.target.casestudy.myretail.api.controllers;

import com.target.casestudy.myretail.api.domain.Product;
import com.target.casestudy.myretail.api.error.NotFoundException;
import com.target.casestudy.myretail.api.repositories.ProductRepository;
import com.target.casestudy.myretail.api.service.ProductDetailAggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {

    @Autowired private ProductDetailAggregatorService productDetailAggregatorService;
    @Autowired private ProductRepository productRepository;

    /* Performs an HTTP GET to retrieve the product-detail by ID. The response includes
     * product name from an external service and product price from the data store.
     * The method will throw "Product not found exception" when tried to retrieve
     * a non-existing product.
    */
    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable("productId") Integer productId) {
        Product ret = productDetailAggregatorService.getProductDetail(productId);
        if(ret !=null) return ret;
        throw new NotFoundException("no product with ID {}", productId);
    }

    /* Performs an HTTP PUT to update the product's price in the data store .
     * The price to be updated is obtained from the JSON request body
     * The method will throw "product not found" exception when a try to update
     * a non-existing product is made.
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<String> updateProduct(@RequestBody @Valid Product productPriceUpdate, @PathVariable("productId") Integer productId) {
        Product product = productRepository.findByIds(productId);
        if (product != null) {
            product.setCurrent_price(productPriceUpdate.getCurrent_price());
            productRepository.save(product);
            return new ResponseEntity<String>("product price has been updated successfully",
                    HttpStatus.OK);
        } else throw new NotFoundException("no product with ID {}", productId);
    }

}
