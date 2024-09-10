# KeyCloak integration

## Prerequisites

### Install KeyCloak on Docker

`docker pull quay.io/keycloak/keycloak:25.0.0`

Run keyCloak on port 8180

`docker run -d --name keycloak-25.0.0 -p 8180:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:25.0.0 start-dev`

### MongoDb
`docker pull bitnami/mongodb:7.0.11`
`docker run -d --name mongodb-7.0.11 -p 27017:27017 -e MONGODB_ROOT_USER=root -e MONGODB_ROOT_PASSWORD=root bitnami/mongodb:7.0.11`

### Web-app 

`npm start`