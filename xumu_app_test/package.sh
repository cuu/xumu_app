#!/bin/bash
#jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore /home/guu/my-release-key.keystore platforms/android/build/outputs/apk/android-debug.apk alias_name

zipalign -v 4 platforms/android/build/outputs/apk/android-debug.apk /tmp/app.apk

