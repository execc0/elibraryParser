# elibraryParser

## Build

### Requirements
* jdk 24+

### Window
If you are using Windows, follow these steps:

    .\gradlew.bat build
    cd app\build\libs

And then just run RUN.bat
    
### Linux / Mac 
If you want to build this project on Linux or Mac, follow these steps:

    ./gradlew build
    cd app/build/libs
    chmod +x RUN.sh
    
And then just run RUN.sh

### Potential problem
If you experience the following error on Linux or macOS:

    Graphics Device initialization failed for :  es2, sw
    Error initializing QuantumRenderer: no suitable pipeline found ...
    
Then replace the JavaFX SDK in the __*app/build/libs*__ directory with the version for your operating system. https://jdk.java.net/javafx25/

Also, due to the use of JDK 24+, explicit permission may be required for modules using native code. To do this, change the content RUN.sh for the following:
    
    #!/bin/bash

    # ELibrary Parser Startup Script
    echo -ne "\033]0;ELibrary Parser\007"
    echo "Starting ELibrary Parser..."

    java --module-path "javafx-sdk-25.0.1/lib" \
         --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics \
         --enable-native-access=javafx.graphics \
         -jar app.jar

    read -p "Press Enter to continue..."
    
## Testing

To test the application, you can use the files in the __*testData*__ directory. In case of successful analysis in the author file.The json should contain 178 lines of information about 3 authors.
some text
hmmm
