package com.target.casestudy.myretail.api.domain;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("products")
public class Product {

    @PrimaryKey private Integer id;
    private String name;
    private Price current_price;

    public Product(Integer id, Price price) {
        this.id = id;
        this.current_price = price;
    }

    public Product() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String productName) {
        this.name = productName;
    }

    public Price getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(Price price) {
        this.current_price = price;
    }
}
