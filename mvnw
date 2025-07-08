#!/usr/bin/env bash
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Maven Start Up Wrapper script
#
# Required ENV vars:
#   JAVA_HOME - location of a JDK home dir
# Optional ENV vars:
#   M2_HOME - location of Maven's installed home dir
#   MAVEN_OPTS - JVM args for Maven
#   MAVEN_SKIP_RC - disable loading of mavenrc files
# ----------------------------------------------------------------------------

set -euo pipefail

if [[ -z "${MAVEN_SKIP_RC:-}" ]]; then
  for rc in /usr/local/etc/mavenrc /etc/mavenrc "$HOME/.mavenrc"; do
    [[ -f "$rc" ]] && source "$rc"
  done
fi

cygwin=false
darwin=false
mingw=false
case "$(uname)" in
  CYGWIN*) cygwin=true ;;
  MINGW*) mingw=true ;;
  Darwin*) darwin=true ;;
esac

if [[ -z "${JAVA_HOME:-}" ]]; then
  if [[ "$darwin" == true && -x "/usr/libexec/java_home" ]]; then
    export JAVA_HOME="$(/usr/libexec/java_home)"
  elif [[ -r /etc/gentoo-release ]]; then
    JAVA_HOME="$(java-config --jre-home)"
  fi
fi

if [[ -z "${M2_HOME:-}" ]]; then
  PRG="$0"
  # Resolve symlinks
  while [[ -h "$PRG" ]]; do
    LS="$(ls -ld "$PRG")"
    LINK="${LS#*-> }"
    if [[ "$LINK" == /* ]]; then
      PRG="$LINK"
    else
      PRG="$(dirname "$PRG")/$LINK"
    fi
  done
  SAVED_DIR="$(pwd)"
  M2_HOME="$(cd "$(dirname "$PRG")/.." && pwd)"
  cd "$SAVED_DIR"
fi

# Convert to UNIX paths on Cygwin/Mingw
if [[ "$cygwin" == true ]]; then
  [[ -n "$M2_HOME" ]] && M2_HOME="$(cygpath --unix "$M2_HOME")"
  [[ -n "$JAVA_HOME" ]] && JAVA_HOME="$(cygpath --unix "$JAVA_HOME")"
fi
if [[ "$mingw" == true ]]; then
  [[ -n "$M2_HOME" ]] && M2_HOME="$(cd "$M2_HOME" && pwd)"
  [[ -n "$JAVA_HOME" ]] && JAVA_HOME="$(cd "$JAVA_HOME" && pwd)"
fi

# Locate Java executable and home
if [[ -z "${JAVA_HOME:-}" ]]; then
  JAVACMD="$(command -v java)"
  if [[ -n "$JAVACMD" ]]; then
    JAVA_HOME="$(dirname "$(dirname "$JAVACMD")")"
    export JAVA_HOME
  fi
else
  if [[ -x "$JAVA_HOME/jre/sh/java" ]]; then
    JAVACMD="$JAVA_HOME/jre/sh/java"
  else
    JAVACMD="$JAVA_HOME/bin/java"
  fi
fi

if [[ ! -x "${JAVACMD:-}" ]]; then
  echo "Error: Unable to find a usable Java executable." >&2
  exit 1
fi

CLASSWORLDS_LAUNCHER=org.codehaus.plexus.classworlds.launcher.Launcher

find_maven_basedir() {
  local dir="$1"
  while [[ "$dir" != "/" ]]; do
    [[ -d "$dir/.mvn" ]] && echo "$dir" && return
    dir="$(cd "$dir/.." && pwd)"
  done
  echo ""  # not found
}

concat_lines() {
  [[ -f "$1" ]] && tr -s '\n' ' ' < "$1"
}

BASE_DIR="$(find_maven_basedir "$(pwd)")"
[[ -z "$BASE_DIR" ]] && exit 1

# Ensure wrapper JAR
WRAPPER_DIR="$BASE_DIR/.mvn/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/maven-wrapper.jar"
if [[ ! -r "$WRAPPER_JAR" ]]; then
  echo "Downloading Maven Wrapper..."
  PROPS="$WRAPPER_DIR/maven-wrapper.properties"
  [[ -f "$PROPS" ]] && source <(grep -E '^wrapperUrl=' "$PROPS" | sed 's/wrapperUrl=//')
  URL="${wrapperUrl:-https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar}"
  mkdir -p "$WRAPPER_DIR"
  if command -v wget &>/dev/null; then
    wget -O "$WRAPPER_JAR" "$URL"
  elif command -v curl &>/dev/null; then
    curl -fsLo "$WRAPPER_JAR" "$URL"
  else
    java -cp "$WRAPPER_DIR" org.apache.maven.wrapper.MavenWrapperDownloader "$BASE_DIR"
  fi
fi

export MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$BASE_DIR}"
MAVEN_OPTS="$(concat_lines "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config") $MAVEN_OPTS"

exec "$JAVACMD" \
  $MAVEN_OPTS \
  -classpath "$WRAPPER_JAR" \
  "-Dmaven.home=${M2_HOME}" \
  "-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
