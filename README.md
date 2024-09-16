# Prueba tecnica

Este proyecto contiene dos microservicios desarrollados con **Spring Boot** que interactúan con bases de datos PostgreSQL y MySQL, respectivamente. Los microservicios se ejecutan en contenedores Docker para facilitar su despliegue y gestión.

## Requisitos previos

Antes de iniciar, asegúrate de tener las siguientes herramientas instaladas:

- **Docker** (versión 20.10 o superior)
- **Java 21** (o superior)
- **Maven 3.8.6** (si compilas localmente)
- **Spring Boot 3.3.3**

## Tecnologías utilizadas

- **Spring Boot**: 3.3.3
- **Java**: 21
- **PostgreSQL**: 12-alpine
- **MySQL**: 8
- **Docker**: para contenerización y despliegue

---

## Instrucciones de despliegue

### 1. Crear una red en Docker

Antes de desplegar los contenedores, debes crear una red Docker que permita la comunicación entre los diferentes contenedores de los microservicios y las bases de datos.

docker network create net-ms1

### 2. Crear el contenedor de PostgreSQL

El microservicio ms-customer utiliza una base de datos PostgreSQL. Para crear el contenedor de PostgreSQL, ejecuta el siguiente comando:

docker run -p 5432:5432 --name db-postgres --network net-ms1 -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=db_customer -d postgres:12-alpine

### 3. Crear el contenedor de MySQL
El microservicio ms-account utiliza una base de datos MySQL. Para crear el contenedor de MySQL, ejecuta el siguiente comando:

docker run -p 3306:3306 --name bd-mysql --network net-ms1 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=db_account -d mysql:8

## Despliegue de los microservicios

### 4. Compilar el microservicio ms-account

#### 1. Primero, compila el código fuente del microservicio ms-account utilizando Maven:
./mvnw clean package -DskipTests

#### 2. Luego, construye la imagen Docker del microservicio ms-account:
docker build -t ms-account:1.0.0 .

#### 3. Finalmente, levanta el contenedor de ms-account en Docker:
docker run -p 8082:8082 --name ms-account --network net-ms1 -d ms-account:1.0.0

### 5. Compilar el microservicio ms-customer

#### 1. Primero, compila el código fuente del microservicio ms-customer utilizando Maven:
./mvnw clean package -DskipTests

#### 2. Luego, construye la imagen Docker del microservicio ms-customer:
docker build -t ms-customer:1.0.0 .

#### 3. Finalmente, levanta el contenedor de ms-customer en Docker:
docker run -p 8080:8080 --name ms-customer --network net-ms1 -d ms-customer:1.0.0

## Acceso a los Microservicios
Una vez que ambos microservicios estén en ejecución, puedes acceder a ellos en los siguientes puertos:

#### ms-account: http://localhost:8082
#### ms-customer: http://localhost:8080
