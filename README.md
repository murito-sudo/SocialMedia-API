# SocialMedia-API
Social Media Backend API with multiple REST API endpoints capable of performing multiple functionalities.

## Technologies Used
- Spring Boot, JPA, MongoDB, MySQL, Docker, Spring Security.

# How to run the app
- Docker Installed and Running
- Java 17 or Higher

## How to run the app
- Clone this repository in your local machine.
- Once the project is cloned to your local machine. Go to the project directory folder and run these commands:
 
### Create an image and container for MySQL
```
  docker-compose -f docker-compose-mysql.yml up -d
```
### Create an image and container for Mongodb:
```
  docker-compose -f docker-compose-mongodb.yml up -d
```
- Finally, Run the program using your preferred IDE or by running the JAR file.
- NOTE: you can also run the spring boot app docker-compose file to create an image and container for the app:
### Create an image and container for the Spring Boot app:
```
  docker-compose up -d
```
- Before running the spring-boot app container, make sure both MySQL and Mongo containers are running.

# Usage
### Check the [Wiki](https://github.com/murito-sudo/SocialMedia-API/wiki) for the api documentation.

# Docker Images File
- [Spring Boot App Image](https://hub.docker.com/r/luise120/social_media_api).
- [MySQL Image](https://hub.docker.com/r/luise120/mysql).
- [MongoDB Image](https://hub.docker.com/r/luise120/mongo).




