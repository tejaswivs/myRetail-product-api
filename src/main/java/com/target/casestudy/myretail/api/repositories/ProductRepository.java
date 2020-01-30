package com.target.casestudy.myretail.api.repositories;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.target.casestudy.myretail.api.domain.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("select * from products where id =?0")
    Product findByIds(Integer productId);

}
