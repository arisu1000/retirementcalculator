#!/bin/sh
#
# Gradle start up script for UN*X
#
APP_HOME="$(cd "$(dirname "$0")" && pwd)"
JAVA_HOME="${JAVA_HOME:-}"

# Find java
if [ -z "$JAVA_HOME" ]; then
    JAVA_BIN="$(which java 2>/dev/null)"
else
    JAVA_BIN="$JAVA_HOME/bin/java"
fi

# Use the pre-downloaded gradle
GRADLE_HOME="/Users/wcjung/.gradle/wrapper/dists/gradle-8.2-bin/bbg7u40eoinfdyxsxr3z4i7ta/gradle-8.2"
exec "$GRADLE_HOME/bin/gradle" "$@"
