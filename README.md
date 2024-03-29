# README #

The repo contains the setup for a simple REST application using JAX-RS.

### What is this repository for? ###

* The project is setup to use Gradle, Java 17, and Apache Tomcat 10 all running on Ubuntu 20.04
* The assumption is that you're starting with a fresh installation of Ubuntu

### What API does this project implement? ###

* The API definition for the project is [here](http://cs.iit.edu/~virgil/cs445/mail.spring2022/project/project-api.html)

### How do I get set up? ###

(i) Install openjdk-17-jdk

`sudo apt install openjdk-17-jdk`

(ii) Install Gradle 7.4

* Download the Gradle binary from `https://services.gradle.org/distributions/gradle-7.4-bin.zip`
* Unzip to the destination directory:
```
cd ~/Downloads ; sudo unzip -d /opt/gradle gradle-7.4-bin.zip
sudo ln -s /opt/gradle/gradle-7.4 /opt/gradle/latest
```
* Create a profile file for Gradle:
```
sudo vi /etc/profile.d/gradle.sh
```
* Add the following two lines to the file, then save and exit vi:
```
export GRADLE_HOME=/opt/gradle/latest
export PATH=${GRADLE_HOME}/bin:${PATH}
```
* Finalize the setup:
```
chmod +x /etc/profile.d/gradle.sh
source /etc/profile.d/gradle.sh
```

(iii) Install Apache Tomcat 10

* Download the binary from `https://dlcdn.apache.org/tomcat/tomcat-10/v10.0.16/bin/apache-tomcat-10.0.16.zip`

* Unzip to the destination directory:
```
cd ~/Downloads ; sudo unzip -d /opt/tomcat apache-tomcat-10.0.16.zip`
```
(iv) Clone this repository:
```
https://github.com/jackie-mcaninch/CS445-project`
```
(v) Build the executable:
```
cd CS445-project/BuyNothing
./gradlew clean
./gradle build
```

(vi) How to run tests

```
./gradlew build
```
You'll find basic coverage information under `lib/build/reports/tests/test/index.html`

If you want detailed unit test coverage then execute the jacocoTestReport task:
```
./gradlew jacocoTestReport
```
The html coverage report is available at `lib/build/reports/jacoco/test/html/index.html`

Or, to make things easier, a Makefile has been provided in the BuyNothing directory. To generate the .war file only, run `make`. To generate and open the test report, first install `xdg-open` using apt-get and then run `make test`. To remove any previous builds, run `make clean`.

(vii) Deployment instructions

* Start tomcat
```
/opt/tomcat/bin/startup.sh
```
* Point your browser to `http://localhost:8080`, you should see the Tomcat banner page
* Select Manager App; if you get an error you may have to edit `/opt/tomcat/conf/tomcat-users.xml`
* Scroll down the page to the 'WAR file to deploy' section and Browse to the war file created by the 'build' task, should be at `lib/build/libs/bn.war`, select it and then press 'Open'
* Press 'Deploy'
* Verify that everything is ok by pointing your browser to `http://localhost:8080/bn/api/accounts`.  Alternatively you could do
```
curl -i "http://localhost:8080/bn/api/accounts"
```
which will print both the response HTTP header and the body.

### Run the Postman test suite

* Launch Postman. If this is the first time you're using the tool, select "Workspaces" from the menu on the upper left and then press "Create Workspace". Name the workspace something meaningful, such as "cs445 REST Project", select "Personal" for Visibility and then press "Create Workspace".
* Select the newly created workspace from the "Workspaces" menu. The name of the workspace will be visible in the upper left corner, below the Home menu item.
* Go to "File > Import", select "Link" from the top of the box and enter the following link: https://www.getpostman.com/collections/aa2b389418c634499d30 and then press "Continue" and "Import". On the left pane select the "Collections" tab: you should see a collection called "REST Lamp Tests".
* Create an environment in which to run the automated suite:
    + Press the "New" button in the upper left corner of the main Postman's screen and select "Environment" for the menu.
    + Choose a name for the new environment, such as "cs445 REST - localhost" and create a variable named base_url of type 'default' and with the Initial and Current Values of http://localhost:8080/bn/api, then press "Save".
* Start the server that accepts and responds to REST requests for your application. The test scripts will be running against the base URL defined by the base_url variable.
* Run the automated test suite:
    + Select the "Buy Nothing Tests" from Collections.
    + Hover over the "Buy Nothing Tests" name, press the ellipsis on the right of the name, and select "Run Collection" from that menu.
  Make sure the "cs445 REST - localhost" is selected from the pull down menu in the upper right.
    + Click on the blue "Run Buy Nothing Tests" to find out how many of the tests are passing.
