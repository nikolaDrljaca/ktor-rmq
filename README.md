# Ktor Producer App

The easiest way to get `RabbitMQ` locally on machines is to pull the [docker image](https://hub.docker.com/_/rabbitmq).
There are two options:
- Base image: `docker pull rabbitmq:3`
- Base image with `Management plugin`: `docker pull rabbitmq:3-management`

The management plugin provides a web based dashboard to inspect queue information. Accessed at port `15672`.

Since we can expose docker ports and map them to `localhost` ports, in development terms our apps can locally interact with
the `RabbitMQ` framework, and later they can be deployed to their own containers.

## Modules 

We have two modules in this project: 
- Ktor server app: Takes in a `POST` request and pushes the message to the queue
- Basic kotlin application running on the JVM: Listens and consumes queue messages

## Run the project

1. Pull `RabbitMQ` image from dockerhub
2. Create local docker network `docker network create <network_name>`
3. Run `RabbitMQ` image `docker run -d --rm --net <network_name> --hostname <host_name> --name <container_name> <image_name>`
   1. `<host_name>` is **important** as our applications need to know this name for `ConnectionFactory` to work.
4. If downloaded base `RabbitMQ` image, enable `management` plugin
   ```
    # Run docker image
   docker run -d --rm --net <network_name> --hostname <host_name> --name <container_name> <image_name>
   
    # Access local container terminal
   docker exec -it <container_name> bash
   
    # Enable management plugin
   rabbitmq-plugins enable rabbitmq_management
    ```
5. Build and run docker image for Producer
    ```
    # Open terminal in project root folder
   docker build --no-cache --tag <name>:<tag> .
   
   # Run image in container - Make sure to assign same network
   docker run -d --rm --net <network_name> -p 80:80 -name <container_name> <name>:<tag>
   
   # Container exposes port 80, send POST request to localhost:80
    ```
6. Build and run docker image for Consumer
    ```
    # Open terminal in module root folder
    docker build --no-cache --tag <name>:<tag> .
    
    # Run image in container
    docker run -d --rm --net <network_name> -name <container_name> <name>:<tag>
    
    # This app doesn't expose ports, it just reads queue messages and prints them
    # To access the app logs
    docker logs --follow <container_name>
    ```
   
**Note**: `hostname` can be passed in as an environment variable to containers, meaning the `hostname` does not have to
be hardcoded. It can be passed in while creating the container:
```
docker run -it --rm -e NAME=<value> -e NAME_2=<value_2> image:tag 
```