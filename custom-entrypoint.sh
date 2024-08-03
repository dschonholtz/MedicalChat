#!/bin/bash
set -e

# Check if the tables exist
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "\dt" | grep -q "papers" || psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /docker-entrypoint-initdb.d/schema.sql

# Execute the original entrypoint script
exec docker-entrypoint.sh "$@"