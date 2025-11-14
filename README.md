# EG - BE assignment - Invoice Service
# By : Rajan Chauhan

This is a BE assignment on implementing an invoice system
using Simple Spring Boot REST service .


Access H2 console for in-mem DB at : http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:invoicedb
User: sa
Password: (leave blank)

Sample invoices are pre-loaded from src/main/resources/data.sql. Example IDs: inv-1, inv-2, inv-3.

Quick test sequence (curl examples):

List invoices curl http://localhost:8080/invoices

Show inv-1 curl http://localhost:8080/invoices/inv-1

Process overdue (example: lateFee 10.5, overdueDays 10) curl -X POST -H "Content-Type: application/json" -d '{"lateFee":10.5,"overdueDays":10}' http://localhost:8080/invoices/process-overdue
APIs:
- POST /invoices
  Request: { "amount": 199.99, "dueDate": "2021-09-11" }
  Response: 201 { "id": "..." }

- GET /invoices
  Response: 200 [ { id, amount, paidAmount, dueDate, status } ]

- POST /invoices/{id}/payments
  Request: { "amount": 159.99 }
  Response: updated invoice

- POST /invoices/process-overdue
  Request: { "lateFee": 10.5, "overdueDays": 10 }
  Response: list of processed invoices (those that were pending and overdue)

Below implementations done as per the given assignment:
- In-memory repository is used (ConcurrentHashMap). Easy to replace with Spring Data JPA.
- Dates use ISO format (e.g., 2021-09-11). DTO expects LocalDate parsing by Jackson.
- Partial payments mark original as PAID and create a new invoice with remaining + lateFee.
- Unpaid invoices become VOID and a new invoice is created with amount + lateFee.
- New invoices created when processing overdue have dueDate = today + overdueDays.

Build & run locally:
- mvn clean package
- java -jar target/invoice-service-0.0.1-SNAPSHOT.jar

Run with Docker:
- docker build -t invoice-service .
- docker run -p 8080:8080 invoice-service

Or using docker-compose:
- docker-compose up --build

Run tests:
- mvn test
