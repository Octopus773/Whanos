# GKE Connection, docker & kubectl config

Our Whanos offer a pre config GCloud connection job.

You must connect to a cluster and setup docker before trying to create any `link-project` otherwise the build will fail at the docker push step.

### Prerequisites
 - Gcloud kubernetes engine activated (GKE)
 - Service account created with needed rights:
	 - Artifact Registry Redactor
	 - Kubernetes Engine Developper
	 - List of all the precise permissions used
		 - artifactregistry.repositories.downloadArtifacts
		- artifactregistry.repositories.uploadArtifacts 
		- container.clusters.get
		- container.deployments.create
		- container.deployments.delete
		- container.deployments.get
		- container.deployments.update
		- container.nodes.list
		- container.secrets.create
		- container.secrets.delete
		- container.secrets.get
		- container.secrets.list
		- container.secrets.update
		- container.services.create
		- container.services.delete
		- container.services.get

On the Jenkins Dashboard, you will see a Job called `GCloud and GKE Login` click on it, then click on `Build with parameters`

You should see this

![](assets/gke_login.png)

You have to provide the following information:
 - `GCLOUD_PROJECT_ID`: This is your Gcloud Project Id
 - `GCLOUD_SERVICE_ACCOUNT_MAIL`: Your mail associated with your service account
	 format: service-account-name@project-id.iam.gserviceaccount.com
 - `gcloud-service-account-key.json`: The account service key file: https://cloud.google.com/iam/docs/creating-managing-service-account-keys
	Looks like this:
	```json
	{
        "type": "service_account",
        "project_id": "project-id",
        "private_key_id": "key-id",
        "private_key": "-----BEGIN PRIVATE KEY-----\nprivate-key\n-----END PRIVATE KEY-----\n",
        "client_email": "service-account-email",
        "client_id": "client-id",
        "auth_uri": "https://accounts.google.com/o/oauth2/auth",
        "token_uri": "https://accounts.google.com/o/oauth2/token",
        "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
        "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/service-account-email"
    }
	```
- `GCLOUD_GKE_CLUSTER_NAME`: The name of your GKE cluster
- `GCLOUD_GKE_CLUSTER_LOCATION`: The precise location of your GKE cluster: ex: `europe-west1-b`

After filling this information, you can press build.

If you have done everything correctly, the build will pass. Check the build log to obtain more information.