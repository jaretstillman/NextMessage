#!/bin/bash

if ! [ -x "$(command -v java)" ]; then
	echo You need to install java! Go to
	echo http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
	echo And install the newest Mac OSX .dmg file
	exit 1
fi

d=`pwd`
cd /Library/Fonts
if [ ! -f ./Roboto-Regular.ttf ]; then
	echo Installing Roboto...
	cp $d/webapps/NextMessage-Project/WEB-INF/classes/js/nextmessage/resources/*.ttf /Library/Fonts
fi

cd $d

if [ -d /Library/Java/Home ]; then
	export JAVA_HOME=/Library/Java/Home
else
	export JAVA_HOME=/Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/
fi

cd bin
./startup.sh

sleep 10
cd ../webapps/NextMessage-Project/WEB-INF/classes/js/nextmessage/resources
./ngrok_osx http 8080
