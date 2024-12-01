#!/bin/bash

# Variables
DOCKER_REPO="badzim/codelands"

VERSION=${1:-"0.0"} # Utilise le paramètre passé ou "latest" par défaut

echo "=================================================="
echo "🚀 Starting Deployment Script for Version $VERSION"
echo "=================================================="

echo "🛠️  Building JAR..."
mvn clean package

# Construire et pousser les images
echo "🛠️  Building API image version $VERSION..."
docker build -t $DOCKER_REPO:dpr-api-v$VERSION -f ./docker/Dockerfile .

echo "🏷️  Tagging API image as latest..."
docker tag $DOCKER_REPO:dpr-api-v$VERSION $DOCKER_REPO:dpr-api-latest

echo "📤 Pushing API image version $VERSION..."
docker push $DOCKER_REPO:dpr-api-v$VERSION

echo "📤 Pushing API image as latest..."
docker push $DOCKER_REPO:dpr-api-latest

# Nettoyage
echo "🧹 Cleaning up local images..."
docker rmi $DOCKER_REPO:dpr-api-v$VERSION $DOCKER_REPO:dpr-api-latest || true

echo "🧹 Cleaning up local target..."
rm -rfd ./target

echo "=================================================="
echo "✅ Deployment Completed Successfully!"
echo "=================================================="
