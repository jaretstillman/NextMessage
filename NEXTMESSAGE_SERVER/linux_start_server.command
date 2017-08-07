#!/bin/bash

if ! [ -n 'which java' ]; then
	echo You need to install java! Go to
	echo http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
	echo And install the newest Mac OSX .dmg file
	exit 1
fi

cd bin
chmod +x *.sh
./catalina.sh start

sleep 7

cd ..
cd webapps/NextMessage/WEB-INF/classes/js/nextmessage/resources
xterm -hold -e ./ngrok_linux http 8080
