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

### Frontend

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 17.0.8.

#### Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

#### Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

#### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

#### Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

#### Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

#### Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

