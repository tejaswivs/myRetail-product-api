package com.target.casestudy.myretail.api.service;

import com.target.casestudy.myretail.api.domain.Product;

import java.util.Optional;

public interface ProductPriceService {
   public Product getProductPrice(Integer productId);
}

