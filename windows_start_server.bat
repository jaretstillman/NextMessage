@echo off

java -version
if %errorlevel% neq 0 goto :nojava
javac -version
if %errorlevel% neq 0 goto :nojdk

for /f "tokens=2" %%g in ('javac -version 2^>^&1 ^| findstr /i "javac"') do (
    set JAVACVER=%%g
)
set JAVA_HOME=C:\Program Files\Java\jdk%JAVACVER%
set PATH=%PATH%;%JAVA_HOME%\bin;
goto :installfont

:nojdk
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVAVER=%%g
)
set JAVAVER=%JAVAVER:"=%
set JRE_HOME=C:\Program Files\Java\jre%JAVAVER%
set PATH=%PATH%;%JRE_HOME%\bin;

:installfont

if exist %windir%\Fonts\Roboto-Regular.ttf goto :start
echo Installing Roboto...

copy webapps\NextMessage-Project\WEB-INF\classes\js\nextmessage\resources\*.ttf %windir%\Fonts 
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-Black (TrueType)" /t REG_SZ /d Roboto-Black.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-BlackItalic (TrueType)" /t REG_SZ /d Roboto-BlackItalic.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-Bold (TrueType)" /t REG_SZ /d Roboto-Bold.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-BoldItalic (TrueType)" /t REG_SZ /d Roboto-BoldItalic.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-Italic (TrueType)" /t REG_SZ /d Roboto-Italic.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-Light (TrueType)" /t REG_SZ /d Roboto-Light.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-LightItalic (TrueType)" /t REG_SZ /d Roboto-LightItalic.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-Medium (TrueType)" /t REG_SZ /d Roboto-Medium.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-MediumItalic (TrueType)" /t REG_SZ /d Roboto-MediumItalic.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-Regular (TrueType)" /t REG_SZ /d Roboto-Regular.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-Thin (TrueType)" /t REG_SZ /d Roboto-Thin.ttf /f
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Roboto-ThinItalic (TrueType)" /t REG_SZ /d Roboto-ThinItalic.ttf /f

:start
cd webapps
@RD /s /q NextMessage-Project
cd ..
cd bin
call startup.bat

ping -n 7 127.0.0.1 > nul
cd ..
@echo on
cd webapps/NextMessage-Project/WEB-INF/classes/js/nextmessage/resources
ngrok_windows http 8080

pause
exit

:nojava
echo You need to install java!
echo Go to http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html 
echo To install the latest windows JRE
pause
exit