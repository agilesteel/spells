[Java 1.6]: http://java.com/en/download/index.jsp
[SBT 0.11.3]: https://github.com/harrah/xsbt/wiki
[here]: http://earlofsteel.de/trunk/spells/spells_2.9.2-1.1.jar

# spells

This is a small scala "util" library, which will hopefully grow over time.

## Features:

* AnsiPrint - ansi styled outputs for your strings

## Usage

### Requirements 

* [Java 1.6] or greater
* [SBT 0.11.3] or greater if you want to build from source

### Configuring your project's dependencies

Add this line to your SBT build file (spells is currently built only against Scala 2.9.2):
```scala
libraryDependencies += "com.github.agilesteel" %% "spells" % "1.1"
```

### Examples

Here is an example of ``AnsiPrint`` in action:

```scala
package myproject

import spells._
import Spells._
 
object Ansi extends App {
   println("white")
   println("green")(Green)
   println("green".green)
   println(1337.cyan)
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
   implicit val defaultStyle = "someStyle".s
   println("styled")
}
```