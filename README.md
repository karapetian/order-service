## Intro
Order-Service is a Spring Boot application.   
The technology stack involves Java 17, Maven, Spring Data.   
For data storing h2 embedded DB is used.  
For mapping entities and Dtos mapstruct lib is used.  
Unit tests and integration test is present.  
The application is Dockerized.

The main domain is the Order object.  
There are also Customer, OrderItem and OrderHistory.

## How to run?
1. For local run you should install locally MySQL Server. Enter a password for the 'root' user ('Strong.Pwd-123').  
But you cannot use 'mysql' command directly (only with the whole path '/usr/local/mysql-8.2.0-macos13-x86_64/bin/mysql --version').  
On your local machine set its path ('/usr/local/mysql-8.2.0-macos13-x86_64/bin') as an env var and export it.  
  ` mysql -u root -p Strong.Pwd-123`  
   `CREATE DATABASE ordersdb;`  
   `show databases;`  
   `CREATE USER 'user'@'localhost' IDENTIFIED BY 'Strong.Pwd-123';`    
   `GRANT ALL PRIVILEGES ON * . * TO 'user'@'localhost';`
   `FLUSH PRIVILEGES;`   
   `select user from mysql.user;`   

2. When building the application docker plugin will generate a docker image from the Dockerfile.   
   After the successful build from the root directory run the image with _docker-compose up_   
   Container's 8090 port will be mapped to th 8090 on the host machine.  

## Connect to DB
DB_URI: jdbc:mysql://localhost:3306/ordersDB   
DB_USER: user   
DB_PASSWORD: Strong.Pwd-123   

## Endpoints
OpenApi endpoint: http://localhost:8090/imea-systems/swagger-ui/index.html#