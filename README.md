## Intro
Order-Service is a Spring Boot application.   
The technology stack involves Java 17, Maven, Spring Data.   
For data storing h2 embedded DB is used.  
For mapping entities and Dtos mapstruct lib is used.  
Unit tests and integration test is present.  

The main domain is the Order object.  
There are also Customer and OrderItem.  

## How to Run?
Turn on you Docker. IF you don't have Docker installed and/or want to run without it,      
then comment out _docker-maven-plugin_ in pom.xml. 

1. Run through IntelliJ IDEA

2. From the root directory run _mvn clean install_     
After the successful build run _java -jar target/order-service.jar_   

3. When building the application docker plugin will generate a docker image from the Dockerfile.   
After the successful build from the root directory run the image with _docker-compose up_   
Container's 8090 port will be mapped to th 8090 on the host machine. 

## DB
H2 console: http://localhost:8090/imea-systems/h2-console   
JDBC URL: jdbc:h2:mem:imeasystems     
username: sa        
password: Strong.Pwd-123

## Endpoints
OpenApi endpoint: http://localhost:8090/imea-systems/swagger-ui/index.html#