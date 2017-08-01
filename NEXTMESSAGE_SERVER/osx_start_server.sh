#!/bin/bash

cd webapps
rm -rf NextMessage
cd ..
cd bin
./startup.sh

sleep 5

cd ..
cd webapps/NextMessage/WEB-INF/classes/js/nextmessage/resources
./ngrok_osx http 8080



