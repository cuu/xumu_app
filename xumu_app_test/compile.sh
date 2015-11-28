#!/bin/bash
source /home/guu/env_phonegap.sh
echo "phonegap -version"
phonegap -version
echo "cordova -version"
cordova -version
echo "npm -version"
npm -version

echo "re plugin plugins...."
phonegap plugin remove edu.uic.travelmidwest.cordova.udptransmit
phonegap plugin remove com.example.hello
phonegap plugin remove guu.cordova.udpserver 

phonegap plugin add /media/sda1/home/cuu/Downloads/cordova-plugin-uic-udp/
phonegap plugin add /media/sda1/home/cuu/Downloads/cordova-plugin-hello/
phonegap plugin add /media/sda1/home/cuu/Downloads/cordova-plugin-guu-udp-server

echo "start to build..."

cordova build android
if [ $? -eq 0 ]; then
	rm /tmp/app.apk
	./package.sh
	COMPILE_OK=1
else
	COMPILE_OK=0
fi

if [ $COMPILE_OK -eq 1 ]; then
	echo "install the /tmp/app.apk to your phone now"
	adb install -r /tmp/app.apk 
fi

