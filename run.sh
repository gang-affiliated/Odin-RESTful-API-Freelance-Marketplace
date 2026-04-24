#!/usr/bin/env bash
set -euo pipefail

PROFILE="${1:-postgres}"

if ! command -v javac >/dev/null 2>&1; then
  echo "JDK not found. Please install JDK 17+ (javac missing)."
  exit 1
fi

JAVAC_PATH="$(command -v javac)"
JAVA_HOME="$(cd "$(dirname "$JAVAC_PATH")/.." && pwd)"

export JAVA_HOME
export PATH="$JAVA_HOME/bin:$PATH"

echo "Using JAVA_HOME=$JAVA_HOME"
echo "Starting Spring Boot with profile '$PROFILE'..."

./mvnw spring-boot:run -Dspring-boot.run.profiles="$PROFILE"
