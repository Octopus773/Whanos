# Whanos Auto Deployment Documentation

Whanos offers you the possibility to automatically deploy your application, though the `whanos.yml` file located at the root of the repository.

Once the image of your git repository has been built successfully, we're looking at the presence of the `whanos.yml` file if found your application will be running in the cloud.

Example of `whanos.yml`:
```yaml
deployment:
  replicas: 1
  resources:
    limits:
      memory: "128M"
    requests:
      memory: "64M"
  ports:
    - 3000
```

The format of the `whanos.yml` is based on Kubernetes config files.

### Replicas
Replicas is the number of simultaneous instances running your app.
It's useful if you want to make a resilient system, The charge is distributed automatically between the different instances. 
Also, when one instance is down, another one can replace it during the restart time and no downtime is experienced.

### Resources
Managing the resources you consume is really important, and the reasons are infinite:
- A resource heavy application can choke other applications running on the same machine, hurting their performances.
- If you're using a cloud provider, when you're paying what you consume, you don't want to see your app running full crazy at 100% CPU usage or 10 GB of RAM.

We're supporting the same syntax as Kubernetes -> [Kubernetes documentation](https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#resource-requests-and-limits-of-pod-and-container)

### Ports
This is the list of all used port by your application to communicate with the outside world. The automatic deployment will guarantee that the external requests are going to these specific ports.

The IP address accessible from the outside world is printed at the end of the Jenkins deployment job.