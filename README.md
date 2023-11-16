## Intro
Order-Service is a Spring Boot application.
The technology stack involves Java 17, Maven, Spring Data.   
For data storing h2 embedded DB is used.  
For mapping entities and Dtos mapstruct lib is used.  
Unit tests and integration test is present.  

The main domain is the Order object.  
There are also Customer and OrderItem.  

## DB
H2 console: http://localhost:8090/imea-systems/h2-console   
username: sa        
password: Strong.Pwd-123

## Endpoints
OpenApi endpoint: http://localhost:8090/imea-systems/swagger-ui/index.html#