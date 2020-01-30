package com.target.casestudy.myretail.api.domain;

import com.datastax.driver.core.DataType;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import com.datastax.driver.core.DataType.Name;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Table("products")
public class Product {

    @PrimaryKey private Integer id;
    private String productName;
    private Price price;

    public Product(Integer id, Price price) {
        this.id = id;
        this.price = price;
    }

    public Product() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
