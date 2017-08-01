@echo off
cd webapps
@RD /s /q NextMessage
cd ..
cd bin
call startup.bat

ping -n 5 127.0.0.1 > nul

cd ..
cd webapps/NextMessage/WEB-INF/classes/js/nextmessage/resources
ngrok_windows http 8080