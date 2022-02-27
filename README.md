# README #

The repo contains the setup for a simple REST application using JAX-RS.

### What is this repository for? ###

* The project is setup to use Gradle, Java 17, and Apache Tomcat 10 all running on Ubuntu 20.04
* The assumption is that you're starting with a fresh installation of Ubuntu

### What API does this project implement? ###

* The project requirements are [ here ] (http://cs.iit.edu/~virgil/cs445/mail.spring2022/sample-rest-code/requirements.html)
* The API definition for the project is [ here ] (http://cs.iit.edu/~virgil/cs445/mail.spring2022/sample-rest-code/api.html)

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
git clone git@bitbucket.org:vbistriceanu/2022-rest-jaxrs-lamp.git`
```
(v) Build the executable:
```
cd 2022-rest-jaxrs-lamp/Rlamp
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

(vii) Deployment instructions

* Start tomcat
```
/opt/tomcat/bin/startup.sh
```
* Point your browser to `http://localhost:8080`, you should see the Tomcat banner page
* Select Manager App; if you get an error you may have to edit `/opt/tomcat/conf/tomcat-users.xml`
* Scroll down the page to the 'WAR file to deploy' section and Browse to the war file created by the 'build' task, should be at `lib/build/libs/rest-lamp.war`, select it and then press 'Open'
* Press 'Deploy'
* Verify that everything is ok by pointing your browser to `http://localhost:8080/rest-lamp/api/demo/cat`.  Alternatively you could do
```
curl -i "http://localhost:8080/rest-lamp/api/demo/cat"
```
which will print both the response HTTP header and the body.


### Who do I talk to? ###

* Email bistriceanu@iit.edu
