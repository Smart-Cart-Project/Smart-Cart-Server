#!/bin/sh
# Point the local Docker daemon to the minikube internal Docker registry
eval "$(minikube -p minikube docker-env)"

# Remove the current deployment
kubectl delete -f server-deployment.yaml
wait

# Remove the current service
kubectl delete services smart-cart-server-deployment
wait

# Delete the Docker image
docker rmi -f smart-cart-server
wait

# Stop the minikube
minikube stop
