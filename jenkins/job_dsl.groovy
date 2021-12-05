folder("Whanos base images") {
	description("The base images of whanos.")
}

folder("Projects") {
	description("The available projets in whanos.")
}

languages = ["c", "java", "javascript", "python", "befunge"]

languages.each { language ->
	freeStyleJob("Whanos base images/whanos-$language") {
		steps {
			shell("docker build /images/$language -f /images/$language/Dockerfile.base -t whanos-$language")
		}
	}
}

freeStyleJob("Whanos base images/Build all base images") {
	publishers {
		downstream(
			languages.collect { language -> "Whanos base images/whanos-$language" }
		)
	}
}

freeStyleJob("GCloud and GKE Login") {
	parameters {
		stringParam("GCLOUD_PROJECT_ID", null, "Gcloud Project id (ex: plucky-agency-332291)")
		stringParam("GCLOUD_SERVICE_ACCOUNT_MAIL", null, "format: service-account-name@project-id.iam.gserviceaccount.com")
		fileParam("gcloud-service-account-key.json", "The account service key file: https://cloud.google.com/iam/docs/creating-managing-service-account-keys")
		stringParam("GCLOUD_GKE_CLUSTER_NAME", null, "The name of your GKE cluster")
		stringParam("GCLOUD_GKE_CLUSTER_LOCATION", null, 'The correct location of your GKE cluster: ex: europe-west1-b')
	}
	steps {
		shell("gcloud auth activate-service-account \$GCLOUD_SERVICE_ACCOUNT_MAIL --key-file=gcloud-service-account-key.json  --project=\$GCLOUD_PROJECT_ID")
		shell("gcloud auth configure-docker europe-west1-docker.pkg.dev")
		shell("gcloud config set compute/zone \$GCLOUD_GKE_CLUSTER_LOCATION")
		shell("gcloud container clusters get-credentials \$GCLOUD_GKE_CLUSTER_NAME")
	}
}

freeStyleJob("link-project") {
	parameters {
		stringParam("GIT_URL", null, 'Git repository url (e.g.: "https://github.com/Octopus773/ts-hello-world.git")')
		stringParam("DISPLAY_NAME", null, "Display name for the job")
	}
	steps {
		dsl {
			text('''
				freeStyleJob("Projects/$DISPLAY_NAME") {
					scm {
						git {
							remote {
								name("origin")
								url("$GIT_URL")
							}
						}
					}
					triggers {
						scm("* * * * *")
					}
					wrappers {
						preBuildCleanup()
					}
					steps {
						shell("/jenkins/deploy.sh \\"$DISPLAY_NAME\\"")
					}
				}
			''')
		}
	}
}
