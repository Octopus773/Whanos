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

freeStyleJob("link-project") {
	parameters {
		stringParam("GIT_URL", null, 'Git repository url (e.g.: "git@github.com:EpitechIT31000/chocolatine")')
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
						steps {
							shell("/jenkins/deploy.sh")
						}
					}
				}
			''')
		}
	}
}
