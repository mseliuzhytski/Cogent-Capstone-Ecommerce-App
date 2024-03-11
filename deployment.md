# Deployment

Here is a link to the [Deployed App](http://54.85.70.2/)

## 1. Setup the database server
We use AWS RDS to host our databases. We have two RDS instances, one is for testing and the other is for production. Edit the `application.properties` in all three microservices to properly set up the RDS instace.

### Free Tier (micro)
```
spring.datasource.url=jdbc:mysql://mydatabase.cbm6usqq06k1.us-east-2.rds.amazonaws.com:3306/dhm67  
spring.datasource.username=admin  
spring.datasource.password=Koch2357
```
### Production (large)
```
spring.datasource.url=jdbc:database-production-instance-1.cbm6usqq06k1.us-east-2.rds.amazonaws.com:3306/dhm67  
spring.datasource.username=admin  
spring.datasource.password=Koch2357
```

### Initializing Database
To clear the database contents and recreate the schema, within the main app microservice, change the following property in `application.properties` and run the application (you can do this locally)
`` spring.sql.init.mode=never ``
Remember to change the property back to the following to prevent successive application restarts from clearing the database:
`` spring.sql.init.mode=always ``

Optionally, if you want to prepopulate the database with sample products, an administrative and normal user (details are specified in the `application.properties` file), run the following POST request:
``POST localhost:8080/databaseloader``

## 2. Updating application code
We use Docker images to track the application code for our different microservices. Below are a table of the Docker images for each of the microservices:
| Microservice   |      DockerHub Image |    
|----------|:-------------:|
| Service Registry |  chrisgioia64/ecommerce-service-registry:1.0.0 |
| API Gateway |    chrisgioia64/ecommerce-gateway:1.0.0   |
| Main | chrisgioia64/ecommerce-main:1.0.0 |
| Sales Report | chrisgioia64/ecommerce-sales:1.0.0 |
| Discount | chrisgioia64/ecommerce-discount:1.0.0 |

After working on the source code locally, from within the project directory, you can push a Docker image to DockerHub by running:
1. `mvn clean install`
2. `docker build -t [imageName] .` (substitute image name with Docker Hub image above)
3. `docker push [imageName]`

## 3. Running the backend services
We are currently using this EC2 instance (18.116.53.160) to host all our microservices. In the future, we might use a Kubernetes cluster on Google Kubernetes Engine to provide for better scalability.

1. Login to the EC2 instance.
2. Make sure that the most up-to-date `docker-compose.yaml` from the [Main app](https://github.com/mseliuzhytski/Cogent-Capstone-Ecommerce-Service-Registry) is located in the home directory.
3. Stop all docker images: ``sudo docker-compose stop``
4. Pull the latest docker images from DockerHub:  ``sudo docker-compose pull``
5. Re-run the docker images: `` sudo docker-compose up``

## 4. Running the frontend

1. Open the frontend source repository on your local machine. Open the file `environment.production.ts` and change the `url`, `sales_url`, and `discount_url` to point to the backend service (in this case 18.116.53.160) with the correct port. The port for `url` (which corresponds to the main microservice) should normally be `8080` and the ports for `sales_url` and `discount_url` should route through the API gateway which is `9005`. Below is a sample configuration. Note that this file may only need to be changed if the backend service URL changes (i.e. you use a different EC2 instance or change over to using Google Kubernetes Engine).
```
export  const  environment  = {
	environmentName:  "Production",
	url:  "http://18.116.53.160:8080/",
	sales_url:  "http://18.116.53.160:9005/",
	discount_url:  "http://18.116.53.160:9005/"
};
```
2. Run the following command to build the Angular project using the production environment.
```
ng build --configuration=production
```
4. Copy the source code from the directory `ecommerce-prototype/dist/ecommerce-prototype/browser` into the web home directory of your EC2 instance.
5. Set up a server to host the files. Nginx is a good choice. See this [tutorial](https://www.youtube.com/watch?v=7jyzbY5iKKY&ab_channel=TheQuickDesk) on details.
