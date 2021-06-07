#!/usr/bin/env bash

registry=935581097791.dkr.ecr.eu-central-1.amazonaws.com
training="training-$1"
image=$registry/bookshelf:$training

echo "Building image $image"

aws eks --region eu-central-1 update-kubeconfig --name training-eks-cluster --role "arn:aws:iam::935581097791:role/EKSNamespace$1"
aws ecr get-login-password | docker login $registry --username AWS --password-stdin
docker build . --tag "$image"
docker push "$image"

kubectl patch --filename=deployment/deployment.yaml --patch='{"spec":{"template":{"spec":{"containers":[{"name":"bookshelf","image":"'"$image"'"}]}}}}' --local=true -o yaml > tmp.yaml
rm -rf deployment/deployment.yaml
mv tmp.yaml deployment/deployment.yaml
yq e '.spec.rules[0].http.paths[0].path = "/training-'"$1"'/(.*)"' -i  deployment/ingress.yaml
kubectl apply -n "$training" -f deployment/.