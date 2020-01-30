# myRetail-product-api

This is a Spring Boot based RESTful web service that:

    1. Agrregrates product data from the multiple sources and return it as JSON to the caller.
    2. Allows client to update the product price in the data store.

### Cassandra as Database    
The myRetail-product-api uses Apache Cassandra as database to retrieve and update production prices.
This application is using Cassandra unit library to run an embedded Cassandra server. The embedded server 
runs on 127.0.0.1:9142. With the help of embedded server we don't need to have a local instance of Cassandra running.  
    
### Quick start 

    1. Clone the repo from -
    2. run MyRetailApplication.java as a "Java application" or "mvn spring-boot:run" from command line/terminal (Running the 
        Spring Boot application will start the embedded Cassandra server along with inserting few product recodrs for the case study api use)
    4. expect APIs to reside on http://localhost:8080/products/{productId}
    
### API URLs and expected behavior

    | Path            | Description             |  Sample URL     |
    |-----------------|-------------------------| ----------------|
    |`/product/{productId}`   | returns a JSON object of product containing aggregated product detail from multiple resources |  
    |`/product/{productId}`   | a POST operation which updates the product price in the data store |  http://localhost:8084/products/13860428 
    
    Sample URLs
        Getting a prodct detail 
        http://localhost:8088/products/13860428
        
       Updating product price
       http://localhost:8088/products/13860428 with request body - {"id":13860428,"productName":"The Big Lebowski (Blu-ray)","price":{"value":157,"currencyCode":"USD"}}
         
### Running test cases
    mvn test
    
    As some of the integration test cases use their own Embedded Cassandra server for executing the test cases(running on the same ip: port (127.0.0.1:9142)  
    you might run into port binding issue.
