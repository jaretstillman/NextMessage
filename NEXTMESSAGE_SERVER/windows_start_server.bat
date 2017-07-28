@echo off
cd webapps
@RD /s /q NextMessage
cd ..
cd bin
call startup.bat

pause

cd ..
cd webapps/NextMessage/WEB-INF/classes/js/nextmessage/resources
ngrok_windows http 8080