# Blog Backend

## Project Overview

This blog backend system provides secure user authentication, role-based authorization, and CRUD operations for users, posts, and comments. It is deployed on **AWS Elastic Beanstalk** for scalable application hosting and uses **Amazon RDS** for database management. It utilizes JWT (JSON Web Token) for secure authentication and role-based access control (RBAC) to restrict or allow access to resources based on the user's role.

## Features

- **User Authentication:** Users can register, log in, and log out securely using JWT.
- **Role-Based Access Control:** Users have roles (Admin, Super Admin, Normal User, Viewer) with specific permissions.
- **CRUD Operations:**
  - Users can perform CRUD operations on their own posts and comments.
  - Admins and Super Admins have the ability to manage all posts and comments.
- **Post and Comment System:** Allows users to create, update, delete, and view posts and comments.
- **Deployed Infrastructure:**
  - Hosted on **AWS Elastic Beanstalk** for the backend application.
  - Database managed on **Amazon RDS**, ensuring reliability and performance.
  
## Tech Stack

- **Backend:** Spring Boot (Java)
- **Database:** MySQL
- **Authentication:** JWT (JSON Web Token)
- **Security:** Spring Security
- **API Documentation:** Swagger

## Plan for Role-Based Operations

1. **Admin and Super Admin:**
   - Responsible for assigning roles to users. 
   - **Super Admin** can assign **Admin**, **Normal**, or **Viewer** roles. 
   - **Admin** can assign only **Normal** and **Viewer** roles.
   - Both Admins and Super Admins can delete any post or comment and view all posts and comments.

2. **Normal User:**
   - Can perform CRUD operations only on posts and comments they have created.
   - Can view all posts and comments.

3. **Viewer:**
   - Can only view posts and comments (GET methods). No permission for CRUD operations.
<br></br>
## Screenshots:

Check out this docs to see screenshots of Role-Based Access Control in action, along with other key functionalities.
https://docs.google.com/document/d/1T_nihtBvzPMjQdmnpDDkCgVwzG_AzV9wmco2tvInM-Y/edit?usp=sharing

  
<br></br>
## Deployed Application
The application has been deployed on **AWS Elastic Beanstalk** and **Amazon RDS**.  

- **Base URL**: [blog-prod.eu-north-1.elasticbeanstalk.com](http://blog-prod.eu-north-1.elasticbeanstalk.com)
- **Swagger UI**: [Blog Swagger API Documentation](http://blog-prod.eu-north-1.elasticbeanstalk.com/swagger-ui/index.html)

### Authentication Workflow for first time Users
1. **Register**:
   - Send a `POST` request to the API: [Register API](http://blog-prod.eu-north-1.elasticbeanstalk.com/api/v1/auth/register)  
     Provide valid user details in the request body.
2. **Jwt token generation**:
   - Send a `POST` request to the API: [Login API](http://blog-prod.eu-north-1.elasticbeanstalk.com/api/v1/auth/login)  
     Provide valid credentials (username and password).
   - Use the returned JWT token in the `Authorization` header as `Bearer <token>` to access other secured endpoints.
  <br></br>

## How to Run Locally

1. **Clone the Repository:**

   ```bash
   git clone <repository-url>
   cd <project-directory>
   ```

2. **Set up MySQL:**
- Create a MySQL database named blog_db.
- Configure your application.properties file with your MySQL credentials:
Update the following properties in src/main/resources/application.properties:


```bash
spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
- Replace <your-username> and <your-password> with your actual MySQL credentials.
  <br></br>

  
3. **Run the Application:**
After setting up the database and configuring the application.properties, run the Spring Boot application with the following command:

```bash
./mvnw spring-boot:run
```
This will start the application on the default port 8080.
   <br></br>


4. **Access Swagger UI:**
Once the application is running, open your browser and go to:

```bash
http://localhost:8080/swagger-ui/index.html
```
Here, you can explore the API endpoints.
   <br></br>


5. **Login and Authentication:**
To get the JWT token, send a POST request to:
```bash
http://localhost:8080/api/v1/auth/login
```

- Provide valid credentials in the request body (e.g., username and password).
- Once you receive the JWT token, use it in the Authorization header as Bearer <token> for protected endpoints.
     <br></br>

## Developed By Nandini :)
  
