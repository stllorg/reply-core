#!/bin/bash

# Build runner
 ./mvnw package -Pnative -Dquarkus.native.container-build=true

# Build docker container
docker build -f src/main/docker/Dockerfile.native -t stllorg/reply-core .