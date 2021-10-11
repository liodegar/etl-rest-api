# Adverity ETL REST API (AERA)

A simple ETL Spring Boot application with H2. This API provides the following basics functionalities: 

1. Endpoint that returns a list of WebTracking objects for dataSource and optional parameters: campaign and range of date.
2. Endpoint that returns aggregated statistic values like count, sum, min, max, avg per metric and dataSource. Optional parameters: campaign and range of date.
3. Endpoint that returns a statistics summary for the given datasource and optional parameters: campaign and range of date.

## Design considerations
- The API supports two levels of security by using Spring Security and JSON Web Token.
  
- Logging: SL4J was used to decouple from any specific implementation. The underlying logging is provided by Log4J. 
  To avoid any locking with a specific filesystem location, the generated logs are sent to the STDOUT console.

- Exception handling: all the errors and exceptions are gracefully managed and mapped to the corresponding HTTP status code.

- Documentation: all the main classes are intradocumented (Javadocs) and also the API is exposed via Swagger.

- No hard coded values. All the config properties are defined in the `application.properties` file

- Test coverage: the main functionalities were tested (integration tests) to validate their proper functionality.

## Stack
- Kotlin 1.5.31
- Java 11
- Spring Boot 2.5.5
- H2
- Kotlin Logging, SL4J & Log4J
- Maven
- JUnit 5

## Getting Started

In order to start the application locally and run the APIs, you should execute from a command line the following instruction:

`./mvnw spring-boot:run`

You must be inside the base project directory the previous command.

After this the Adverity ETL REST API (AERA) will start.

### Deployment
The application is fully deployed on Heroku and it is accessible at: https://etl-rest-api.herokuapp.com

### Running the web services

1. To get access to the API, you should get a token in the following endpoint:

   `https://etl-rest-api.herokuapp.com/v1/security/generate-token?subject=appSubject`

   HTTP method: GET
      
   
   Using BASIC auth with the following credentials:

        username=adverity
        password=adverity2021
      
   And adding to the header:
     
        Content-Type=application/json


   After invoking this endpoint you will get something like this:

   ```
     "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3ZWF0aGVyU3ViamVjdCIsImV4cCI6MTUzOTU2NzI0OX0.J3Xli1EV-T_cP-nQ_uJbkYGcYJdGINSvlmrwC6cSiHY"
   ```

   You should copy all the result value (this is the generated token) in order to invoke the other secured endpoints.

2. After generating the token, you can get the list of WebTracking objects for dataSource and optional parameters:
   
    `https://etl-rest-api.herokuapp.com/v1/adverity/web-trackings/data-sources//Google Ads?campaign=GDN_Retargeting`
   
    HTTP method: GET
    
    Header parameters:
     ```
            Authorization:Basic dXNlcjpmbGFjb25pQDIwMTg=
     ```
      ```
            Content-Type:application/json
      ```
      ```
            authorizationToken:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXRlZ29yeVN1YmplY3QiLCJleHAiOjE1NDQzMTU1NjZ9.D-_UsI_YiAWSTKXxAlyDc9aQhOZwP71HZzJC4dyeYM0
      ```
        
    Response:
   ```
   [
       {
           "dataSource": "Google Ads",
           "campaign": "Adventmarkt Touristik",
           "daily": "2019-11-14",
           "clicks": 147,
           "impressions": 80351
       },
       {
           "dataSource": "Google Ads",
           "campaign": "Adventmarkt Touristik",
           "daily": "2019-11-15",
           "clicks": 131,
           "impressions": 81906
       }
   ]
   ```

3. To get the list of aggregated statistic values per metric and dataSource:

   `https://etl-rest-api.herokuapp.com/v1/adverity/statistics/metrics/IMPRESSIONS/data-sources/Google Ads?campaign=GDN_Retargeting`

    HTTP method: GET

    Header parameters:
   ```
     Authorization:Basic dXNlcjpmbGFjb25pQDIwMTg=
   ```
   ```
     Content-Type:application/json
   ```
   ```
     authorizationToken:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXRlZ29yeVN1YmplY3QiLCJleHAiOjE1NDQzMTU1NjZ9.D-_UsI_YiAWSTKXxAlyDc9aQhOZwP71HZzJC4dyeYM0
   ```

   Response:
   ```
   {
       "metric": "IMPRESSIONS",
       "count": 405,
       "sum": 18991655,
       "min": 13596,
       "max": 130041,
       "avg": 46892.98
   }
   ```

4. To get the statistics summary for the given datasource and optional parameters:

   `https://etl-rest-api.herokuapp.com/v1/adverity/summary-statistics/data-sources/{dataSource}/Google Ads?campaign=GDN_Retargeting`

   HTTP method: GET

   Header parameters:
   ```
     Authorization:Basic dXNlcjpmbGFjb25pQDIwMTg=
   ```
   ```
     Content-Type:application/json
   ```
   ```
     authorizationToken:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXRlZ29yeVN1YmplY3QiLCJleHAiOjE1NDQzMTU1NjZ9.D-_UsI_YiAWSTKXxAlyDc9aQhOZwP71HZzJC4dyeYM0
   ```

   Response:
   ```
   {
    "statsPerMetrics": [
        {
            "metric": "CLICKS",
            "count": 410,
            "sum": 6454,
            "min": 1,
            "max": 42,
            "avg": 15.74
        },
        {
            "metric": "IMPRESSIONS",
            "count": 410,
            "sum": 183048,
            "min": 23,
            "max": 1238,
            "avg": 446.46
        }
    ],
    "clickThroughRate": 0.04  
    }
     ```
##Restrictions
As this service is an ETL POC, there is still room for improvement, like:

- Caching, at the client level (ETags) for GET retrieval operations, and at the application level (Redis to cache the service results).

- Comprehensive testings for all the layers.

- More robust API for querying data. 

- Pagination.

- Addressing some performance improvements.


## More info

You can check all the functionalities exposed by this API in: `https://etl-rest-api.herokuapp.com/swagger-ui.html`

![Adverity ETL REST API (AERA)](swagger.png "Adverity ETL REST API (AERA)")


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Liodegar Bracamonte** - *Initial work* - [liodegar@gmail.com)


## License

Apache License 2.0.

## Acknowledgments

* To the all open source software contributors.


