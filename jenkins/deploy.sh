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

if [[ -f Dockerfile ]]; then
	docker build . -t whanos-${LANGUAGE[0]}-standalone
else
	docker build . \
		-f /images/${LANGUAGE[0]}/Dockerfile.standalone \
		-t whanos-${LANGUAGE[0]}-standalone
fi

if [[ -f whanos.yml ]]; then
	# TODO deploy to kubernetes (img is tagged as whanos-${LANGUAGE[0]}-standalone)
fi