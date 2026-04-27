#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCHEMA_FILE="$ROOT_DIR/src/main/resources/db/schema.sql"
DATA_FILE="$ROOT_DIR/src/main/resources/db/data.sql"

DB_HOST="${DB_HOST:-0.0.0.0}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-library}"
DB_USER="${DB_USER:-admin}"
DB_PASSWORD="${DB_PASSWORD:-turkcell-gygy-admin}"

if ! command -v psql >/dev/null 2>&1; then
  echo "psql command not found. Please install PostgreSQL client tools."
  exit 1
fi

export PGPASSWORD="$DB_PASSWORD"

echo "Applying schema: $SCHEMA_FILE"
psql -v ON_ERROR_STOP=1 -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$SCHEMA_FILE"

echo "Applying seed data: $DATA_FILE"
psql -v ON_ERROR_STOP=1 -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$DATA_FILE"

echo "Initialization completed successfully."
