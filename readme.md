# Rent data management

- This is a project while I learn Spring boot (v2)
- Tech:
    - Spring boot
    - Spring security
    - Spring mail
    - Maven
    - JWT (Json Web Token)
    - Docker (Run database)
    - Swagger (Spring doc)
- IDE:
    - IntelliJ IDEA

# Features:

- Authentication, Authorization
- CRUD
- Validator (not null, format, check duplicate)
- Import/Export (CSV)
- Paging
- Searching

# Updates:
- [2024.10.31]
  - Update Selenium test `Login Page` and `Change Password Page`
- [2024.10.29] 
  - Update init **Role** & **first user** in database
  - [Repo Frontend](https://github.com/Bie-NHD/Rent-management-dashboard)

# How to run

- Clone this repository
- Make sure you are using JDK 17
- Install extension [Lombok](https://projectlombok.org/) in IntelliJ IDEA

### 1. Update `.env` (Optional - if you need to send mail)

- Rename `.env.example` in `resources` folder to `.env.dev` and change your value to set up mail
  ```
  EMAIL_USERNAME=<Your email address>
  EMAIL_PASSWORD=<Your email's password>
  ```
- Install plugin [EnvFile](https://plugins.jetbrains.com/plugin/7861-envfile) and do like
  document or setting like that in `Edit configuration` of
  project ![img.png](img/setting-envfile.png)

### 2. Initial role entity

- Just run and do not care about that
![initial-role-user.png](img%2Finitial-role-user.png)

### 3. Set up Database with docker

- Run 2 command under

```docker
docker image pull mysql:8.3.0
```

```docker
docker run --name k1ethoang-mysql-container -d -e MYSQL_ROOT_PASSWORD=123456 -p 6603:3306 mysql:8.3.0
```

### 4. Set up MySQL

- Create connection (Do not change):
    - Username: root
    - Password: 123456

  ![img.png](img/create-connection.png)

- Create database in MySQL name: `Learn_BackEnd` or use script `create database Learn_BackEnd`
  ![img.png](img/create-database.png)

### 5. Run project

- Run your docker first, you can run
  via [Docker Desktop](https://www.docker.com/products/docker-desktop/)
  ![img.png](img/run-docker.png)

- Run command in your project folder

```
./mvnw spring-boot:run
```

- Access http://localhost:9090/swagger-ui/index.html to see swagger doc
