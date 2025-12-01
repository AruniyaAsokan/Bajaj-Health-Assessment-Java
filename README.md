# Bajaj Finserv Health - Java Qualifier Assessment

This project is a **Spring Boot Automation Application** developed for the Bajaj Finserv Health hiring challenge (Qualifier 1). 

Instead of a standard web server, this application acts as an automated client that communicates with the hiring API, processes logic based on registration details, and submits a solution securely.

## 🚀 Project Overview

The application performs the following automated workflow "on startup" (using `CommandLineRunner`):

1.  **Authentication:** Sends a `POST` request to the hiring gateway with candidate details.
2.  **Token Handling:** Parses the JSON response to extract the `accessToken` (JWT) and the destination `webhook` URL.
3.  **Dynamic Logic:** * Analyzes the candidate's Registration Number (`22BCE1367`).
    * Identifies the last digit (7) as **ODD**.
    * Selects the solution for **Question 1** (SQL Challenge).
4.  **Submission:** Submits the final SQL query to the dynamic webhook URL with the required `Authorization` headers.

## 🛠️ Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.4.12
* **Build Tool:** Maven
* **Dependencies:** Spring Web (RestTemplate), Jackson (JSON Processing)

## 📄 Solution Logic (Question 1)

**Scenario:** The logic detects an **ODD** registration number and solves the following SQL problem:
> *Find the highest salary credited to an employee (excluding payments made on the 1st of the month), along with their Name, Age, and Department.*

**SQL Query Used:**
```sql
SELECT 
    p.AMOUNT AS SALARY, 
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, 
    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, 
    d.DEPARTMENT_NAME 
FROM PAYMENTS p 
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID 
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID 
WHERE DAY(p.PAYMENT_TIME) <> 1 
ORDER BY p.AMOUNT DESC 
LIMIT 1
