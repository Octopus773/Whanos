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
		stringParam("GCLOUD_GKE_CLUSTER_LOCATION", null, 'GCLOUD_GKE_CLUSTER_LOCATION')
		stringParam("GCLOUD_PROJECT_ID", null, "GCLOUD_PROJECT_ID")
		stringParam("GCLOUD_SERVICE_ACCOUNT_MAIL", null, "GCLOUD_SERVICE_ACCOUNT_MAIL")
		stringParam("GCLOUD_SERVICE_ACCOUNT_KEY_FILE", null, "GCLOUD_SERVICE_ACCOUNT_KEY_FILE")
		stringParam("GCLOUD_GKE_CLUSTER_NAME", null, "GCLOUD_GKE_CLUSTER_NAME")
	}
	steps {
		shell("gcloud auth activate-service-account \$GCLOUD_SERVICE_ACCOUNT_MAIL --key-file=/gcloud/here.json  --project=\$GCLOUD_PROJECT_ID")
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
