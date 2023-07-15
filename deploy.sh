#!/bin/sh
# Build the project
./gradlew clean build
wait

# Start the minikube
minikube start
wait

# Point the local Docker daemon to the minikube internal Docker registry
eval "$(minikube -p minikube docker-env)"

# Build the Docker image
docker image build -t smart-cart-server .
wait


# Deploy to Kubernetes locally
kubectl create -f server-deployment.yaml
wait

# Create a service from the Kubernetes pod
kubectl expose deployment smart-cart-server-deployment --type="NodePort" --port 8080
wait
# Kubernetes service must fully initialize.
sleep 1.5m

# Expose the service via ngrok
NODE_PORT="$(kubectl get services smart-cart-server-deployment -o go-template='{{(index .spec.ports 0).nodePort}}')"
export NODE_PORT
echo "NODE_PORT=$NODE_PORT"
ngrok http http://"$(minikube ip):$NODE_PORT"
