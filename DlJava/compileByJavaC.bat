rmdir /S /Q jbin jdoc
mkdir jbin jdoc
javac   -d jbin -encoding UTF-8 -source 1.6 -target 1.6 src\net\digicre\digilaun\*.java src\net\digicre\digilaun\work\settable\*.java src\net\digicre\digilaun\work\*.java src\net\digicre\digilaun\config\*.java src\net\digicre\digilaun\config\regworks\*.java
javadoc -d jdoc -encoding UTF-8 -source 1.6 -docencoding UTF-8 -author -private -sourcepath src -subpackages net.digicre.digilaun -quiet
cd jbin
jar cfe ..\DigiLaun.jar net.digicre.digilaun.DigiLaun        net\digicre\digilaun\work\*.class net\digicre\digilaun\*.class
jar cfe ..\config.jar   net.digicre.digilaun.config.DlConfig net\digicre\digilaun\work         net\digicre\digilaun\Config.class net\digicre\digilaun\Config$*.class net\digicre\digilaun\config
PAUSE
