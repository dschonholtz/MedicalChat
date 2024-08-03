# Use the official PostgreSQL image as the base image
FROM postgres:15

# Install build dependencies
RUN apt-get update && apt-get install -y \
    build-essential \
    git \
    postgresql-server-dev-15

# Clone and install pgvector
RUN git clone --branch v0.4.4 https://github.com/ankane/pgvector.git \
    && cd pgvector \
    && make \
    && make install

# Clean up
RUN apt-get remove -y build-essential git postgresql-server-dev-15 \
    && apt-get autoremove -y \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /pgvector

# Set environment variable to enable pgvector
ENV POSTGRES_DB=vectordb
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=mysecretpassword

# Add SQL scripts to create extension and schema
COPY ./init.sql /docker-entrypoint-initdb.d/
COPY ./schema.sql /docker-entrypoint-initdb.d/

# Expose the PostgreSQL port
EXPOSE 5432

# Custom entrypoint to run schema.sql if tables do not exist
COPY ./custom-entrypoint.sh /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/custom-entrypoint.sh