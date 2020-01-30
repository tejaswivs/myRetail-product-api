package com.target.casestudy.myretail.api.domain;

import java.math.BigDecimal;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("price")
public class Price {
	
    private BigDecimal value;
    private String currencyCode;

    public Price(BigDecimal value, String currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getCurrency_code() {
        return currencyCode;
    }

    public void setCurrency_code(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
