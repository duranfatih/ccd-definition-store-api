# Case definition store

Validation and persistence of definitions for field types, jurisdictions, case types and associated display elements.

## Overview

Definitions are imported as an Excel spreadsheet which are parsed, persisted and then exposed as JSON through a REST API.

Spring Boot and Spring Data are used to persist the data in a PostgreSQL database. The database schema is created and maintained by Liquibase changesets applied during application startup.

## Getting started

### Prerequisites

- [JDK 8](https://www.oracle.com/java)
- [Docker](https://www.docker.com)

#### Environment variables

The following environment variables are required:

| Name | Default | Description |
|------|---------|-------------|
| DEFINITION_STORE_DB_USERNAME | - | Username for database |
| DEFINITION_STORE_DB_PASSWORD | - | Password for database |
| DEFINITION_STORE_IDAM_KEY | - | Definition store's IDAM S2S micro-service secret key. This must match the IDAM instance it's being run against. |
| DEFINITION_STORE_S2S_AUTHORISED_SERVICES | ccd_data,ccd_gw | Authorised micro-service names for S2S calls |
| IDAM_USER_URL | - | Base URL for IdAM's User API service (idam-app). `http://localhost:4501` for the dockerised local instance or tunneled `dev` instance. |
| IDAM_S2S_URL | - | Base URL for IdAM's S2S API service (service-auth-provider). `http://localhost:4502` for the dockerised local instance or tunneled `dev` instance. |
| USER_PROFILE_HOST | - | Base URL for the User Profile service. `http://localhost:4453` for the dockerised local instance. |
| APPINSIGHTS_INSTRUMENTATIONKEY | - | secrets for Microsoft Insights logging, can be a dummy string in local |

### Building

The project uses [Maven](https://maven.apache.org/). 

To build project please execute the following command:

```bash
mvn install
```

### Running

If you want your code to become available to other Docker projects (e.g. for local environment testing), you need to build the image:

```bash
docker-compose build
```

The above will build both the application and database images.  
If you want to build only one of them just specify the name assigned in docker compose file, e.g.:

```bash
docker-compose build ccd-definition-store-api
```

When the project has been packaged in `target/` directory, 
you can run it by executing following command:

```bash
docker-compose up
```

As a result the following containers will get created and started:

 - Database exposing port `5451`
 - API exposing ports `4451`

#### Handling database

Database will get initiated when you run `docker-compose up` for the first time by execute all scripts from `database` directory.

You don't need to migrate database manually since migrations are executed every time `docker-compose up` is executed.

You can connect to the database at `http://localhost:5451` with the username and password set in the environment variables.

## Modules

The application is structured as a Maven multi-module project. The modules are:

### repository

Data access layer.

### domain

Domain logic.

### rest-api

Secured RESTful API giving access to part of the domain logic.

### excel-importer

Secured endpoint and specific logic for importing case definition as an Excel spreadsheet.

### application

Spring application entry point and configuration.

## LICENSE

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

