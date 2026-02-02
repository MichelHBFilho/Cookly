# Cookly
Cookly is a social media app for people who want to share, or discover new recipes. This is the backend API that powers the platform, handling everything.

## SETUP
1. Clone the repository
2. Create a .env file with these variables: DB_USER, DB_PASSWORD, DB_ROOT_PASSWORD, JWT_SECRET.
3. Run ```docker compose up```
4. API will be available at http://localhost:8080

## API Documentation
This API is fully documented with **Swagger/OpenAPI**. Once the app is running go to the relative path: **swagger-ui/index.html#/**

## How It's Made:

**Tech used:** Java, Spring Boot, Spring Security, JWT, JUnit, Mockito, MySQL, Flyway, Docker

I wanted to build a complete backend system from scratch to really understand what I'm doing. The idea was to create something that could be used in production, instead of just a CRUD app. Therefore, I decided to create a basic social media app, with authorization, relationships between users and file uploads.

The first thing I did in the project was the authentication system, because, at first, it really scared me. Turns out that Spring Security is really simple if you know what you're doing. The security of the app is based on JWT tokens, that expire really fast, and refresh tokens that last a longer time.

With security implemented, I started to model the project. I created a bunch of classes to represent the data. While I was creating those classes, I also created Flyway migrations - which I learned how to use in this project. The part of modeling is always the funniest part, because you have to think about class relationships - many-to-many, one-to-many, many-to-one...

The upload system made me very proud, because, at first, it seemed impossible. However, as everything in this project, it turned out to be very simple if you design it right.

The entire project have appropriate errors, and error handling, which is very fun to do. This feature of Java is really good, and should be as easy in other languages.

I've tried to create tests throughout the project, so I learned a lot about how to make them work. During the coding, I noticed how tests are crucial, because they warn you, before you realize, that you've done something wrong. 

The main objective of the project was to learn more about Spring Boot, I believe I achieved it, I went from really basic knowledge to an intermediate level. It was a really fun project to code, and I think someday I'll update it.

## What I've Learned:
- Containerization with Docker
- Flyway migrations
- Unit tests with JUnit and Mockito
- OpenAPI documentation
- File uploads
- Spring security
- Password encryption
- Refresh Tokens
- Global exception handler