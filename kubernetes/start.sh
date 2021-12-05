#!/usr/bin/sh

kubectl apply -f kubernetes/jenkins.secret.yaml
kubectl apply -f kubernetes/dind.service.yaml
kubectl apply -f kubernetes/jenkins.service.yaml
kubectl apply -f kubernetes/jenkins.deployment.yaml
kubectl apply -f kubernetes/dind.deployment.yaml