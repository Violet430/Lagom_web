# lagom-template
Building Reactive Java 8 application with Lagom framework. This is a classic CRUD application which persist events in Cassandra Db. Here we are using embedded Cassandra to persist events and embedded kafka for publishing and subscribing between microservices.

# Prerequisites
1. Java 1.8
2. Maven 4.0

# Getting the Project
https://github.com/knoldus/lagom-java-crud.git

####Create executable jar: 
`mvn package -Dmaven.skip.test=true`

####Command to start the project

`mvn lagom:runAll`

## Json Formats for different Rest services are mentioned below :

#### 1. Create Movie:

Route(Method - POST) : `localhost:9000/api/new-movie`

Rawdata(json): 
    {
	"id": "1",
	"name": "Avengers",
	"genre": "Action"
    }


#### 2. Update Movie:

Route(Method - PUT) : `localhost:9000/api/update-movie/:id`
    

#### 3. Delete Movie:

Route(Method - DELETE) : `localhost:9000/api/delete-movie/:id`
    

#### 4. Get Movie details:

Route(Method - GET) : `localhost:9000/api/movie/:id`

