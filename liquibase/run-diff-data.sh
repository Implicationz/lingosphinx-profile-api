#!/bin/bash
set -e

# 📍 Move to the script's directory (ensures relative paths work)
cd "$(dirname "$0")"

# 🔐 Load environment variables from .env
set -o allexport
source .env
set +o allexport

# 📝 Generate changelog from local (updated) to Neon (prod) schema
liquibase \
  --changeLogFile=changelog/generated-data.xml \
  --url="$LOCAL_DB_URL" \
  --username="$LOCAL_DB_USER" \
  --password="$LOCAL_DB_PASS" \
  generateChangeLog \
  --diffTypes=data
