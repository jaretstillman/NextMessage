#!/bin/bash

if ! [ -x "$(command -v java)" ]; then
	echo You need to install java! Go to
	echo http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
	echo And install the newest Mac OSX .dmg file
	exit 1
fi

if [ -z "$(fc-list | grep -i "roboto" > /dev/null)" ]; then
	echo Installing Roboto...
	cp webapps/NextMessage-Project/WEB-INF/classes/js/nextmessage/resources/*.ttf /Library/Fonts
fi

cd /
cd ${0%/*}

export JAVA_HOME=/Library/Java/Home

cd bin
xterm -hold -e startup.sh

sleep 7

cd ..
cd webapps/NextMessage-Project/WEB-INF/classes/js/nextmessage/resources
./ngrok_osx http 8080