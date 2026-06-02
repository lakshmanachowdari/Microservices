# 🎓 Online Assessment Platform (Microservices)

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-316192?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

A robust, distributed, and scalable **Online Assessment Platform** built using a Microservices Architecture. The platform provides a seamless ecosystem where **Professors** can register courses and curate assessment questions categorized by difficulty levels, while **Students** can enroll in courses, attempt assessments, and receive instant feedback on their level of understanding.

---

## 🏗️ Core Architecture Overview

The system is split into specialized, loosely coupled services managed by an API Gateway and discovered via a central Service Registry:

* **`service-registry`**: Discovery server (Netflix Eureka) ensuring dynamic routing and high availability.
* **`api-gateway`**: Single entry point handling request routing, security, and load distribution.
* **`quiz-service`**: Handles quiz creation, orchestration, assessment tracking, and final evaluation logic.
* **`question-service`**: Manages the question bank, categorizing assessment queries by courses and proficiency levels.

---

## 🛠️ Tech Stack & Dependencies

### Prerequisites
Ensure you have the following software installed locally before running the application:
* **Java Development Kit (JDK 17 or higher)**
* **Spring Boot & Spring Cloud Frameworks**
* **Apache Maven** (Dependency Management & Build Tool)
* **Git** (Version Control)
* **PostgreSQL** (Relational Database Server)

### Recommended Developer Tools
* **IDE:** IntelliJ IDEA
* **Database Management:** DBeaver
* **API Testing:** Postman

---

## ⚙️ Getting Started & Installation

### 1. Clone the Repository
```bash
git clone [https://github.com/lakshmanachowdari/Microservices.git](https://github.com/lakshmanachowdari/Microservices.git)
cd Microservices
