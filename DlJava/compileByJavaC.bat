rmdir /S /Q jbin
mkdir jbin
javac -d jbin -encoding UTF-8 src\net\digicre\digilaun\*.java src\net\digicre\digilaun\work\*.java
cd jbin
jar cfe ..\DigiLaun.jar net.digicre.digilaun.DigiLaun .
PAUSE
