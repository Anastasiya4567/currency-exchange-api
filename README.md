# Currency Exchange API

A Spring Boot REST API for creating currency accounts and exchanging PLN <-> USD using real-time NBP rates.

## Features
- Create a currency account with initial PLN balance, first name, and last name
- Exchange PLN <-> USD using NBP public API rates
- Retrieve account info and balances in PLN and USD
- Data persists after restart (H2 file database)

## Requirements
- Java 21
- Spring Boot
- Maven
- H2 Database
- Swagger/OpenAPI for API documentation

## How to Run

1. **Build the project:**
   ```sh
   mvn clean package
   ```
2. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   or run the generated JAR:
   ```sh
   java -jar target/currency-exchange-api-0.0.1-SNAPSHOT.jar
   ```
3. **API Endpoints:**
   - `POST /accounts` – create account
   - `GET /accounts/{id}` – get account info
   - `POST /accounts/{id}/exchange` – exchange currency

## Example Requests

### Create Account
```
POST /accounts
{
  "firstName": "John",
  "lastName": "Doe",
  "initialBalancePLN": "1000.00"
}
```

### Exchange PLN to USD
```
POST /accounts/{id}/exchange
{
  "fromCurrency": "PLN",
  "toCurrency": "USD",
  "amount": "100.00"
}
```

### Exchange USD to PLN
```
POST /accounts/{id}/exchange
{
  "fromCurrency": "USD",
  "toCurrency": "PLN",
  "amount": "10.00"
}
```

### Get Account Info
```
GET /accounts/{id}
```

## H2 Console
- Access at [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:file:./data/currencydb`

## Swagger UI / OpenAPI Docs

After starting the application, you can explore and test the API using Swagger UI:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

The OpenAPI (Swagger) specification is also available at:
- [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Running Tests
Run all unit and integration tests:
```sh
mvn test
```

## Project Structure
- src/main/java: Application source code
- src/test/java: Unit and integration tests
- src/main/resources/openapi: OpenAPI spec and config

## Notes
- All monetary values in requests/responses must be JSON strings (e.g., "100.00"), not numbers.
- Exchange rates are fetched live from [NBP API](http://api.nbp.pl/)
- Data is stored in a file-based H2 database (`./data/currencydb`)
- All dependency versions are managed in `pom.xml` properties for easy upgrades.
- The build automatically removes unwanted OpenAPI-generated test files to prevent test errors.
