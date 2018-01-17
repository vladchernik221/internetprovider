#Description

Project for internet providers. This is solution for registration tariff plans, services and discounts. You can create contract with new client or add annex to existing contracts. Also it's possible to register clients' transactions and account statuses, write-off and refill money in client's account.

#Deploy

For deploying this application it's needed to create database in MySQL. Properties for configuration access to database are stored in **"src/main/resources/application.properties"**. Flyway is used in the project, so scripts for initialization database schema will be applied automatically.

Also it's important to deploy project in Tomcat as **"ROOT"**.