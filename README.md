# Under Construction!

# Medical Chat

## Description

A comprehensive tool suite to pull down current medical research papers, store them in a database, and search over them.

Then we'll use LLMs to chat with that searchable index.

An architecture diagram of the desired deployed arch will look like the below:
![Chat with Papers](ChatWPapers.png "Chat with Papers")

This also is an excuse to re-learn or learn for the first time the following:

- [X] Dropwizard
- [X] Java
- [X] Maven
- [ ] Pgvector
- [ ] Postgres text search with AWS RDBS
- [ ] AWS local development for the above
- [ ] AWS ECS Task Services
- [ ] Maybe Kafka? Seems like AWS queues will cover my needs here.

## Current Status

- [x] Pull down papers



How to start the true application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/MedicalChat-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
