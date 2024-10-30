#!/bin/bash
# Compile Zigzag.java
javac -cp "lib/SwiftBotAPI-5.1.0.jar" -d bin src/Zigzag.java src/subprocesses/Converters.java src/subprocesses/Lights.java src/subprocesses/Qrcode.java src/subprocesses/Movement.java src/subprocesses/Timer.java src/subprocesses/SaveMaker.java
java -cp "lib/SwiftBotAPI-5.1.0.jar:bin" Zigzag