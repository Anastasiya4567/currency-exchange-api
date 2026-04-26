# Currency Exchange API

A Spring Boot REST API for creating currency accounts and exchanging PLN <-> USD using real-time NBP rates.

## Features
- Create a currency account with initial PLN balance, first name, and last name
- Exchange PLN <-> USD using NBP public API rates
- Retrieve account info and balances in PLN and USD
- Data persists after restart (H2 file database)

## Requirements
- Java 21
- Maven

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
  "initialBalancePLN": 1000.0
}
```

### Exchange PLN to USD
```
POST /accounts/{id}/exchange
{
  "fromCurrency": "PLN",
  "amount": 100.0
}
```

### Exchange USD to PLN
```
POST /accounts/{id}/exchange
{
  "fromCurrency": "USD",
  "amount": 10.0
}
```

### Get Account Info
```
GET /accounts/{id}
```

## H2 Console
- Access at [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:file:./data/currencydb`

## Notes
- Exchange rates are fetched live from [NBP API](http://api.nbp.pl/)
- Data is stored in a file-based H2 database (`./data/currencydb`)

---

For any questions, contact via email.
