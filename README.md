# Application

## Introduction

This is project is a simple implementation of a backend for delivery service powered by Spring Boot.
In real world delivery services do have a really sophisticated infrastructure. It requires consideration
of many edge cases and solid system architecture. This project is simple a demo demonstrating basic features
This project certainly has bugs and vulnerabilities, so please don't blame me for that, I'm a beginner and still
learning how to build proper backend 

## About This Project
1. Courier:
   * You can accept, complete orders
   * Track ongoing rides
   * Browse nearby restaurants based on your location and transport
   * Your complete orders is saved on the database
2. Customer:
    * You can make new order
    * Browse all personal orders
    * Browse nearby restaurants based on your location
3. All users:
    * Login into their accounts
    * Create new accounts
    * Update personal information
    * Update password
    * Update current location 
4. Owners:
    * Add new restaurant
    * Track all orders
    * Update menu profile

For more information about app's functionality, please, refer to the open API documentation

## Prerequisites

What things you need to install

~~~
    - Locally installed PostgreSQL server
    - COmpativle IDE, Intellij IDEA strongly recommended for this projects
    - In case you want to run the Dockerized version keep in mind, docker/docker-compouse must be installed on you computer
    - Create an account on https://geocode.maps.co/ and get a free API key which we'll need to specify in propery file 
~~~

## Installing / Running
   To run the application fill required fields in application.properties file as following
~~~
   # Spring Security properties

spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
refresh.token.expiration =  < your refersh token expiration >
access.token.expiration= < your access token expiration >
secret = < your secret >
geocoding = < Here must be your API key from https://geocode.maps.co/ >

# Database properties

jdbc.driverClassName = org.postgresql.Driver
jdbc.url = jdbc:postgresql://localhost:5432/delivery-app
jdbc.username = < your user name >
jdbc.password = < your password >
hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql = true
hibernate.format_sql = true
hibernate.hbm2ddl.auto = update
hibernate.max.depth = 1

#Rate Limiter Settings

capacity = 100
tokens = 10
defaultTokens = 1

# Spring Configuration

spring.output.ansi.enabled=ALWAYS
server.port = 8099
~~~

You can also change logger's properties in log4j2.xml file if you want

For PostgrSQL

~~~
   Make sure that you create a database with a name delivery-app, or came up with your own
   naming, but don't forget to make changes in the db section of a property file
~~~

For Docker

~~~
   First of all, make sure you build an executable jar file
   
   In the root folder run the followign command
   
   docker-compose up
~~~


## Built With
- [Spring](https://spring.io/) - Spring Boot resource and authorization server
- [Java](https://www.oracle.com/technetwork/java/javase/overview/index.html)
- [PostgreSQL](https://www.postgresql.org/)
- [Hibernate](https://hibernate.org/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Junit](https://junit.org/junit5/)
- [Mockito](https://github.com/mockito/mockito)
- [Log4j](https://logging.apache.org/log4j/2.x/)
- [Geocoding API](https://geocode.maps.co/)
- [Lombok](https://projectlombok.org/)
- [Jackson](https://github.com/FasterXML/jackson)
- [bucket4j](https://bucket4j.com/)
