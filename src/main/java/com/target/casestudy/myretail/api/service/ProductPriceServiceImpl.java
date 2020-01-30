package com.target.casestudy.myretail.api.service;

import com.target.casestudy.myretail.api.domain.Product;
import com.target.casestudy.myretail.api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceServiceImpl implements ProductPriceService {

    @Autowired
    ProductRepository productRepository;

    /*Finds the product by it's id from the data store.
    * Using findByIds from the CrudRepository*/
    @Override
    public Product getProductPrice(Integer productId) {
        return productRepository.findByIds(productId);
    }
}
