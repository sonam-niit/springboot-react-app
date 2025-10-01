# Application Deployment

This project demonstrates deployment of a full-stack application with
the following components:

-   **Frontend:** React (Vite)
-   **Backend:** Java (Spring Boot)
-   **Database:** MySQL

------------------------------------------------------------------------

## Backend Setup (Spring Boot)

### 1. Create `application-docker.properties`

Place this file inside the `src/main/resources` folder:

``` properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
```

### 2. Dockerfile for Backend

Create a `Dockerfile` in the backend directory:

``` dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /app/target/crudapi-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

------------------------------------------------------------------------

## Frontend Setup (React + Vite)

### 1. Update `vite.config.js`

Update the configuration to run the app on port **8100**:

``` javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,      // required for Docker
    port: 8100,      // change Vite port
  },
})
```

### 2. Dockerfile for Frontend

Create a `Dockerfile` in the frontend directory:

``` dockerfile
FROM node:20.19-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 8100

CMD ["npm", "run", "dev", "--", "--host"]
```

------------------------------------------------------------------------

## Docker Compose Setup

Create a `docker-compose.yml` file in the root directory:

``` yaml
services:
  mysql:
    image: mysql:8
    container_name: mysql-container
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: mydb
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - springboot-network

  springboot-app:
    build: ./backend
    container_name: springboot-container
    depends_on:
      - mysql
    environment:
      SPRING_PROFILES_ACTIVE: docker
      DB_URL: jdbc:mysql://mysql:3306/mydb
      DB_USER: user
      DB_PASS: password
    ports:
      - "8081:8081"
    networks:
      - springboot-network

  frontend:
    build: ./frontend
    container_name: react-container
    environment:
      - PORT=8100
      - VITE_API_URL=${VITE_API_URL}
    ports:
      - "8100:8100"
    networks:
      - springboot-network
    command: ["npm", "run", "dev", "--", "--host"]

volumes:
  mysql-data:

networks:
  springboot-network:
```

------------------------------------------------------------------------

## Running the Application

1.  Export the API URL for the frontend:

``` bash
export VITE_API_URL=http://springboot-app:8081/api
```

2.  Build and start all containers:

``` bash
docker-compose up -d --build
```

3.  Stop and remove containers:

``` bash
docker-compose down
```

------------------------------------------------------------------------

Your application should now be running with: - MySQL on **3306** -
Spring Boot backend on **8081** - React frontend on **8100**
