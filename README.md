Particles live wallpaper
======================================

A live wallpaper featuring particle-like dots bouncing off each other and the walls.


Compiling
=========

TL;DR ; I experimented with Gradle for this project.


Release
-------

To compile particles, you need a release key. A howto is available at <https://developer.android.com/tools/publishing/app-signing.html>. Here's an example for generating the key (only do this once):

    keytool -genkey -keystore particles.keystore -alias particles -keyalg RSA -keysize 2048 -validity 10000
    echo "keystore=particles.keystore" > key.properties
    echo "keystore.alias=particles" >> key.properties
    echo "keystore.password=ENTER_PASSWORD_HERE" >> key.properties
    echo "particles.properties=PATH_TO_SOURCE_TREE/key.properties" >> ~/.gradle/gradle.properties"

And to actually build the program:

    ./gradlew assemble

