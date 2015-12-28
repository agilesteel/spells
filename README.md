[Java 8]:         http://java.com/en/download/index.jsp
[SBT 0.13.9]:     http://www.scala-sbt.org/

# spells: devtime utilities for Scala

[![Stories in Ready](https://badge.waffle.io/agilesteel/spells.png?label=ready&title=Ready)](https://waffle.io/agilesteel/spells)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.agilesteel/spells_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.agilesteel/spells_2.11)
[![License](http://img.shields.io/:license-Apache%202-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Build Status](https://travis-ci.org/agilesteel/spells.svg?branch=master)](https://travis-ci.org/agilesteel/spells)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/aeb5e73b4a0e4ad98888505a544f3e7c)](https://www.codacy.com/app/agilesteel/spells)
[![Coverage Status](https://img.shields.io/coveralls/agilesteel/spells.svg)](https://coveralls.io/r/agilesteel/spells)
[![Join the chat at https://gitter.im/agilesteel/spells](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/agilesteel/spells?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Features:

For the full list of features, please refer to the [wiki](https://github.com/agilesteel/spells/wiki).

## Usage

### Requirements

* [Java 8] or greater
* [SBT 0.13.9] or greater if you want to build from source

### Configuring your project's dependencies

**Releases:** add these lines to your SBT build file:

```scala
resolvers += Resolver.sonatypeRepo("releases") // optional, but quicker

libraryDependencies += "com.github.agilesteel" %% "spells" % "1.6.1"
```

**Snapshots:** add these lines to your SBT build file:

```scala
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "com.github.agilesteel" %% "spells" % "2.0.0-SNAPSHOT"
```
