# Running your own Whanos instance

We're documenting 2 ways to setup your own Whanos instance

## Docker-Compose

### Prerequisites
 - Docker
 - Docker-compose
  

When cloning the Whanos repository, you will find a `docker-compose.yml` file 
**Note:** 
You have to set the environment variable `JENKINS_ADMIN_PASSWORD`, it will be the admin password 
You have to set the environment variable `JENKINS_DOCKER_REGISTRY`, it will be the base URL for docker push (e.g., for GCloud Artifact Registry : europe-west1-docker.pkg.dev/plucky-agency-332314)

Run
```bash
docker-compose up --build
``` 
You're done !

Your Jenkins instance is running on `localhost:8080`

## Kubernetes

