# Activage Deployment Tools - Deployment manager

## Introduction
The deployment manager tool provides a graphical interface through which the deployer can create/edit a specific deployment installation and have an overview of the deployment inventory, along with its installed devices and services.

Through the deployment manager, the deployer can edit/view the component installation characteristics, such as the locations where devices are installed, their current operational status, etc.

## Architecture
The tool presented is an HTTP REST server that consumes data from a metadata storage to provide values to the deployment tools frontend hosted in Component Configurator tool
.
- The HTTP REST server has been developed in Java.

The application requires a metadata storage server deployed.

## Build and deployment

### Prerequisites:
- Install Docker

````
sudo apt-get install docker-ce
````
- Install Docker Compose

````
sudo curl -L "https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
````
- Install Maven

````
sudo apt-get install maven
````

### Build

#### Build the tool using maven
This tool uses maven so for its use it should be placed in the root directory of the project and execute:
```bash
$ mvn clean install
```

#### Build docker image
Assuming you are in the project root folder and the project has been compiled using maven. The docker image of the tool can be built executing:
```bash
$ docker build -t activage-deployment-manager:develop .
```
In the previous code example we can see the image is created using *activage-deployment-manager* as the image name and *develop*
as the tag. The final dot is necessary for the detection of the Dockerfile file in the directory.

#### Run the tool using the tomcat embedded-server
The tool can be run using the maven tomcat plugin. This plugin launches a tomcat embedded server in the port configured in pom.xml.
The plugin can be run executing:
```bash
$ mvn clean install tomcat7:run-war-only
```

### Deployment
The project rely on docker for development and production deployments. The following subsections explains in detail how to 
deploy the tool using docker technology and how to configure the tool with the required environmental variables or configuration files.

### Deploy docker-compose
Once built the docker image, the deployment manager can be run using docker-compose utility. For that purpose, a docker-compose file
including the configuration fields required is provided in the docker/deployment-manager/ directory. This file can be run executing:
````
docker-compose up
```` 
To stop the deployment, you must run in the same directory:
````
docker-compose down
````
### Deploy as service in docker-swarm
The next step is to start the services as a Docker Swarm. Assuming you are in the project root folder, you have to execute:
````
$ cd docker/deployment-manager/
$ docker stack deploy -c docker-compose.yml deployment-manager
````
To stop a Docker Stack we run
````
docker stack rm [service_name]
````

In addition, we can see the status of a Docker Stack:
````
docker stack ps [service_name]
````

In case you want to see the log of a service:
````
docker service logs [service_name]
````

### Docker environmental variables
To deploy the application using a docker, it's necessary to specify a environment variable:

- **METADATA_STORAGE_SERVER_URL**. The URL where metadata storage server is displayed. 

## Testing 
The tool provides tests ensuring the main operations are probed. To execute the tests you have to run:
```bash
$ mvn clean test
```
If some tests appear as ignored you have to delete the @Ignore annotation in the class or test inside the src/test/java/ directory.

For functional testing the tool provides an Postman collection where the developer can test the HTTP REST methods. 

## License
```bash
Copyright 2020 HOPU

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
