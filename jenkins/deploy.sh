#! /bin/bash

LANGUAGE=()

if [[ -f Makefile ]]; then
	LANGUAGE+=("c")
fi
if [[ -f app/pom.xml ]]; then
	LANGUAGE+=("java")
fi
if [[ -f package.json ]]; then
	LANGUAGE+=("javascript")
fi
if [[ -f requirements.txt ]]; then
	LANGUAGE+=("python")
fi
if [[ $(find app -type f) == app/main.bf ]]; then
	LANGUAGE+=("befunge")
fi

if [[ ${#LANGUAGE[@]} == 0 ]]; then
	echo "Invalid project: no language matched."
	exit 1
fi
if [[ ${#LANGUAGE[@]} != 1 ]]; then
	echo "Invalid project: multiple language matched (${LANGUAGE[@]})."
	exit 1
fi
echo ${LANGUAGE[@]} matched

#image_name=eu.gcr.io/plucky-agency-332314/whanos-$1-${LANGUAGE[0]}
image_name=europe-west1-docker.pkg.dev/plucky-agency-332314/whanos/whanos-$1-${LANGUAGE[0]}

if [[ -f Dockerfile ]]; then
	docker build . -t $image_name
else
	docker build . \
		-f /images/${LANGUAGE[0]}/Dockerfile.standalone \
		-t $image_name
fi

docker push $image_name

if [[ -f whanos.yml ]]; then
	if helm status $1 &> /dev/null; then
		helm upgrade -f whanos.yml $1 /helm/AutoDeploy --set image.image=$image_name --set image.name=$1-name
	else
		helm install -f whanos.yml $1 /helm/AutoDeploy --set image.image=$image_name --set image.name=$1-name
	fi
else
	if helm status $1 &> /dev/null; then
		helm uninstall $1
	fi
fi