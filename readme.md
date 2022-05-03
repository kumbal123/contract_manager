# Contract Manager
This branch was created specifically for demonstrating purposes. Because the production database already has real data 
of customers and their contracts the project is configured to use local database HSQLDB.

## Building and running the app
For building and running the app we need to install the following:

### JDK - Java Development Kit
Firstly we need to download JDK. If you do not have it follow the instructions of the specific platform.

#### Windows
1. Visit website [ORACLE](https://www.oracle.com/java/technologies/downloads/#jdk17-windows) to download the latest 
version of JDK on Windows.

2. Set the environment variable `JAVA_HOME` so that it contains the absolute path to installed directory JDK. To do 
that open Command Prompt and set the variable `JAVA_HOME` with the following command.
    ```
    setx JAVA_HOME "<absolute_path_to_JDK"
    ```
    For example:
    ```
    setx JAVA_HOME "C:\ProgramFiles\Java\jdk-17.0.2"
    ```

3. After that set the environment variable `PATH` with the following command.
    ```
    setx PATH "%PATH%;%JAVA_HOME%\bin";
    ```

#### MacOs
1. Visit website [ORACLE](https://www.oracle.com/java/technologies/downloads/#jdk17-windows) to download the latest 
version of JDK on MacOS.

2. Set the environment variable `JAVA_HOME` so that it contains the absolute path to installed directory JDK. To do 
that open Terminal and follow the instructions bellow.
    ```
    echo $SHELL
    ```
    If you get `/bin/zsh` on your Terminal open the file `~/.zshenv` with some kind of an editor for example
    ```
    nano ~/.zshenv
    ```
    If you get `/bin/bash` on your Terminal open the file `~/.bash_profile` with some kind of an editor for example
    ```
    nano ~/.bash_profile
    ```
    Finally to set variable `JAVA_HOME` write the following line into the open file.
    ```
    export JAVA_HOME=<absolutní_cesta_k_adresáři_JDK>
    ```
3. Do not close the file yet as we also need to set the variable `PATH`. Copy paste the following line into the file.
    ```
    export PATH=$JAVA_HOME/bin:$PATH
    ```
    Save the file and close it.
### Apache Maven
1. Visit a website [Apache Maven](https://maven.apache.org/download.cgi) to download Apache Maven for example in 
.zip format. 
2. After installing it extract the content of the .zip file.
3. Lastly add the absolute path of directory `bin` located in directory `apache-maven` into the environment variable 
`PATH` like it was done while installing JDK.

After everything is installed and configured follow the instructions bellow:
1. Clone the repository.
2. Open Command Prompt or Terminal and move to the project directory that you just cloned.
3. To build the project write the following command.
    ```
    mvn clean package
    ```
4. To run the application write the following command.
    ```
    java -jar target/contract_manager-1.0-SNAPSHOT.jar 
    ```

## Generating installers from source code
Generating installers from source code requires JDK and Apache Maven. If you do not have it you can follow the 
instructions written above to install it.
Apart from that we also need:

### WiX Toolset - only for Windows
1. Visit a webpage [WiX Toolset](https://wixtoolset.org/) to download the latest release. 
2. After installing it configure the environment variable `PATH` like so
    ```
    setx PATH "%PATH%;<absolute_path_to_WixToolset>\bin";
    ```

### Generating installers with JPackage
As soon as everything required is installed and ready we can start with generating installers from source code 
with JPackage that is already present in JDK.
For generating installers follow the instructions of the specific platform.

#### Windows
1. Clone the repository.  
2. Open the Command Prompt and move to the project directory that you just cloned.
3. With Apache Maven write the following command into the Command Prompt.
    ```
    mvn clean package
    ```
4. After that generate installer with JPackage with the following command.
    ```
    jpackage -i ./target/ --main-class cz.fit.cvut.contract_manager.Main --main-jar contract_manager-1.0-SNAPSHOT.jar --name "ContractManager" --win-dir-chooser --win-shortcut --icon src/main/resources/pics/cm_icon.ico
    ```
#### MacOs
1. Clone the repository.  
2. Open the Terminal and move to the project directory that you just cloned.
3. With Apache Maven write the following command into the Command Prompt.
    ```
    mvn clean package
    ```
4. After that generate installer with JPackage with the following command.
    ```
    jpackage -i ./target/ --main-class cz.fit.cvut.contract_manager.Main --main-jar contract_manager-1.0-SNAPSHOT.jar --name "ContractManager"
    ```
For more information and options about JPackage visit the 
[guide](https://docs.oracle.com/en/java/javase/14/docs/specs/man/jpackage.html) from Oracle.
