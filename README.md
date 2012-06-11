[Java 1.6]: http://java.com/en/download/index.jsp
[SBT 0.11.3]: https://github.com/harrah/xsbt/wiki

# spells 1.0

This is a small scala "util" library, which will hopefully grow over time.

## Features:

* AnsiPrint - ansi styled outputs for your strings

## Setup

### Requirements 

* [Java 1.6] or greater
* [SBT 0.11.3]
* Internet connection (in order for SBT to be able to download the necessary dependencies)

### Configuring your project's dependencies

SBT allows you to pull dependencies directly from github. Unfortunately the light build configuration is not sufficient for this. You have to use the full build configuration which might look like this:

```scala
import sbt._
import Keys._

object MyBuild extends Build {
   lazy val myProject = Project("My Project", file(".")) dependsOn spells
   lazy val spells = RootProject(uri("git://github.com/agilesteel/spells.git"))
}
```

### Usage

Here is an example of ``AnsiPrint`` in action:

```scala
package myproject

import spells._
import Spells._
 
object Ansi extends App {
   println("white")
   println("green")(Green)
   println("green".green)
   println("green".magenta)(Green)
   println("yellow".yellow + "green".green + "yellow".yellow)(Yellow)
   println("yellow" + "green".green)(Yellow)
   println("yellow".yellow + "green")(Green)
   println("yellow" + "green".green + "yellow")(Yellow)
   println("yellow".yellow + "green" + "yellow".yellow)(Green)
   println("yellow" + "green".green + "yellow".yellow)(Yellow)
   println("yellow".yellow + "green".green + "yellow")(Yellow)
   println("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow)
}

object OverridingDefaultStyle extends App {
   implicit val defaultStyle = Yellow
   println("yellow" + "red".red + "yellow" + "green".green + "yellow")
}

object OverridingDefaultStyleWithAnything extends App {
   implicit val defaultStyle = "someStyle".s // <- converts Strings to AnsiStyles
   println("styled")
}
```