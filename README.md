# Welcome to cetereumj

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/cetereum/cetereumj?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/cetereum/cetereumj.svg?branch=master)](https://travis-ci.org/cetereum/cetereumj)
[![Coverage Status](https://coveralls.io/repos/cetereum/cetereumj/badge.png?branch=master)](https://coveralls.io/r/cetereum/cetereumj?branch=master)


# About
CetereumJ is a pure-Java implementation of the Cetereum protocol. For high-level information about Cetereum and its goals, visit [cetereum.org](https://cetereum.org). The [cetereum white paper](https://github.com/cetereum/wiki/wiki/White-Paper) provides a complete conceptual overview, and the [yellow paper](http://gavwood.com/Paper.pdf) provides a formal definition of the protocol.

We keep CetereumJ as thin as possible. For [JSON-RPC](https://github.com/cetereum/wiki/wiki/JSON-RPC) support and other client features check [Cetereum Harmony](https://github.com/ceter-camp/cetereum-harmony).

# Running CetereumJ

##### Adding as a dependency to your Maven project: 

```
   <dependency>
     <groupId>org.cetereum</groupId>
     <artifactId>cetereumj-core</artifactId>
     <version>1.12.0-RELEASE</version>
   </dependency>
```

##### or your Gradle project: 

```
   repositories {
       mavenCentral()
       jcenter()
       maven { url "https://dl.bintray.com/cetereum/maven/" }
   }
   implementation "org.cetereum:cetereumj-core:1.9.+"
```

As a starting point for your own project take a look at https://github.com/ceter-camp/cetereumj.starter

##### Building an executable JAR
```
git clone https://github.com/cetereum/cetereumj
cd cetereumj
cp cetereumj-core/src/main/resources/cetereumj.conf cetereumj-core/src/main/resources/user.conf
vim cetereumj-core/src/main/resources/user.conf # adjust user.conf to your needs
./gradlew clean fatJar
java -jar cetereumj-core/build/libs/cetereumj-core-*-all.jar
```

##### Running from command line:
```
> git clone https://github.com/cetereum/cetereumj
> cd cetereumj
> ./gradlew run [-PmainClass=<sample class>]
```

##### Optional samples to try:
```
./gradlew run -PmainClass=org.cetereum.samples.BasicSample
./gradlew run -PmainClass=org.cetereum.samples.FollowAccount
./gradlew run -PmainClass=org.cetereum.samples.PendingStateSample
./gradlew run -PmainClass=org.cetereum.samples.PriceFeedSample
./gradlew run -PmainClass=org.cetereum.samples.PrivateMinerSample
./gradlew run -PmainClass=org.cetereum.samples.TestNetSample
./gradlew run -PmainClass=org.cetereum.samples.TransactionBomb
```

##### For snapshot builds:
Please, note, snapshots are not stable and are currently in development! If you still want to try it:

 - Add https://oss.jfrog.org/libs-snapshot/ as a repository to your build script
 - Add a dependency on `org.cetereum:cetereumj-core:${VERSION}`, where `${VERSION}` is of the form `1.13.0-SNAPSHOT`.

Example:

    <repository>
        <id>jfrog-snapshots</id>
        <name>oss.jfrog.org</name>
        <url>https://oss.jfrog.org/libs-snapshot/</url>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
    <!-- ... -->
    <dependency>
       <groupId>org.cetereum</groupId>
       <artifactId>cetereumj-core</artifactId>
       <version>1.13.0-SNAPSHOT</version>
    </dependency>

##### Importing project to IntelliJ IDEA: 
```
> git clone https://github.com/cetereum/cetereumj
> cd cetereumj
> gradlew build
```
  IDEA: 
* File -> New -> Project from existing sources…
* Select cetereumj/build.gradle
* Dialog “Import Project from gradle”: press “OK”
* After building run either `org.cetereum.Start`, one of `org.cetereum.samples.*` or create your own main. 

# Configuring CetereumJ

For reference on all existing options, their description and defaults you may refer to the default config `cetereumj.conf` (you may find it in either the library jar or in the source tree `cetereum-core/src/main/resources`) 
To override needed options you may use one of the following ways: 
* put your options to the `<working dir>/config/cetereumj.conf` file
* put `user.conf` to the root of your classpath (as a resource) 
* put your options to any file and supply it via `-Dcetereumj.conf.file=<your config>`, accepts several configs, separated by comma applied in provided order: `-Dcetereumj.conf.file=<config1>,<config2>`
* programmatically by using `SystemProperties.CONFIG.override*()`
* programmatically using by overriding Spring `SystemProperties` bean 

Note that don’t need to put all the options to your custom config, just those you want to override. 

# Special thanks
YourKit for providing us with their nice profiler absolutely for free.

YourKit supports open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of <a href="https://www.yourkit.com/java/profiler/">YourKit Java Profiler</a>
and <a href="https://www.yourkit.com/.net/profiler/">YourKit .NET Profiler</a>,
innovative and intelligent tools for profiling Java and .NET applications.

![YourKit Logo](https://www.yourkit.com/images/yklogo.png)

# Contact
Chat with us via [Gitter](https://gitter.im/cetereum/cetereumj)

# License
cetereumj is released under the [LGPL-V3 license](LICENSE).

