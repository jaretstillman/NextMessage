@echo off

java -version
if %errorlevel% neq 0 goto :error

for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVAVER=%%g
)
set JAVAVER=%JAVAVER:"=%

set JRE_HOME=C:\Program Files\Java\jre%JAVAVER%
set PATH=%PATH%;%JRE_HOME%\bin;

cd webapps
@RD /s /q NextMessage
cd ..
cd bin
call startup.bat

ping -n 7 127.0.0.1 > nul
cd ..
cd webapps/NextMessage-Project/WEB-INF/classes/js/nextmessage/resources
ngrok_windows http 8080

pause
exit

:error
echo You need to install java!
echo Go to http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html 
echo To install the latest windows JRE
pause
exit