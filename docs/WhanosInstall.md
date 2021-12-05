
# Running your own Whanos instance

We're documenting 2 ways to setup your own Whanos instance

## Docker-Compose

### Prerequisites

- Docker
- Docker-compose

When cloning the Whanos repository, you will find a `docker-compose.yml` file

**Note:** You must set some environment variables
 -  `JENKINS_ADMIN_PASSWORD`, it will be the admin password
 - `JENKINS_DOCKER_REGISTRY`, it will be the base URL for docker push (e.g., for GCloud Artifact Registry : europe-west1-docker.pkg.dev/plucky-agency-332314)

Run
```bash
docker-compose up --build
```
You're done !

Your Jenkins instance is running on `localhost:8080`


## Helm (Kubernetes)

### Prerequisites
- kubectl
- helm
- be connected to a cluster that supports LoadBalancers

Whanos supports these options

Empty variables have to be filled
```yaml
whanos:
  jenkins:
    # the repository to pull the jenkins image created by docker build . (at the root of the repo)
    image: 
    # the password has to be encoded in base64
    adminpassword: 
  docker:
	# this is the docker registry base url
    registry:
    # by default another pod is running using dind
    host: tcp://dind-service:2375
```

Run
```bash
helm install whanos helm/Whanos -f my_whanos_config.yml
```
It's now running ! Check your cluster to find the LoadBalancer external IP