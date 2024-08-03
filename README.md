# Under Construction!

# Medical Chat
THIS IS NOT MEDICAL ADVICE. IT SIMPLY ALLOWS YOU TO CHAT WITH RESEARCH PAPERS. 
Many of these papers have not been peer reviewed.

In hindsight, this probably shouldn't be called medical chat, as I probably will use it on agentic LLM research more than medical research.

But that is the beauty, you can use it for whatever you want.

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
    - [ ] Cosine similarity works with pgvector, but hybrid lex search does not
- [ ] Chatting with data

## Important files:

- `src/main/java/org.doug/db/PaperDAO.java` - This is the data access object for papers. It is the interface to the database. All search logic is in here.
- `src/main/java/org.doug/cli/FetchPapersCLI.java` - This is the command line interface to fetch papers. It stores all of the medRxiv papers to a local json file. This allows us to separate network requests from db management.
- `src/main/java/org.doug/cli/LoadPapersFromJsonCLI.java` - This is the command line interface to load papers into the database from the previously loaded json file.
- `src/main/java/org.doug/cli/SearchPapersCLI.java` - This allows you to play with search without a webserver. Just a DB.



How to start the true application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/MedicalChat-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
