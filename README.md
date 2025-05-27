# Voting App

A simple distributed voting application orchestrated with Docker containers.

This project is inspired by [Docker samples](https://github.com/dockersamples/example-voting-app) and enhanced with Spring Boot and Kafka integration. The application enables users to vote between two options and demonstrates a microservices architecture.

## Demo
![demonstration](https://github.com/devsteppe9/voting_app/blob/main/demo.gif)

## Getting started

Download [Docker Desktop](https://www.docker.com/products/docker-desktop) for Mac or Windows. [Docker Compose](https://docs.docker.com/compose) will be automatically installed. On Linux, make sure you have the latest version of [Compose](https://docs.docker.com/compose/install/).

This solution uses Spring Boot, Node.js with Kafka for messaging and Postgres for storage.

Run in this directory to build and run the app:

```shell
docker compose up
```

The `vote` app will be running at [http://localhost:8080](http://localhost:8080), and the `results` will be at [http://localhost:8081/results/{session_id}](http://localhost:8081/results/1). Replace `{session_id}` in the URL with the ID of the voting session you want to see results for (e.g., `http://localhost:8081/results/1` for session ID 1).

## Run the app in Kubernetes

The folder k8s-specifications contains the YAML specifications of the Voting App's services.

Run the following command to create the deployments and services. Note it will create these resources in your current namespace (`default` if you haven't changed it.)

```shell
kubectl create -f k8s-specifications/
```

The `vote` web app is then available on port 31000 on each host of the cluster, the `result` web app is available on port 31001.

To remove them, run:

```shell
kubectl delete -f k8s-specifications/
```

## Architecture

![Architecture diagram](architecture.excalidraw.png)

* A front-end web app [Spring Boot/Thymeleaf](/vote) for submitting votes and lets you create multiple voting sessions
* A [Kafka](https://hub.docker.com/_/redis/) message broker for handling vote events
* A [Spring Boot](/worker/) worker service to process and persist votes in a Postgres database.
* A [Spring Boot](/vote-session) REST API for managing voting sessions
* A [Postgres](https://hub.docker.com/_/postgres/) database 
* A [Node.js](/result) web app for displaying real-time results

## Notes

The voting application only accepts one vote per client browser. It does not register additional votes if a vote has already been submitted from a client.
