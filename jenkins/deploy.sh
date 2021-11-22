#! /bin/bash

LANGUAGE=()

if [[ -f Makefile ]]; then
	LANGUAGE+=("C")
fi
if [[ -f app/pom.xml ]]; then
	LANGUAGE+=("java")
fi
if [[ -f package.json ]]; then
	LANGUAGE+=("JS")
fi
if [[ -f requirements.txt ]]; then
	LANGUAGE+=("python")
fi
if [[ app/* == app/main.bf ]]; then
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