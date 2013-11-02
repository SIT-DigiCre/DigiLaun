rmdir /S /Q jbin jdoc
mkdir jbin jdoc
javac   -d jbin -encoding UTF-8 src\net\digicre\digilaun\*.java src\net\digicre\digilaun\work\*.java
javadoc -d jdoc -encoding UTF-8 -private -sourcepath src -subpackages net.digicre.digilaun -quiet
cd jbin
jar cfe ..\DigiLaun.jar net.digicre.digilaun.DigiLaun .
PAUSE
