package com.target.casestudy.myretail.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.casestudy.myretail.api.error.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Service
public class ExtensiveProductDetailServiceImpl implements ExtensiveProductDetailService {

    private @Autowired RestTemplate restTemplate;
    @Value("${extensiveProductDetailApi.host}") protected String extensiveProductDetailApiHost;
    @Value("${extensiveProductDetailApi.path}") protected String extensiveProductDetailApiPath;

    protected Log log = LogFactory.getLog(ExtensiveProductDetailServiceImpl.class);

    /*getProductName(productID) will get the product name from an external service based on the product id
    * This method returns just the product name though the external service call returns extensive product details.
    * It will throw "404 not found" if tried to retrieve a non-existing product.
    * */
    @Override
    public String getProductName(Integer productId) {
        String productName = null;
        String queryParam = "taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
        // add productId as path segment
        UriComponentsBuilder bldr = baseUrl().pathSegment(String.valueOf(productId));
        //Add the query parameters
        bldr.queryParam("excludes", queryParam);
        // get the base url
        URI url = bldr.build().toUri();
        try{
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode =  mapper.readTree(response.getBody());
            // parse the json node to get just the product name
            productName = jsonNode.at("/product/item/product_description/title").asText("Default");
            return productName;
        } catch(RestClientException | IOException ex) {
            log.warn("Error while getting the product name", ex);
        }
        return productName;
    }

    // Build the external service base url.
    protected UriComponentsBuilder baseUrl() {
        return UriComponentsBuilder.fromUriString(extensiveProductDetailApiHost + extensiveProductDetailApiPath);
    }

}
