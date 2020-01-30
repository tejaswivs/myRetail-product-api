package com.target.casestudy.myretail.api.repositories;

import com.target.casestudy.myretail.api.domain.Product;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("select * from products where id =?0")
    Product findByIds(Integer productId);

}
