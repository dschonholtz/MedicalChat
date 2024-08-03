# Medical Chat
THIS IS NOT MEDICAL ADVICE. IT SIMPLY ALLOWS YOU TO CHAT WITH RESEARCH PAPERS. 
Many of these papers have not been peer reviewed.

In hindsight, this probably shouldn't be called medical chat, as I probably will use it on agentic LLM research more than medical research.

But that is the beauty, you can use it for whatever you want.

![ResearchSearcher](./ResearchSearcher.gif)

## Purpose

This project aims to demonstrate the following:

1. **Hybrid Search with pgvector and Postgres**: Learn how to implement hybrid search capabilities using pgvector and Postgres.
2. **Using Dropwizard**: Understand how to use Dropwizard for building RESTful web services.
3. **Connecting to a React App**: Learn how to connect the backend services to a React frontend application.


# Getting Started

## Pull down papers

```bash
# I have just been letting intelliJ handle this, so adjust params/flags as needed.
java -cp target/MedicalChat-1.0-SNAPSHOT.jar org.doug.cli.FetchPapersCLI
```

## Start the DB

```bash
docker build -t pgvector . -f pgvector.Dockerfile
docker run -d --name pgvector -p 5432:5432 -v pgvector_data:/var/lib/postgresql/data pgvector
```

## Load papers into DB

I just use IntelliJ so this might not be quite right.

```bash
java -cp target/MedicalChat-1.0-SNAPSHOT.jar org.doug.cli.LoadPapersFromJsonCLI
```

## Start the server

```bash
java -jar target/MedicalChat-1.0-SNAPSHOT.jar server config.yml
```

## Start the frontend

```bash
cd frontend
npm install
npm start
```

## Description

A comprehensive tool suite to pull down current medical research papers, store them in a database, and search over them.

Then we'll use LLMs to chat with that searchable index.


This also is an excuse to re-learn or learn for the first time the following:

- [X] Dropwizard
- [X] Java
- [X] Maven
- [X] Pgvector
- [X] Postgres
- [X] Loaded data into db
- [X] Searching over data given query
    - [X] Cosine similarity + postgres text search. I still don't understand how text search works in postgres super well yet, but it's working
- [ ] Chatting with data

## Important files:

- [`src/main/java/org.doug/db/PaperDAO.java`](src/main/java/org/doug/db/PaperDAO.java) - This is the data access object for papers. It is the interface to the database. All search logic is in here.
- [`src/main/java/org.doug/cli/FetchPapersCLI.java`](src/main/java/org/doug/cli/FetchPapersCLI.java) - This is the command line interface to fetch papers. It stores all of the medRxiv papers to a local json file. This allows us to separate network requests from db management.
- [`src/main/java/org.doug/cli/LoadPapersFromJsonCLI.java`](src/main/java/org/doug/cli/LoadPapersFromJsonCLI.java) - This is the command line interface to load papers into the database from the previously loaded json file.
- [`src/main/java/org.doug/cli/SearchPapersCLI.java`](src/main/java/org/doug/cli/SearchPapersCLI.java) - This allows you to play with search without a webserver. Just a DB.


How to start the true application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/MedicalChat-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`


# Future Work!
- I have dummy postgres creds everywhere, those should be env variables and defined in a single location
- I originaly wanted to use localstack and AWS services. This wasn't worth the hastle, it might be if you want to deploy this.
- Setting up LLMs to actually ask questions about these papers is likely worth doing.
- Further testing! This was all built in a couple days so it is poorly tested.
- More paper sources! There are around 100 papers in the db right now, but there are many more to be had. These are only sports science papers from medRxiv. Presumably, you could bring everything down from bioRxiv and MedRxiv and Arxiv. Then search conditionally by section. Maybe having an LLM do that choice for you.
- Health checks! I do not have any health check endpoints set up. I decided I would be ok without implementing that dropwizard feature.
- Less reliance on local dev.
    - Containerizing the dropwizard app
    - Env variables
    - Deployment scripts etc
    - CI/CD
    - Easier initial DB set up