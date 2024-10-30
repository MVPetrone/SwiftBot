#!/bin/bash
# Compile ZigZag.java
javac -cp "lib/SwiftBotAPI-5.1.0.jar" -d bin src/DrawShape.java src/subprocesses/QRCode.java src/subprocesses/Calibrate.java src/subprocesses/Draw.java src/subprocesses/Shape.java src/subprocesses/Prompt.java src/subprocesses/Controller.java src/subprocesses/SaveMaker.java src/subprocesses/Lights.java
java -cp "lib/SwiftBotAPI-5.1.0.jar:bin" DrawShape