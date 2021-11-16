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
			shell("docker build images/$language/Dockerfile.base")
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
		stringParam("GITHUB_NAME", null, 'GitHub repository owner/repo_name (e.g.: "EpitechIT31000/chocolatine")')
		stringParam("DISPLAY_NAME", null, "Display name for the job")
	}
	steps {
		dsl {
			text('''
				freeStyleJob("Projects/$DISPLAY_NAME") {
					triggers {
						scm("M/1")
					}
					scm {
						git {
							remote {
								name("origin")
								github("$GITHUB_NAME")
							}
						}
					}
					wrappers {
						steps {
							shell("make fclean")
							shell("make")
							shell("make tests_run")
							shell("make clean")
						}
					}
				}
			''')
		}
	}
}
