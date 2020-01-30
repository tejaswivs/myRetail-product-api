package com.target.casestudy.myretail.api.error;

import com.target.casestudy.myretail.api.controllers.ProductController;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Not Found")
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, Object... argumentArray) {
        super(MessageFormatter.arrayFormat(message, argumentArray).getMessage());
    }

}
