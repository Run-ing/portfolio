#!/usr/bin/env sh
set -eu

ROOT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
ENV_FILE="${ENV_FILE:-$ROOT_DIR/deploy/env}"
ENV_EXAMPLE_FILE="$ROOT_DIR/deploy/env.example"

fail() {
  printf 'ERROR: %s\n' "$1" >&2
  exit 1
}

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || fail "$1 is required but was not found in PATH"
}

read_env_value() {
  key="$1"
  value="$(
    awk -F= -v key="$key" '
      $0 !~ /^[[:space:]]*#/ && $1 == key {
        sub(/^[^=]*=/, "", $0)
        gsub(/^[[:space:]]+|[[:space:]]+$/, "", $0)
        gsub(/^"|"$/, "", $0)
        gsub(/^'\''|'\''$/, "", $0)
        print $0
      }
    ' "$ENV_FILE" | tail -n 1
  )"
  [ -n "$value" ] || fail "$key is missing or empty in $ENV_FILE"
  printf '%s' "$value"
}

require_file() {
  [ -f "$1" ] || fail "Required file not found: $1"
}

require_env_file() {
  if [ -f "$ENV_FILE" ]; then
    return
  fi

  if [ -f "$ENV_EXAMPLE_FILE" ]; then
    fail "Required file not found: $ENV_FILE. Copy deploy/env.example to deploy/env and adjust values before running this script."
  fi

  fail "Required file not found: $ENV_FILE"
}

require_cmd docker
docker compose version >/dev/null 2>&1 || fail "Docker Compose v2 is required: docker compose version failed"
require_env_file
require_file "$ROOT_DIR/deploy/images/backend/Dockerfile"
require_file "$ROOT_DIR/deploy/images/frontend/Dockerfile"
require_file "$ROOT_DIR/deploy/artifacts/backend/app.jar"
require_file "$ROOT_DIR/deploy/artifacts/frontend/dist/index.html"

BACKEND_IMAGE="$(read_env_value BACKEND_IMAGE)"
FRONTEND_IMAGE="$(read_env_value FRONTEND_IMAGE)"

printf 'Building backend image: %s\n' "$BACKEND_IMAGE"
docker build -t "$BACKEND_IMAGE" -f "$ROOT_DIR/deploy/images/backend/Dockerfile" "$ROOT_DIR/deploy"

printf 'Building frontend image: %s\n' "$FRONTEND_IMAGE"
docker build -t "$FRONTEND_IMAGE" -f "$ROOT_DIR/deploy/images/frontend/Dockerfile" "$ROOT_DIR/deploy"

printf '\nImages built successfully.\n'
printf 'Start or update services with:\n'
printf '  docker compose --env-file deploy/env -f deploy/docker-compose.yml up -d\n'
