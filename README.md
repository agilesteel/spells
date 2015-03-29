[![Stories in Ready](https://badge.waffle.io/agilesteel/spells.png?label=ready&title=Ready)](https://waffle.io/agilesteel/spells)
[Java 1.7]:       http://java.com/en/download/index.jsp
[SBT 0.13.5]:     http://www.scala-sbt.org/

[Ansi]:           https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/AnsiTests.scala
[AnyOps]:         https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/AnyOpsTests.scala
[ClearPrint]:     https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/ClearPrintTests.scala
[Clipboard]:      https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/ClipboardTests.scala
[ErrorPrint]:     https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/ErrorPrintTests.scala
[HumanRendering]: https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/HumanRenderingTests.scala
[Misc]:           https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/MiscTests.scala
[StringOps]:      https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/StringOpsTests.scala
[StylePrint]:     https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/StylePrintTests.scala
[Xray]:           https://github.com/agilesteel/spells/blob/master/src/test/scala/spells/user/XrayTests.scala

# spells

This is a small scala "util" library, which will hopefully grow over time.

[![Build Status](https://travis-ci.org/agilesteel/spells.svg?branch=master)](https://travis-ci.org/agilesteel/spells)
[![Coverage Status](https://img.shields.io/coveralls/agilesteel/spells.svg)](https://coveralls.io/r/agilesteel/spells)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.agilesteel/spells_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.agilesteel/spells_2.11)
[![License](http://img.shields.io/:license-Apache%202-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

## Features:

* [Ansi] - Ansi styled outputs for your strings
* [AnyOps] - Additional operations on `Any`
* [ClearPrint] - clearPrintln (let's you stay on the same line)
* [Clipboard] - Clipboard.writeString and Clipboard.readString
* [ErrorPrint] - printerr (eases styling of errors)
* [HumanRendering] - rendering for things like duration
* [Misc] - noop
* [StringOps] - Additional operations on `String`
* [StylePrint] - println on steroids
* [Xray] - call .xray on any expression to see what's inside of it ;)

## Usage

### Requirements

* [Java 1.7] or greater
* [SBT 0.13.5] or greater if you want to build from source

### Configuring your project's dependencies

Releases: add these lines to your SBT build file:
```scala
resolvers += Resolver.sonatypeRepo("releases") // optional, but quicker

libraryDependencies += "com.github.agilesteel" %% "spells" % "1.6.1"
```

Snapshots: add these lines to your SBT build file:
```scala
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "com.github.agilesteel" %% "spells" % "1.6.2-SNAPSHOT"
```
### Help
If you clone this repository, SBT will guide you through the man pages!

### Examples
```scala
package company.project

package object util extends spells.Spells

import company.project.util._

object Ansi extends App {
   println("white")
   println("green")(Green)
   println("green".green)
   println(1337.cyan)
   println("magenta".magenta)(Green)
   println("yellow".yellow + "green".green + "yellow".yellow)(Yellow)
   println("yellow" + "green".green)(Yellow)
   println("yellow".yellow + "green")(Green)
   println("yellow" + "green".green + "yellow")(Yellow)
   println("yellow".yellow + "green" + "yellow".yellow)(Green)
   println("yellow" + "green".green + "yellow".yellow)(Yellow)
   println("yellow".yellow + "green".green + "yellow")(Yellow)
}

object UsingCustomStyles extends App {
   val customStyle = "someStyle".toAnsiStyle
   println("styled" in customStyle)
}

object OverridingDefaultStyle extends App {
   implicit val defaultStyle = Yellow
   println("yellow" + "red".red + "yellow" + "green".green + "yellow")
}

object OverridingDefaultStyleWithCustomStyle extends App {
   implicit val defaultStyle = "someStyle".toAnsiStyle
   println("styled")
}
```
