[Java 1.6]: http://java.com/en/download/index.jsp
[SBT 0.13.1]: http://www.scala-sbt.org/

[Ansi]:       src/test/scala/spells/AnsiTests.scala
[StylePrint]: src/test/scala/spells/StylePrintTests.scala
[ErrorPrint]: src/test/scala/spells/ErrorPrintTests.scala
[ClearPrint]: src/test/scala/spells/ClearPrintTests.scala

# spells

This is a small scala "util" library, which will hopefully grow over time.

## Features:

* [Ansi] - Ansi styled outputs for your strings
* [StylePrint] - println on steroids
* [ErrorPrint] - printerr (eases styling of errors)
* [ClearPrint] - clearPrintln (let's you stay on the same line)

## Usage

### Requirements

* [Java 1.6] or greater
* [SBT 0.13.1] or greater if you want to build from source

### Configuring your project's dependencies

Releases: add these lines to your SBT build file:
```scala
resolvers += Resolver.sonatypeRepo("releases") // optional, but quicker

libraryDependencies += "com.github.agilesteel" %% "spells" % "1.5.1"
```

Snapshots: add these lines to your SBT build file:
```scala
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += "com.github.agilesteel" %% "spells" % "1.6.0-SNAPSHOT"
```
### Help
If you clone this repository, SBT will guide you through the man pages!

### Examples
```scala
package myproject

import spells._

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
