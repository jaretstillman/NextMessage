#!/bin/bash

if[ -n `which java`]; then
	echo You need to install java! Go to
	echo http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
	echo And install the newest Mac OSX .dmg file
	exit 1
fi

cd /
cd ${0%/*}

export JAVA_HOME=/Library/Java/Home

cd bin
xterm -e startup.sh

sleep 5

cd ..
cd webapps/NextMessage/WEB-INF/classes/js/nextmessage/resources
./ngrok_osx http 8080