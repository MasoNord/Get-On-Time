# Delivery App

## Table of Content:
- [About the project](#about-the-project)
- [Dependencies](#dependencies)
- [How to install](#How-to-install)
- [How to use:](#CRUD-API)
    - [CRUD API]()
    - [Courier ranking]()
    - [Orders distribution]()

## About the project:
This is an implementation of backend for delivery service (no frontend).

App will consist of 4 parts:
* CRUD API: here we're expecting to have about 7 basic methods
* Courier ranking: here we're going to calculate salary for every courier
* Rate Limiter
* Orders distribution: before the start of a working day the service is going to divide available orders between couriers base on distance, order's weight, courier's type, etc.

## Dependencies:
- Spring Boot
- Java 21
- PostgreSQL
- Hibernate
- Docker & Docker Compose
- Junit
- Spring Security and jjwt
- Log4j
- [Geocoding API](https://geocode.maps.co/)
- lombok
- jackson
- bucket4j

## How to install:
1. Clone the following repository
```
git clone https://github.com/MasoNord/delivery-app.git
```
2. Import it in Intellij IDE or any IDE/text editor of you flavor

3. Run the application

## How to use:
