#!/bin/bash

# Variables
DOCKER_REPO="badzim/codelands"
APP_NAME="dpr-server" # Nom de votre application
VERSION=${1:-"0.0"} # Utilise le paramètre passé ou "latest" par défaut

echo "=================================================="
echo "🚀 Starting Deployment Script for Version $VERSION"
echo "=================================================="

echo "🛠️  Building JAR..."
mvn clean package

# Construire et pousser les images
echo "🛠️  Building API image version $VERSION..."
docker build -t $DOCKER_REPO:$APP_NAME-v$VERSION -f ./docker/Dockerfile .

echo "🏷️  Tagging API image as latest..."
docker tag $DOCKER_REPO:$APP_NAME-v$VERSION $DOCKER_REPO:$APP_NAME-latest

echo "📤 Pushing API image version $VERSION..."
docker push $DOCKER_REPO:$APP_NAME-v$VERSION

echo "📤 Pushing API image as latest..."
docker push $DOCKER_REPO:$APP_NAME-latest

# Nettoyage
echo "🧹 Cleaning up local images..."
docker rmi $DOCKER_REPO:$APP_NAME-v$VERSION $DOCKER_REPO:$APP_NAME-latest || true

echo "🧹 Cleaning up local target..."
rm -rfd ./target

echo "=================================================="
echo "✅ Deployment Completed Successfully!"
echo "=================================================="
